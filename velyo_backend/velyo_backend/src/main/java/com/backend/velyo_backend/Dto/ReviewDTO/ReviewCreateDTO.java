package com.backend.velyo_backend.Dto.ReviewDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewCreateDTO  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer rating;
    private String comment;
    private UUID id_accommodation;
    private UUID id_booking;
}
