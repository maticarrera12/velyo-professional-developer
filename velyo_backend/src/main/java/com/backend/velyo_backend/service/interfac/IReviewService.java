package com.backend.velyo_backend.service.interfac;

import com.backend.velyo_backend.Dto.ReviewDTO.ReviewCreateDTO;
import com.backend.velyo_backend.Dto.ReviewDTO.ReviewSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IReviewService {

    Page<ReviewSummaryDTO> findAll(Pageable pageable);

    Page<ReviewSummaryDTO> findByStay(Pageable pageable, UUID id);

    ReviewSummaryDTO save(ReviewCreateDTO reviewCreateDTO, String email);
}
