package com.backend.velyo_backend.Dto.AccommodationDTO;


import com.backend.velyo_backend.Dto.AddressDTO.AddressDTO;
import com.backend.velyo_backend.Dto.AmenityDTO.AmenityDTO;
import com.backend.velyo_backend.Dto.ReviewDTO.ReviewSummaryDTO;
import com.backend.velyo_backend.Entity.AccommodationPolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccommodationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String description;
    private Set<String> images;
    private Set<AmenityDTO> amenities;
    private Double Price;
    private UUID category_id;
    private AddressDTO address;
    private Double avgRating;
    private Set<AccommodationPolicy> policies;
    private Set<LocalDate>  unavailableDates;
    private Set<ReviewSummaryDTO> reviews;
    private Integer totalReviews;

}
