package com.backend.velyo_backend.service.interfac;

import com.backend.velyo_backend.Dto.BookingDTO.BookingCreateDTO;
import com.backend.velyo_backend.Dto.BookingDTO.BookingDTO;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IBookingService {

    BookingCreateDTO save(BookingCreateDTO bookingCreateDTO);

    BookingDTO findById(UUID id);

    Page<BookingDTO> findAll(Pageable pageable);

    BookingDTO delete(UUID id) throws ResourceNotFoundException;

    List<BookingDTO> getReservationByUser(String email, LocalDate date);

    BookingDTO confirmBooking(UUID id, String email) throws ResourceNotFoundException;
}
