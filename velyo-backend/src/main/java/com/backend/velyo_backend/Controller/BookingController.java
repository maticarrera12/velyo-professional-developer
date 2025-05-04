package com.backend.velyo_backend.Controller;


import com.backend.velyo_backend.Dto.BookingDTO.BookingCreateDTO;
import com.backend.velyo_backend.Dto.BookingDTO.BookingDTO;
import com.backend.velyo_backend.Repository.BookingRepository;
import com.backend.velyo_backend.Util.ApiPageResponse;
import com.backend.velyo_backend.service.imp.BookingService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    public BookingController(BookingService bookingService, BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<BookingCreateDTO> createBooking(@RequestBody BookingCreateDTO bookingCreateDTO){
        log.debug("Solicitud recibida para crear la reserva");
        BookingCreateDTO savedBooking = bookingService.save(bookingCreateDTO);
        log.info("La reserva fue creada: {}", savedBooking);
        return  ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }

    @GetMapping
    public ResponseEntity<ApiPageResponse<List<BookingDTO>>> getAllReservations(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        log.debug("Recibida la solicitud para obtener todas las reservas");
        Pageable pageable = PageRequest.of(page, size);
        Page<BookingDTO> pageBookings = bookingService.findAll(pageable);
        List<BookingDTO> bookings = pageBookings.getContent();
        log.info("Devolviendo {} reservas", bookings.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ApiPageResponse<>(
                                pageBookings.getTotalPages(),
                                (int) pageBookings.getTotalElements(),
                                bookings,
                                "Reserva devuelta"
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable("id")UUID id){
        log.debug("Recibida la solicitud para obtener una reserva con id: {}", id);
        BookingDTO booking = bookingService.findById(id);
        log.info("Devolviendo la reserca con id: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(booking);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") UUID id){
        log.debug("Recibida la solicitud para borrar la reserva con id: {}", id);
        bookingService.delete(id);
        log.info("La reserva con id: {} fue borrada", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public ResponseEntity<List<BookingDTO>> getReservationsByToken(@RequestParam(value = "date", required = false)LocalDate date){
        log.debug("Recibida la solicitud para obtener reservas por token");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<BookingDTO> bookings = bookingService.getReservationByUser(userDetails.getUsername(), date);
        return ResponseEntity.status(HttpStatus.OK).body(bookings);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/confirm/{id}")
    public ResponseEntity<BookingDTO> confirmBooking(@PathVariable("id") UUID id){
        log.debug("Recibida la solicitud para confirmar la reserva con id: {}", id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        BookingDTO booking = bookingService.confirmBooking(id, userDetails.getUsername());
        log.info("La reserva con id: {} se confirmo", id);
        return ResponseEntity.status(HttpStatus.OK).body(booking);
    }
}
