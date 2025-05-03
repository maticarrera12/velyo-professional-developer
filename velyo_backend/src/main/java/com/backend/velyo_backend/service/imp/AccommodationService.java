package com.backend.velyo_backend.service.imp;

import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationDTO;
import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSaveDTO;
import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSummaryDTO;
import com.backend.velyo_backend.Dto.ReviewDTO.ReviewSummaryDTO;
import com.backend.velyo_backend.Entity.*;
import com.backend.velyo_backend.Exception.ResourceAlreadyExistsException;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import com.backend.velyo_backend.Mapper.AccommodationMapper.AccommodationMapper;
import com.backend.velyo_backend.Mapper.AccommodationMapper.AccommodationSaveMapper;
import com.backend.velyo_backend.Mapper.AccommodationMapper.AccommodationSummaryMapper;
import com.backend.velyo_backend.Mapper.ReviewMapper.ReviewSummaryMapper;
import com.backend.velyo_backend.Repository.*;
import com.backend.velyo_backend.Util.BaseUrl;
import com.backend.velyo_backend.service.interfac.IAccommodationService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.*;
import java.util.stream.Collectors;


@Log4j2
@Service
public class AccommodationService implements IAccommodationService, BaseUrl {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationImageRepository accommodationImageRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final AmenityRepository amenityRepository;
    private final UserRepository userRepository;

    public AccommodationService(AccommodationRepository accommodationRepository, AccommodationImageRepository accommodationImageRepository, BookingRepository bookingRepository, ReviewRepository reviewRepository, AmenityRepository amenityRepository, UserRepository userRepository) {
        this.accommodationRepository = accommodationRepository;
        this.accommodationImageRepository = accommodationImageRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.amenityRepository = amenityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AccommodationSaveDTO save(AccommodationSaveDTO accommodationDTO, MultipartFile[] images) throws IOException {
        log.info(accommodationDTO);
        accommodationRepository.findByName(accommodationDTO.getName()).ifPresent(accommodation -> {
            log.error("El alojamiento con nombre: {} ya existe", accommodationDTO.getName());
            throw new ResourceAlreadyExistsException("El alojamiento con nombre: " + accommodationDTO.getName() + " ya existe");
        });

        log.info("Guardando el alojamineto: {}", accommodationDTO.getName());
        Set<String> imageNames = saveImages(images);
        accommodationDTO.setImages(imageNames);
        Accommodation accommodationToSave = AccommodationSaveMapper.INSTANCE.dtoToEntity(accommodationDTO);
        log.info("El alojamiento para guardar: {}", accommodationToSave);
        accommodationToSave.setAvgRating(0.0);
        accommodationRepository.save(accommodationToSave);
        log.info("El alojamiento fue guardado: {}", accommodationToSave.getName());
        return AccommodationSaveMapper.INSTANCE.entityToDto(accommodationToSave);
    }

    @Override
    public AccommodationDTO findById(UUID id) {
        log.debug("Buscando alojamiento por id: {}", id);

        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("El alojamiento con id: {} no fue encontrado", id);
                    return new ResourceNotFoundException("El alojamiento con id: " + id + " no fue encontrado");
                });

        // Obtener fechas no disponibles
        Set<LocalDate> unavailableDates = getUnavailableDates(id);

        // Obtener reviews
        Set<Review> reviews = reviewRepository.findByAccommodationId(id);

        // Mapear entidad a DTO
        AccommodationDTO accommodationFound = AccommodationMapper.INSTANCE.entityToDto(accommodation);

        // Mapear reviews a DTO
        Set<ReviewSummaryDTO> reviewsDTO = reviews.stream()
                .map(ReviewSummaryMapper.INSTANCE::entityToDto)
                .collect(Collectors.toSet());

        // Setear reviews y total
        accommodationFound.setReviews(reviewsDTO);
        accommodationFound.setTotalReviews(reviewsDTO.size());

        // Modificar URLs de imágenes
        accommodationFound.setImages(accommodationFound.getImages().stream()
                .map(image -> getBaseUrl() + "/api/accommodations/images/" + image)
                .collect(Collectors.toSet()));

        // Modificar íconos de amenities
        accommodationFound.setAmenities(accommodationFound.getAmenities().stream()
                .peek(amenity -> amenity.setIcon(getBaseUrl() + "/api/amenities/svg/" + amenity.getIcon()))
                .collect(Collectors.toSet()));

        // Setear fechas no disponibles
        accommodationFound.setUnavailableDates(unavailableDates);

        return accommodationFound;
    }

    @Override
    public AccommodationDTO findByName(String name) {
        log.debug("Buscando alojamiento por nombre: {}", name);
        Accommodation accommodation = accommodationRepository.findByName(name).orElseThrow(()->{
            log.error("El alojamiento con nombre: {} no fue encontrada", name);
            return new ResourceNotFoundException("El alojamiento con nombre " + name + " no fue encontrado");
        });
        AccommodationDTO accommodationFound = AccommodationMapper.INSTANCE.entityToDto(accommodation);
        accommodationFound.setImages(accommodationFound.getImages().stream()
                .map(image -> getBaseUrl() + "/api/accommodations/images/" + image)
                .collect(Collectors.toSet()));
        accommodationFound.setAmenities(accommodationFound.getAmenities().stream()
                .peek(amenity -> amenity.setIcon(getBaseUrl() + "/api/amenities/svg/" + amenity.getIcon()))
                .collect(Collectors.toSet()));
        return accommodationFound;
    }

    @Override
    @Transactional
    public Page<AccommodationDTO> findAll(Pageable pageable) {
        log.debug("Buscando todos los alojamientos paginados: {}", pageable);
        Page<AccommodationDTO> pageAccommodations = accommodationRepository.findAll(pageable).map(AccommodationMapper.INSTANCE::entityToDto);
        pageAccommodations.forEach(accommodationDTO -> {
            accommodationDTO.setTotalReviews(reviewRepository.findByAccommodationId(accommodationDTO.getId()).size());
            accommodationDTO.setImages(accommodationDTO.getImages().stream()
                    .map(image -> getBaseUrl() + "/api/accommodations/images/" + image)
                    .collect(Collectors.toSet()));
        });
        return pageAccommodations;
    }

@Override
public Set<AccommodationSummaryDTO> getRandomAccommodations(int size) {
    List<Accommodation> accommodations = accommodationRepository.findAll();

    if (accommodations.size() < size)
        size = accommodations.size();

    Collections.shuffle(accommodations);
    List<Accommodation> selected = accommodations.subList(0, size);

    Set<AccommodationSummaryDTO> result = selected.stream()
            .map(AccommodationSummaryMapper.INSTANCE::entityToDto)
            .collect(Collectors.toCollection(LinkedHashSet::new)); // conserva el orden

    result.forEach(accommodation -> {
        accommodation.setTotalReviews(reviewRepository.findByAccommodationId(accommodation.getId()).size());
        accommodation.setImages(accommodation.getImages().stream()
                .map(image -> getBaseUrl() + "/api/accommodations/images/" + image)
                .collect(Collectors.toSet()));
    });

    return result;
}


    @Override
    public void update(AccommodationSaveDTO accommodationDTO, MultipartFile[] images, Set<String> imagesToDelete) throws IOException {
        log.debug("Actualizando alojamiento: {}", accommodationDTO.getId());
        boolean hasImageToDelete = imagesToDelete != null && !imagesToDelete.isEmpty();
        Accommodation accommodationToUpdate = accommodationRepository.findById(accommodationDTO.getId()).orElseThrow(()->{
            log.error("El alojamiento con id: {} no fue encontrado", accommodationDTO.getId());
            return new ResourceNotFoundException("El alojamiento con id: " + accommodationDTO.getId() + " no fue encontrado");
        });

        if (!accommodationToUpdate.getName().equals(accommodationDTO.getName())){
            Optional<Accommodation> accommodationWithEqualName = accommodationRepository.findByName(accommodationDTO.getName());
            if (accommodationWithEqualName.isPresent() && !accommodationWithEqualName.get().getId().equals(accommodationDTO.getId())){
                log.error("El alojamiento con nombre: {} ya existe", accommodationDTO.getName());
                throw new ResourceAlreadyExistsException("El alojamiento con nombre: " + accommodationDTO.getName() + " ya existe");
            }
        }

        accommodationDTO.setImages(accommodationToUpdate.getImages()
                .stream()
                .map(AccommodationImage::getUrl)
                .collect(Collectors.toSet()));

        if (images != null){
            Set<String> imageNames = saveImages(images);
            accommodationDTO.getImages().addAll(imageNames);
        }

        if (hasImageToDelete){
            accommodationDTO.getImages().removeAll(imagesToDelete);
        }

        accommodationToUpdate = AccommodationSaveMapper.INSTANCE.dtoToEntity(accommodationDTO);
//        for (AccommodationImage accommodationImage : accommodationToUpdate.getImages()){
//            if (accommodationImageRepository.findByUrl(accommodationImage.getUrl()).isPresent())
//                accommodationImage.setId(accommodationImageRepository.findByUrl(accommodationImage.getUrl()).get().getId());
//        }

        if (accommodationDTO.getAmenities() != null) {
            Set<Amenity> updatedAmenities = accommodationDTO.getAmenities().stream()
                    .map(amenityId -> amenityRepository.findById(amenityId) // Aquí solo necesitas buscar por ID
                            .orElseThrow(() -> new ResourceNotFoundException("La amenity con id: " + amenityId + " no fue encontrada")))
                    .collect(Collectors.toSet());
            accommodationToUpdate.setAmenities(updatedAmenities);  // Asocia las amenities encontradas al alojamiento
        }


        accommodationToUpdate = accommodationRepository.save(accommodationToUpdate);

        if (hasImageToDelete){
            accommodationImageRepository.deleteByUrls(imagesToDelete);
        }
        log.debug("El alojamientoha ha sido actualizado: {}", accommodationToUpdate.getName());
    }

    @Override
    public Set<AccommodationSummaryDTO> findByCategoryIdsAndCountryOrCity(Set<UUID> categoryIds, String searchTerm, LocalDate checkIn, LocalDate checkOut) {
        log.debug("Encontrando alojamientos por categoria ids: {} y termino de busqueda: {}", categoryIds, searchTerm);
        Set<AccommodationSummaryDTO> accommodations;

        if((categoryIds == null || categoryIds.isEmpty()) && (searchTerm == null || searchTerm.isEmpty()) && checkIn == null && checkOut == null){
            accommodations = accommodationRepository.findAll().stream()
                    .map(AccommodationSummaryMapper.INSTANCE::entityToDto)
                    .collect(Collectors.toSet());
        }else{
            accommodations = accommodationRepository.findByCategoryAndCountryOrCityContainingIgnoreCase(categoryIds, searchTerm, checkIn, checkOut).stream()
                    .map(AccommodationSummaryMapper.INSTANCE::entityToDto)
                    .collect(Collectors.toSet());
        }

        log.debug("Devolviendo {} alojamientos", accommodations.size());
        accommodations.forEach(accommodation ->{
            accommodation.setTotalReviews(reviewRepository.findByAccommodationId(accommodation.getId()).size());
            accommodation.setImages(accommodation.getImages().stream()
                    .map(image-> getBaseUrl() + "/api/accommodations/images/" + image)
                    .collect(Collectors.toSet()));
        });
        return accommodations;
    }
    @Override
    public void delete(UUID id) throws ResourceNotFoundException, IOException {
        log.debug("Borrando alojamiento con id: {}", id);
        Accommodation accommodationToDelete = accommodationRepository.findById(id)
                .orElseThrow(()->{
                    log.error("El alojamiento con id: {} no fue encontrado", id);
                    return new ResourceNotFoundException("El alojamiento con id: " + id + " no se ha encontrado");
                });
        userRepository.findByFavoritesId(Collections.singleton(id)).forEach(user ->
                user.getFavorites().remove(accommodationToDelete));

        accommodationRepository.delete(accommodationToDelete);
        if (!accommodationRepository.existsById(id)){
            deleteFileImages(accommodationToDelete.getImages());
        }
        log.info("El alojamiento ha sido borrado: {}", accommodationToDelete.getName());
    }

    public Resource getImage(String imageName) throws MalformedURLException{
        log.debug("Obteniendo imagen con nombre: {}", imageName);
        Path imagePath = Paths.get("uploads/accommodations").resolve(imageName);
        Resource resource = new UrlResource(imagePath.toUri());
        if (!resource.exists()){
            log.error("La imagen no fue encontrada: {}", imageName);
            throw new ResourceNotFoundException("La imagen no fue encontrada" + imageName);
        }
        return resource;
    }

    public Set<LocalDate> getUnavailableDates(UUID id){
        log.debug("Obteniendo fechas no disponibles para el alojamiento con id: {}", id);
        List<Booking> bookings = bookingRepository.findByAccommodationId(id);

        return bookings.stream()
                .flatMap(booking -> booking.getCheckIn().datesUntil(booking.getCheckOut().plusDays(1)))
                .collect(Collectors.toSet());
    }

    private Set<String> saveImages(MultipartFile[] images) throws IOException{
        Set<String> imageNames = new HashSet<>();
        String uploadDir = "uploads/accommodations";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        for (MultipartFile image : images){
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String fileName = timestamp + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            imageNames.add(fileName);
        }
        return imageNames;
    }

    private void deleteFileImages(Set<AccommodationImage> imageNames) throws IOException{
        for (AccommodationImage imageName : imageNames){
            Path filePath = Paths.get("uploads/accommodations").resolve(imageName.getUrl()).normalize();
            Files.deleteIfExists(filePath);
        }
    }
}
