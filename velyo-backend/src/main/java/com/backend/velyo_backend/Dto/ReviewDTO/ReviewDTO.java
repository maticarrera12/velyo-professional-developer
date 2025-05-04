package com.backend.velyo_backend.Dto.ReviewDTO;


import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Integer rating;
    private String comment;
    private LocalDate reviewDate;
    private UserDTO user;
    private AccommodationDTO accommodation;

}
