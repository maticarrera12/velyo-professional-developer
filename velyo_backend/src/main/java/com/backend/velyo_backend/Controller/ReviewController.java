package com.backend.velyo_backend.Controller;

import com.backend.velyo_backend.Dto.ReviewDTO.ReviewCreateDTO;
import com.backend.velyo_backend.Dto.ReviewDTO.ReviewSummaryDTO;
import com.backend.velyo_backend.Util.ApiPageResponse;
import com.backend.velyo_backend.service.imp.ReviewService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;


    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<String> createReview(@RequestBody ReviewCreateDTO reviewCreateDTO) {
        log.debug("Recibida la solicitud para crear review");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        reviewService.save(reviewCreateDTO, userDetails.getUsername());
        log.info("Review creada");
        return ResponseEntity.status(HttpStatus.CREATED).body("La reseña ha sido creada");
    }

    @GetMapping
    public ResponseEntity<ApiPageResponse<List<ReviewSummaryDTO>>> getAllReviews(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.debug("Recibida la solicitus para obtener todas las reviews");
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewSummaryDTO> pageReviews = reviewService.findAll(pageable);
        log.info("Reviews encontradas: {}", pageReviews);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiPageResponse<>(
                        pageReviews.getTotalPages(),
                        (int) pageReviews.getTotalElements(),
                        pageReviews.getContent(),
                        "Reviews devueltas"
                )
        );
    }
}
