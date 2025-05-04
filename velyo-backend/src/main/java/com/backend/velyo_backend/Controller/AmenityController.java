package com.backend.velyo_backend.Controller;

import com.backend.velyo_backend.Dto.AmenityDTO.AmenityDTO;
import com.backend.velyo_backend.Util.ApiPageResponse;
import com.backend.velyo_backend.service.imp.AmenityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;


    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<AmenityDTO> createAmenity(@RequestPart("amenity") AmenityDTO amenityDTO,
                                                    @RequestPart("icon")MultipartFile icon) throws IOException{
        log.debug("Recibida la solicitud para crear amenity: {}", amenityDTO.getName());
        AmenityDTO savedAmenity = amenityService.save(amenityDTO, icon);
        log.debug("Amenity creada: {}", savedAmenity.getName());
        return  ResponseEntity.status(HttpStatus.CREATED).body(savedAmenity);
    }

    @GetMapping
    public ResponseEntity<ApiPageResponse<List<AmenityDTO>>> getAllamenities(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                            @RequestParam(value = "size", defaultValue = "10") int size){
        log.debug("Recibida la solicitud para obtener todas las amenities");
        Pageable pageable = PageRequest.of(page,size);
        Page<AmenityDTO> pageAmenities = amenityService.findAll(pageable);
        List<AmenityDTO> amenities = pageAmenities.getContent();
        log.debug("Devolviendo {} amenities", amenities.size());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiPageResponse<>(
                        pageAmenities.getTotalPages(),
                        (int) pageAmenities.getTotalElements(),
                        amenities,
                        "Amenities devueltas"
                ));
    }

    @GetMapping("/all")
    public  ResponseEntity<List<AmenityDTO>> getAllAmenitieswithoutPagination(){
        log.debug("Recibida la solicitud para obtener todas las amenities sin paginacion");
        List<AmenityDTO> amenities = amenityService.findAllWithoutPagination();
        log.info("Devolvieendo {} amenities", amenities.size());
        return ResponseEntity.status(HttpStatus.OK).body(amenities);
    }

    @GetMapping("/{name}")
    public  ResponseEntity<AmenityDTO> getAmenityByName(@PathVariable String name){
        log.debug("Recibida la solicitud para obtener amenity por nombre: {}", name);
        AmenityDTO amenityFound = amenityService.findByName(name);
        log.debug("Amenity devuelta: {}", amenityFound.getName());
        return ResponseEntity.status(HttpStatus.OK).body(amenityFound);
    }

    @GetMapping("/svg/{iconName}")
    public ResponseEntity<Resource> getAmenityIcon(@PathVariable String iconName) throws MalformedURLException{
        log.debug("Recibida la solicitud para obtener el icono por nombre: {}", iconName);
        Resource resource = amenityService.getIcon(iconName);
        log.debug("Icon de la amenity devuelto: {}", iconName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/svg+xml"))
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<AmenityDTO> updateAmenity(@RequestPart("amenity") AmenityDTO amenityDTO,
                                                    @RequestPart(value = "icon", required = false) MultipartFile icon) throws IOException{
        log.debug("Recibida la solicitud para actualizar amenity: {}", amenityDTO.getId());
        AmenityDTO  updatedAmenity = amenityService.update(amenityDTO, icon);
        log.debug("Amenity actualizada: {}", updatedAmenity.getName());
        return ResponseEntity.status(HttpStatus.OK).body(updatedAmenity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable UUID id) throws IOException{
        log.debug("Recibida la solicitud para borrar amenity con id: {}", id);
        amenityService.delete(id);
        log.debug("Amenity borrada: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
