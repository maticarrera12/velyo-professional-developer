package com.backend.velyo_backend.Controller;


import com.backend.velyo_backend.Dto.CategoryDTO.CategoryDTO;
import com.backend.velyo_backend.Entity.Category;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import com.backend.velyo_backend.Util.ApiPageResponse;
import com.backend.velyo_backend.service.imp.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiPageResponse<List<CategoryDTO>>> getAllCategories(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "20") int size){
            log.debug("Recibida la solicitud para obetener todas las caregorias paginadas: {} y size: {}", page, size);
        Pageable pageable = PageRequest.of(page,size);
        Page<CategoryDTO> pageCategories = categoryService.findAll(pageable);
        List<CategoryDTO> categories = pageCategories.getContent();
        log.info("Devolviendo {} categorias", categories.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ApiPageResponse<>(
                                pageCategories.getTotalPages(),
                                (int) pageCategories.getTotalElements(),
                                categories,
                                "Categorias devueltas"
                        )
                );
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategoriesWithoutPaginatio(){
        log.debug("Recibida la solicitud para obtener todas las categorias");
        List<CategoryDTO> categories = categoryService.findAllWithoutPagination();
        log.info("Devolviendo {} categorias", categories.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categories);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CategoryDTO> createCategory(@RequestPart("category")CategoryDTO categoryDTO,
                                                      @RequestPart("image")MultipartFile image) throws IOException, IllegalArgumentException{
        log.debug("Recibida la solicitud para crear categoria: {}", categoryDTO);
        CategoryDTO savedCategory = categoryService.save(categoryDTO, image);
        log.info("Categoria creada: {}", savedCategory.getName());
        return  ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @PutMapping(consumes = {"multipart/form-data"})
    public  ResponseEntity<CategoryDTO> updateCategory(@RequestPart("category") CategoryDTO categoryDTO,
                                                       @RequestPart(value = "image", required = false) MultipartFile image) throws ResourceNotFoundException, IOException{
        log.debug("Recibida la solicitud para actualizar la categoria: {}", categoryDTO);
        CategoryDTO updatedCategory = categoryService.update(categoryDTO, image);
        log.info("Categoria actualizada: {}", updatedCategory.getName());
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws MalformedURLException {
        log.debug("Recibida la solicitud para obtener imagen: {}", imageName);
        Resource resource = categoryService.getImage(imageName);
        log.info("Devolviendo imagen: {}", imageName);

        // Determinar el tipo de contenido dinámicamente
        String contentType = "application/octet-stream"; // por defecto
        try {
            Path path = Paths.get("uploads/categories").resolve(imageName).normalize();
            contentType = Files.probeContentType(path);
        } catch (IOException ex) {
            log.warn("No se pudo determinar el tipo de contenido para: {}", imageName);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID id)
        throws ResourceNotFoundException, IOException{
        log.debug("Recibida la solicitud para borrar la categoria con id: {}", id);
        categoryService.delete(id);
        log.info("Categoria borrada con id: ", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
