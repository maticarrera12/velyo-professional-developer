package com.backend.velyo_backend.Controller;


import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationDTO;
import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSaveDTO;
import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSummaryDTO;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import com.backend.velyo_backend.Util.ApiPageResponse;
import com.backend.velyo_backend.service.imp.AccommodationService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;


    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @GetMapping
    public ResponseEntity<ApiPageResponse<List<AccommodationDTO>>> getAllAccommodations(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        log.debug("Recibido la solicitud para obtener todos los alojamientos paginados: {} and size: {}", page,size);
        Pageable pageable = PageRequest.of(page, size);
        Page<AccommodationDTO> pageAccommodations = accommodationService.findAll(pageable);
        List<AccommodationDTO> accommodationDTOS = pageAccommodations.getContent();
        log.debug("Devolviendo {} alojamientos", accommodationDTOS.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ApiPageResponse<>(
                                pageAccommodations.getTotalPages(),
                                (int) pageAccommodations.getTotalElements(),
                                accommodationDTOS,
                                "Alojamientos devueltos"
                        )
                );
    }

    @GetMapping("/search")
    public  ResponseEntity<ApiPageResponse<Set<AccommodationSummaryDTO>>> getAccommodationsByCategoryAndCountryOrCity(
            @RequestParam(value = "categoryIds", required = false) Set<UUID> categoryIds,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "checkIn", required = false) LocalDate checkIn,
            @RequestParam(value = "checkOut", required = false) LocalDate checkOut
    ){
        log.debug("Recibida la solicitud para obtener todos los alojamientos por categoria id: {}", categoryIds);
        Set<AccommodationSummaryDTO> accommodations = accommodationService.findByCategoryIdsAndCountryOrCity(categoryIds, searchTerm, checkIn, checkOut);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiPageResponse<>(
                                1,
                                accommodations.size(),
                                accommodations,
                                "Alojamientos devueltos"
                        )
                );
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public  ResponseEntity<AccommodationSaveDTO> createAccommodation(@RequestPart("accommodation") AccommodationSaveDTO accommodationDTO,
                                                                     @RequestPart("images") MultipartFile[] images) throws IOException,IllegalArgumentException{
        log.debug("Recibida la solicitud para crear el alojamiento: {}", accommodationDTO);
        log.debug(accommodationDTO);
        AccommodationSaveDTO savedAccommodation = accommodationService.save(accommodationDTO, images);
        log.debug("Alojamiento creado: {}", savedAccommodation.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccommodation);
    }

    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> updateAccommodation(@RequestPart("accommodation") AccommodationSaveDTO accommodationDTO,
                                                      @RequestPart(value = "images", required = false) MultipartFile[] images,
                                                      @RequestPart(value = "imagesToDelete", required = false) Set<String> imagesToDelete)
        throws ResourceNotFoundException, IOException{
        log.debug("Recibida la solicitud para actualizar el alojamiento: {}", accommodationDTO.getId());
        accommodationService.update(accommodationDTO, images, imagesToDelete);
        return ResponseEntity.status(HttpStatus.OK)
                .body("El alojamiento ha sido actualizado correctamente");
    }

    @GetMapping("/images/{imageName}")
    public  ResponseEntity<Resource> getImage(@PathVariable String imageName) throws MalformedURLException{
        log.debug("Recibida la solicitud para obtener imagen con nombre: {}", imageName);
        Resource resource = accommodationService.getImage(imageName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/random")
    public ResponseEntity<Set<AccommodationSummaryDTO>> getRandomAccommodations(@RequestParam(value = "size", defaultValue = "10") int size){
        log.debug("Recibida la solicitud para obtener alojamientos random con size: {}", size);
        Set<AccommodationSummaryDTO> randomAccommodations = accommodationService.getRandomAccommodations(size);
        log.debug("Devolviendo {} alojamientos random", randomAccommodations.size());
        return ResponseEntity.status(HttpStatus.OK).body(randomAccommodations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccommodation(@PathVariable UUID id) throws ResourceNotFoundException, IOException{
        log.debug("Recibida la solicitud para borrar el alojamiento con id: {}", id);
        accommodationService.delete(id);
        log.debug("Alojamiento borrado con id: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccommodationDTO> getAccommodation(@PathVariable UUID id) throws ResourceNotFoundException{
        log.debug("Recibida la solicitud para obtener el alojamiento por id: {}", id);
        AccommodationDTO accommodationFound = accommodationService.findById(id);
        log.debug("Alojamiento devuelto: {}", accommodationFound);
        return ResponseEntity.status(HttpStatus.OK).body(accommodationFound);
    }
}
