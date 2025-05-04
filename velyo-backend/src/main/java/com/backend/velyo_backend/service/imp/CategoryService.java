package com.backend.velyo_backend.service.imp;

import com.backend.velyo_backend.Dto.CategoryDTO.CategoryDTO;
import com.backend.velyo_backend.Entity.Category;
import com.backend.velyo_backend.Exception.ResourceAlreadyExistsException;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import com.backend.velyo_backend.Mapper.CategoryMapper.CategoryMapper;
import com.backend.velyo_backend.Repository.AccommodationRepository;
import com.backend.velyo_backend.Repository.CategoryRepository;
import com.backend.velyo_backend.Util.BaseUrl;
import com.backend.velyo_backend.service.interfac.ICategoryService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Log4j2
@Service
public class CategoryService implements ICategoryService, BaseUrl {

    private final CategoryRepository categoryRepository;
    private final AccommodationRepository accommodationRepository;

    public CategoryService(CategoryRepository categoryRepository, AccommodationRepository accommodationRepository) {
        this.categoryRepository = categoryRepository;
        this.accommodationRepository = accommodationRepository;
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO, MultipartFile image) throws IllegalArgumentException, IOException {
        if (categoryRepository.findByName(categoryDTO.getName()).isPresent()){
            log.error("La categoria con nombre : {} ya existe", categoryDTO.getName());
            throw new IllegalArgumentException("La categoria con nombre: " + categoryDTO.getName() + " ya existe");
        }
        String fileName = saveImage(image);
        categoryDTO.setImage(fileName);
        Category categoryToSave = CategoryMapper.INSTANCE.dtoToEntity(categoryDTO);
        categoryRepository.save(categoryToSave);
        log.info("Categoria guardada: {}", categoryToSave.getName());
        return CategoryMapper.INSTANCE.entityToDto(categoryToSave);
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO, MultipartFile image) throws IOException {
        log.debug("Actualizando categoria: {}", categoryDTO.getId());

        Category categoryToUpdate = categoryRepository.findById(categoryDTO.getId()).orElseThrow(()->{
            log.error("La categoria con id: {} no fue encontrada", categoryDTO.getId());
            return new ResourceNotFoundException("La categoria con id: " + categoryDTO.getId()+ " no fue encontrada");
        });

        if (!categoryToUpdate.getName().equals(categoryDTO.getName())){
            categoryRepository.findByName(categoryDTO.getName()).ifPresent(category->{
                log.error("La categoria con nombre: {} ya existe", categoryDTO.getName());
                throw new ResourceAlreadyExistsException(
                        "La categoria con nombre: " + categoryDTO.getName() + " ya existe");
            });
        }

        if (image != null){
            categoryDTO.setImage(saveImage(image));
            deleteImage(categoryToUpdate.getImage());
        }else{
            categoryDTO.setImage(categoryToUpdate.getImage());
        }

        categoryToUpdate.setName(categoryDTO.getName());
        categoryToUpdate.setDescription(categoryDTO.getDescription());
        categoryToUpdate.setImage(categoryDTO.getImage());


        categoryRepository.save(categoryToUpdate);
        log.info("Categoria actualizada: {}", categoryToUpdate.getName());
        return CategoryMapper.INSTANCE.entityToDto(categoryToUpdate);
    }

    @Override
    @Transactional
    public Optional<CategoryDTO> findById(UUID id) {
        log.debug("Buscando categoria por: {}", id);
        return categoryRepository.findById(id)
                .map(CategoryMapper.INSTANCE::entityToDto);
    }

    @Override
    public Optional<CategoryDTO> findByName(String name) {
        log.debug("Buscando categoria por nombre: {}", name);
        return categoryRepository.findByName(name)
                .map(CategoryMapper.INSTANCE::entityToDto);
    }

    @Override
    public Page<CategoryDTO> findAll(Pageable pageable) {
        log.debug("Buscando todas las categorias paginadas: {}", pageable);
        Page<CategoryDTO> pageCategories =categoryRepository.findAll(pageable)
                .map(CategoryMapper.INSTANCE::entityToDto);
        pageCategories.forEach(categoryDTO -> {
            categoryDTO.setImage(getBaseUrl() + "/api/categories/images/" + categoryDTO.getImage());
        });
        return pageCategories;
    }

    @Override
    public List<CategoryDTO> findAllWithoutPagination() {
        log.debug("Buscando todas las categorias sin paginacion");
        List<CategoryDTO> categoryDTOS = categoryRepository.findAll().stream()
                .map(CategoryMapper.INSTANCE::entityToDto)
                .toList();
        categoryDTOS.forEach(categoryDTO -> {
            categoryDTO.setImage(getBaseUrl() + "/api/categories/images/" + categoryDTO.getImage());
        });
        return categoryDTOS;
    }

    @Override
    public void delete(UUID id) throws ResourceNotFoundException, IOException {
        log.debug("Borrando categoria con id: {}", id);
        Category categoryToDelete = categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Categoria con id no encontrada: " + id));

        accommodationRepository.findByCategory_IdIn(Set.of(categoryToDelete.getId())).forEach(accommodation -> {
            accommodationRepository.delete(accommodation);
        });

        categoryRepository.delete(categoryToDelete);
        if (!categoryRepository.existsById(id)){
            deleteImage(categoryToDelete.getImage());
        }
        log.info("Categoria borrada con id: {}", id);
    }

    public Resource getImage(String imageName) throws MalformedURLException{
        Path filePath = Paths.get("uploads/categories").resolve(imageName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists()){
            log.error("Imagen no encontrada: {}", imageName);
            throw new  ResourceNotFoundException("Imagen no encontrada: " + imageName);
        }
        return resource;
    }

    private String saveImage(MultipartFile image) throws IOException{
        String uploadDir = "uploads/categories";
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = timestamp + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    private void deleteImage(String imageName) throws IOException{
        Path imagePath = Paths.get("uploads/categories").resolve(imageName);
        Files.deleteIfExists(imagePath);
    }
}
