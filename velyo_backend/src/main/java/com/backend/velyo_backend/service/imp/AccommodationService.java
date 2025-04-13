package com.backend.velyo_backend.service.imp;

import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationDTO;
import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSaveDTO;
import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSummaryDTO;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import com.backend.velyo_backend.Repository.*;
import com.backend.velyo_backend.Util.BaseUrl;
import com.backend.velyo_backend.service.interfac.IAccommodationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;


@Log4j2
@Service
public class AccommodationService implements IAccommodationService, BaseUrl {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationImageRepository accommodationImageRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final AmenityRepository amenityRepository;
    private final UserRepository userRepository;

    public AccommodationService(AccommodationRepository accommodationRepository, AccommodationImageRepository accommodationImageRepository, BookingRepository bookingRepository, ReviewRepository reviewRepository, AmenityRepository amenityRepository, UserRepository userRepository) {
        this.accommodationRepository = accommodationRepository;
        this.accommodationImageRepository = accommodationImageRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.amenityRepository = amenityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AccommodationSaveDTO save(AccommodationSaveDTO accommodationDTO, MultipartFile[] images) throws IOException {
        return null;
    }

    @Override
    public AccommodationDTO findById(UUID id) {
        return null;
    }

    @Override
    public AccommodationDTO findByName(String name) {
        return null;
    }

    @Override
    public Page<AccommodationDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Set<AccommodationSummaryDTO> getRandoStays(int size) {
        return Set.of();
    }

    @Override
    public void update(AccommodationSaveDTO accommodationDTO, MultipartFile[] images, Set<String> imagesToDelete) throws IOException {

    }

    @Override
    public void delete(UUID id) throws ResourceNotFoundException, IOException {

    }

    @Override
    public Set<AccommodationSummaryDTO> findByCategoryAndCountryOrCity(Set<UUID> categoryIds, String searchTerm, LocalDate checkIn, LocalDate checkOut) {
        return Set.of();
    }
}
