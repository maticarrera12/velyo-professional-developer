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
    public ReviewSummaryDTO save(ReviewCreateDTO reviewCreateDTO, String email) {
        log.debug("Solicitud para guardar Review: {}", reviewCreateDTO);

        User user = userRepository.findByEmail(email).orElseThrow(()->{
            log.error("El usuario con email: {} no fue encontrado ", email);
            return new ResourceNotFoundException("El usuario con email " +  email + " no fue encontrado");
        });

        Accommodation accommodation = accommodationRepository.findById(reviewCreateDTO.getId_accommodation()).orElseThrow(()->{
            log.error("El alojamiento con id: {} no fue encontrado", reviewCreateDTO.getId_accommodation());
            return new ResourceNotFoundException("El alojamiento con id: " + reviewCreateDTO.getId_accommodation() + " no fue encontrado");
        });

        Booking booking = bookingRepository.findById(reviewCreateDTO.getId_booking()).orElseThrow(()->{
            log.error("La reserva con id: {} no fue encontrada", reviewCreateDTO.getId_booking());
            return new ResourceNotFoundException("La reserva con id: " + reviewCreateDTO.getId_booking() + " no fue encontrada");
        });

        Review review = ReviewCreateMapper.INSTANCE.dtoToEntity(reviewCreateDTO);
        review.setUser(user);
        review.setReviewDate(LocalDateTime.now());
        reviewRepository.save(review);
        log.info("La review fue guardada: {}", review);

        booking.setReview(review);
        booking.setReviewed(true);
        bookingRepository.save(booking);
        log.info("La reserva fue actualizada: {}", booking);

        Double average = reviewRepository.findByAccommodationId(reviewCreateDTO.getId_accommodation())
                .stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        log.info("Rating promedio: {}", average);
        accommodation.setAvgRating(average);
        accommodationRepository.save(accommodation);

        log.info("Alojamiento actualizado: {}", accommodation);
        return ReviewSummaryMapper.INSTANCE.entityToDto(review);
    }
}
