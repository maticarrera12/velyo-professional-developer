package com.backend.velyo_backend.service.imp;


import com.backend.velyo_backend.Dto.ReviewDTO.ReviewCreateDTO;
import com.backend.velyo_backend.Dto.ReviewDTO.ReviewSummaryDTO;
import com.backend.velyo_backend.Entity.Accommodation;
import com.backend.velyo_backend.Entity.Booking;
import com.backend.velyo_backend.Entity.Review;
import com.backend.velyo_backend.Entity.User;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import com.backend.velyo_backend.Mapper.ReviewMapper.ReviewCreateMapper;
import com.backend.velyo_backend.Mapper.ReviewMapper.ReviewSummaryMapper;
import com.backend.velyo_backend.Repository.AccommodationRepository;
import com.backend.velyo_backend.Repository.BookingRepository;
import com.backend.velyo_backend.Repository.ReviewRepository;
import com.backend.velyo_backend.Repository.UserRepository;
import com.backend.velyo_backend.service.interfac.IReviewService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        log.debug("Solicitud para obtener todas las reviews");
        Page<ReviewSummaryDTO> reviews = reviewRepository.findAll(pageable).map(ReviewSummaryMapper.INSTANCE::entityToDto);
        log.info("Reviews encontradas: {}", reviews);
        return reviews;
    }

    @Override
    public Page<ReviewSummaryDTO> findByAccommodation(Pageable pageable, UUID id) {
        log.debug("Solicitud para obtener todas las reviews por id del alojamiento: {}", id);
        Page<ReviewSummaryDTO> reviews = reviewRepository.findByAccommodationId(id, pageable).map(ReviewSummaryMapper.INSTANCE::entityToDto);
        log.info("Reviews encontradas: {}", reviews);
        return reviews;
    }
    @Override
    @Transactional
    public ReviewSummaryDTO save(ReviewCreateDTO reviewCreateDTO, String email) {
        log.debug("Request to save Review: {}", reviewCreateDTO);

        // 1. Fetch required entities
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User with email: {} not found", email);
            return new ResourceNotFoundException("User with email: " + email + " not found");
        });

        Accommodation accommodation = accommodationRepository.findById(reviewCreateDTO.getId_accommodation())
                .orElseThrow(() -> {
                    log.error("Accommodation with id: {} not found", reviewCreateDTO.getId_accommodation());
                    return new ResourceNotFoundException("Accommodation with id: " + reviewCreateDTO.getId_accommodation() + " not found");
                });

        Booking booking = bookingRepository.findById(reviewCreateDTO.getId_booking())
                .orElseThrow(() -> {
                    log.error("Booking with id: {} not found", reviewCreateDTO.getId_booking());
                    return new ResourceNotFoundException("Booking with id: " + reviewCreateDTO.getId_booking() + " not found");
                });

        // 2. Create and save review
        Review review = Review.builder()
                .rating(reviewCreateDTO.getRating())
                .comment(reviewCreateDTO.getComment())
                .reviewDate(LocalDateTime.now())
                .user(user)
                .accommodation(accommodation)
                .build();

        Review savedReview = reviewRepository.save(review);
        log.info("Review saved: {}", savedReview);

        // 3. Update booking
        booking.setReview(savedReview);
        booking.setReviewed(true);
        bookingRepository.save(booking);
        log.info("Booking updated: {}", booking);

        // 4. Calculate and update average rating
        Double average = reviewRepository.findByAccommodationId(accommodation.getId())
                .stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        accommodation.setAvgRating(average);
        accommodationRepository.save(accommodation);
        log.info("Accommodation updated with average rating: {}", average);

        // 5. Return DTO
        return ReviewSummaryMapper.INSTANCE.entityToDto(savedReview);
    }

}
