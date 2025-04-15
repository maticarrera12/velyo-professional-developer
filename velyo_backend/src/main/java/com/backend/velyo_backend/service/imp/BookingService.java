package com.backend.velyo_backend.service.imp;

import com.backend.velyo_backend.Dto.BookingDTO.BookingCreateDTO;
import com.backend.velyo_backend.Dto.BookingDTO.BookingDTO;
import com.backend.velyo_backend.Entity.Booking;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import com.backend.velyo_backend.Mapper.BookingMapper.BookingCreateMapper;
import com.backend.velyo_backend.Mapper.BookingMapper.BookingMapper;
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
import java.util.stream.Collectors;


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
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                bookingCreateDTO.getCheckIn(),
                bookingCreateDTO.getCheckOut(),
                bookingCreateDTO.getId_accommodation()
        );
        if (!overlappingBookings.isEmpty()){
            log.error("La reserva se solapa con una reserva existente");
            throw new IllegalArgumentException("El alojammiento ya esta reservado en las fechas seleccionadas");
        }

        Booking booking = BookingCreateMapper.INSTANCE.dtoToEntity(bookingCreateDTO);
        Double price = accommodationRepository.findById(booking.getAccommodation().getId()).orElseThrow(()->{
            log.error("El alojamiento con id: {} no fue encontrada", booking.getAccommodation().getId());
            return new ResourceNotFoundException("El alojamiento con id: " + booking.getAccommodation().getId() + " no fue encontrada");
        }).getPrice();
        int days = booking.getCheckOut().compareTo(booking.getCheckIn());
        if (days == 0) days = 1;
        booking.setTotal(price * days);
        booking.setConfirmed(false);
        booking.setReviewed(false);
        bookingRepository.save(booking);
        log.info("La reserva ha sido guardada: {}", booking.getId());
        return BookingCreateMapper.INSTANCE.entityToDto(booking);

    }

    @Override
    public BookingDTO findById(UUID id) {
        log.debug("Obteniendo la reserva con id: {}", id);
        Booking booking = bookingRepository.findById(id).orElseThrow(()->{
            log.error("La reser va con id: {} no fue encontrada", id);
            return new ResourceNotFoundException("La reserva con id: " + id + " no fue encontrada");
        });
        return BookingMapper.INSTANCE.entityToDto(booking);
    }

    @Override
    public Page<BookingDTO> findAll(Pageable pageable) {
        log.debug("Obteniendo todas las reservas");
        return bookingRepository.findAll(pageable).map(BookingMapper.INSTANCE::entityToDto);
    }

    @Override
    public BookingDTO delete(UUID id) throws ResourceNotFoundException {
        log.debug("Borrando reserva con id: {}", id);
        Booking bookingToDelete = bookingRepository.findById(id).orElseThrow(()->{
            log.error("La reserva con id: {} no fue encontrada", id);
            return new ResourceNotFoundException("La reserca con id: " + id + " no fue encontrada");
        });
        bookingRepository.delete(bookingToDelete);
        log.info("La reserva borrada: {}", id);
        return BookingMapper.INSTANCE.entityToDto(bookingToDelete);
    }

    @Override
    public List<BookingDTO> getReservationByUser(String email, LocalDate date) {
        log.debug("Obteniendo reservas por mail del usuario: {}", email);
        List<Booking> bookings;
        if (date != null){
            bookings = bookingRepository.findBookingByUserEmailAndDate(email, date);
        }else{
            bookings = bookingRepository.findBookingByUser_Email(email);
        }

        List<BookingDTO> bookingDTOS = bookings.stream().map(BookingMapper.INSTANCE::entityToDto).toList();
        bookingDTOS.forEach(bookingDTO -> {
            bookingDTO.getAccommodation().setImages(bookingDTO.getAccommodation().getImages().stream()
                    .map((image) -> getBaseUrl() + "/api/accommodations/images/" + image)
                    .collect(Collectors.toSet()));
        });
        return bookingDTOS;
    }

    @Override
    public BookingDTO confirmBooking(UUID id, String email) throws ResourceNotFoundException {
        log.debug("Confirmando reserva con id: {}", id);
        Booking booking = bookingRepository.findById(id).orElseThrow(()->{
            log.error("La reserva con id: {} no fue encontrada", id);
            return new ResourceNotFoundException("La reserva con el id " + id + " no se ha encontrado");
        });

        if (!booking.getUser().getEmail().equals(email)){
            log.error("El usuario con email: {} no es propietario de la reserva con id: {}", email, id);
            throw new ResourceNotFoundException("La reserva con el id: " + id + " no se ha encontrado");
        }
        if (booking.getConfirmed()){
            log.error("La reserva con id: {} ya esta confirmada", id);
            throw new IllegalArgumentException("La reserva ya esta confirmada");
        }
        booking.setConfirmed(true);
        bookingRepository.save(booking);
        log.info("Reserca confirmada: {}", id);
        try {
            emailService.sendConfirmationEmail(booking.getUser().getFirstname(), booking.getUser().getLastname(), booking.getUser().getEmail());
        }catch (Exception e){
            log.error("Error enviando el mail: {}", e.getMessage());
        }
        return BookingMapper.INSTANCE.entityToDto(booking);
    }
}
