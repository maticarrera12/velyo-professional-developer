package com.backend.velyo_backend.service.interfac;

import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationDTO;
import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSaveDTO;
import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSummaryDTO;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public interface IAccommodationService {

    AccommodationSaveDTO save(AccommodationSaveDTO accommodationDTO, MultipartFile[] images) throws IOException;

    AccommodationDTO findById(UUID id);

    AccommodationDTO findByName(String name);

    Page<AccommodationDTO> findAll(Pageable pageable);

    Set<AccommodationSummaryDTO> getRandoStays(int size);

    void update(AccommodationSaveDTO accommodationDTO, MultipartFile[] images, Set<String> imagesToDelete) throws IOException;

    void delete(UUID id) throws ResourceNotFoundException, IOException;

    Set<AccommodationSummaryDTO> findByCategoryAndCountryOrCity(Set<UUID> categoryIds, String searchTerm, LocalDate checkIn, LocalDate checkOut);
}
