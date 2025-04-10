package com.backend.velyo_backend.Dto.BookingDTO;


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
public class BookingCreateDTO  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID id_accommodation;
    private UUID id_user;
    private LocalDate checkIn;
    private LocalDate checkOut;
}
