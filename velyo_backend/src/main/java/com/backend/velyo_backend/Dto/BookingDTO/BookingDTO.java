package com.backend.velyo_backend.Dto.BookingDTO;

import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSummaryDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Double total;
    private AccommodationSummaryDTO accommodation;
    private UserSummaryDTO user;
    private Boolean confirmed;
    private Boolean reviewed;
}
