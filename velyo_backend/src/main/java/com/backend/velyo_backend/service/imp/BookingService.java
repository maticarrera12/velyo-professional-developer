package com.backend.velyo_backend.service.imp;

import com.backend.velyo_backend.Dto.BookingDTO.BookingCreateDTO;
import com.backend.velyo_backend.Dto.BookingDTO.BookingDTO;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import com.backend.velyo_backend.Repository.AccommodationRepository;
import com.backend.velyo_backend.Repository.BookingRepository;
import com.backend.velyo_backend.Util.BaseUrl;
import com.backend.velyo_backend.service.EmailService;
import com.backend.velyo_backend.service.interfac.IBookingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Log4j2
@Service
public class BookingService implements IBookingService, BaseUrl {

    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final EmailService emailService;

    public BookingService(BookingRepository bookingRepository, AccommodationRepository accommodationRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.accommodationRepository = accommodationRepository;
        this.emailService = emailService;
    }

    @Override
    public BookingCreateDTO save(BookingCreateDTO bookingCreateDTO) {
        return null;
    }

    @Override
    public BookingDTO findById(UUID id) {
        return null;
    }

    @Override
    public Page<BookingDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public BookingDTO delete(UUID id) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public List<BookingDTO> getReservationByUser(String email, LocalDate date) {
        return List.of();
    }

    @Override
    public BookingDTO confirmReservation(UUID id, String email) throws ResourceNotFoundException {
        return null;
    }
}
