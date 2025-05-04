package com.backend.velyo_backend.Dto.AccommodationDTO;

import com.backend.velyo_backend.Dto.AddressDTO.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationSummaryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private Double price;
    private Set<String> images;
    private AddressDTO address;
    private Double avgRating;
    private Integer totalReviews;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccommodationSummaryDTO that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

