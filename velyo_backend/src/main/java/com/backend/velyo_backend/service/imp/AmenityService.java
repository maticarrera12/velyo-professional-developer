package com.backend.velyo_backend.service.imp;

import com.backend.velyo_backend.Dto.AmenityDTO.AmenityDTO;
import com.backend.velyo_backend.Entity.Amenity;
import com.backend.velyo_backend.Exception.ResourceAlreadyExistsException;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import com.backend.velyo_backend.Mapper.AmenityMapper.AmenityMapper;
import com.backend.velyo_backend.Repository.AccommodationRepository;
import com.backend.velyo_backend.Repository.AmenityRepository;
import com.backend.velyo_backend.Util.BaseUrl;
import com.backend.velyo_backend.service.interfac.IAmenityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Log4j2
@Service
public class AmenityService implements IAmenityService, BaseUrl {

    private final AmenityRepository amenityRepository;
    private final AccommodationRepository accommodationRepository;

    public AmenityService(AmenityRepository amenityRepository, AccommodationRepository accommodationRepository) {
        this.amenityRepository = amenityRepository;
        this.accommodationRepository = accommodationRepository;
    }

    @Override
    public AmenityDTO save(AmenityDTO amenityDTO, MultipartFile icon) throws IOException {
        amenityRepository.findByName(amenityDTO.getName()).ifPresent(amenity -> {
            log.error("La Amenity con nombre : {} ya existe", amenityDTO.getName());
            throw new ResourceAlreadyExistsException("La Amenity con nombre: " + amenityDTO.getName() + " ya existe");
        });
        String fileName = saveIcon(icon);
        amenityDTO.setIcon(fileName);
        Amenity amenityToSave = AmenityMapper.INSTANCE.dtoToEntity(amenityDTO);
        amenityRepository.save(amenityToSave);
        log.info("Amenity guardada: {}", amenityToSave.getName());
        return AmenityMapper.INSTANCE.entityToDto(amenityToSave);
    }

    @Override
    public AmenityDTO findById(UUID id) {
        return null;
    }

    @Override
    public AmenityDTO findByName(String name) {
        Amenity amenity = amenityRepository.findByName(name).orElseThrow(()->{
            log.error("La amenity con nombre: {} no fue encontrada", name);
            return new ResourceNotFoundException("La amenity con nombre: " + name + " no fue encontrada");
        });
        AmenityDTO amenityFound = AmenityMapper.INSTANCE.entityToDto(amenity);
        amenityFound.setIcon(getBaseUrl() + "/api/amenities/svg/" + amenityFound.getIcon());
        return amenityFound;
    }

    @Override
    public Page<AmenityDTO> findAll(Pageable pageable) {
        log.debug("Devolviendo todas las amenities");
        Page<AmenityDTO> pageAmenities = amenityRepository.findAll(pageable).map(AmenityMapper.INSTANCE::entityToDto);
        pageAmenities.forEach(amenityDTO -> {
            String svgUrl = getBaseUrl() + "/api/amenities/svg/" + amenityDTO.getIcon();
            amenityDTO.setIcon(svgUrl);
        });
        return pageAmenities;
    }

    @Override
    public List<AmenityDTO> findAllWithoutPagination() {
        log.debug("Devolviendo todas las amenities sin paginacion");
        List<AmenityDTO> amenities = amenityRepository.findAll().stream()
                .map(AmenityMapper.INSTANCE::entityToDto)
                .toList();
        amenities.forEach(amenityDTO -> {
            String svgUrl = getBaseUrl() + "/api/amenities/svg/" + amenityDTO.getIcon();
            amenityDTO.setIcon(svgUrl);
        });
        return amenities;
    }

    @Override
    public AmenityDTO update(AmenityDTO amenityDTO, MultipartFile icon) throws IOException {
        log.debug("Actualizando amenity:{}", amenityDTO.getId());
        Amenity amenityToUpdate = amenityRepository.findById(amenityDTO.getId()).orElseThrow(()->{
            log.error("La amenity con id: {} no fue encontrada", amenityDTO.getId());
            return new ResourceNotFoundException("La amenity con id " + amenityDTO.getId() + "no fue encontrada");
        });

        if (!amenityToUpdate.getName().equals(amenityDTO.getName())){
            amenityRepository.findByName(amenityDTO.getName()).ifPresent(amenity -> {
                log.error("La amenity con nombre: {} ya existe", amenityDTO.getName());
                throw new ResourceAlreadyExistsException("La amenity con nombre: " + amenityDTO.getName() + " ya existe");
            });
        }

        if (icon != null){
            amenityDTO.setIcon(saveIcon(icon));
            deleteIcon(amenityToUpdate.getIcon());
        }else amenityDTO.setIcon(amenityToUpdate.getIcon());

        amenityToUpdate = AmenityMapper.INSTANCE.dtoToEntity(amenityDTO);
        amenityRepository.save(amenityToUpdate);
        log.info("La amenity fue actualizada: {}", amenityToUpdate.getName());
        return AmenityMapper.INSTANCE.entityToDto(amenityToUpdate);
    }

    @Override
    public void delete(UUID id) throws IOException {
        log.debug("Borrando aenity con id: {}", id);
        Amenity amenityToDelete = amenityRepository.findById(id).orElseThrow(()->{
            log.error("La amenity con id: {} no fue encontrada", id);
            return new ResourceNotFoundException("La amenitie con id: " + id + " no fue encontrada");
        });

        accommodationRepository.findByAmenityIds(Set.of(id)).forEach(accommodation -> {
            accommodation.getAmenities().remove(amenityToDelete);
            accommodationRepository.save(accommodation);
        });

        amenityRepository.delete(amenityToDelete);
        if (!amenityRepository.existsById(id)){
            deleteIcon(amenityToDelete.getIcon());
        }
        log.info("La amenity fue borrada: {}", amenityToDelete.getName());
    }

    public Resource getIcon(String iconName) throws MalformedURLException{
        Path filePath = Paths.get("uploads/amenities").resolve(iconName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists()){
            log.error("El icon de la amenity no fue encontrado: {}", iconName);
            throw new ResourceNotFoundException("El icon de la amenity no fue encontrado: " + iconName);
        }

        return resource;
    }

    private String saveIcon(MultipartFile icon) throws IOException{
        String uploadDir = "uploads/amenities";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = timestamp + "_" + icon.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(icon.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    private void deleteIcon(String iconName) throws IOException{
        Path iconPath = Paths.get("uploads/amenities").resolve(iconName);
        Files.deleteIfExists(iconPath);
    }
}
