package com.backend.velyo_backend.service.interfac;

import com.backend.velyo_backend.Dto.AmenityDTO.AmenityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IAmenityService {

    AmenityDTO save(AmenityDTO amenityDTO, MultipartFile icon) throws IOException;

    AmenityDTO findById(UUID id);

    AmenityDTO findByName(String name);

    Page<AmenityDTO> findAll(Pageable pageable);

    List<AmenityDTO> findAllWithoutPagination();

    AmenityDTO update(AmenityDTO amenityDTO, MultipartFile icon) throws IOException;

    void delete(UUID id) throws IOException;
}
