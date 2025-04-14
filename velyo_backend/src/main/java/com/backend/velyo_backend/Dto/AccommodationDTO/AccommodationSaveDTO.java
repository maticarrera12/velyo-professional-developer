package com.backend.velyo_backend.Dto.AccommodationDTO;

import com.backend.velyo_backend.Dto.AddressDTO.AddressDTO;
import com.backend.velyo_backend.Entity.AccommodationPolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationSaveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String description;
    private Set<String> images;
    private Double price;
    private AddressDTO address;
    private UUID category_id;
    private Set<UUID> amenities;
    private Set<AccommodationPolicy> policies;
}
