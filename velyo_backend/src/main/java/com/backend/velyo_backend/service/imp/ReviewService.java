package com.backend.velyo_backend.service.imp;


import com.backend.velyo_backend.Dto.ReviewDTO.ReviewCreateDTO;
import com.backend.velyo_backend.Dto.ReviewDTO.ReviewSummaryDTO;
import com.backend.velyo_backend.Repository.AccommodationRepository;
import com.backend.velyo_backend.Repository.BookingRepository;
import com.backend.velyo_backend.Repository.ReviewRepository;
import com.backend.velyo_backend.Repository.UserRepository;
import com.backend.velyo_backend.service.interfac.IReviewService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@Service
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public ReviewService(ReviewRepository reviewRepository, AccommodationRepository accommodationRepository, UserRepository userRepository, BookingRepository bookingRepository) {
        this.reviewRepository = reviewRepository;
        this.accommodationRepository = accommodationRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }


    @Override
    public Page<ReviewSummaryDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReviewSummaryDTO> findByStay(Pageable pageable, UUID id) {
        return null;
    }

    @Override
    public ReviewSummaryDTO save(ReviewCreateDTO reviewCreateDTO, String email) {
        return null;
    }
}
