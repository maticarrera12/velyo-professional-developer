package com.backend.velyo_backend.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "accommodation_policies")
public class AccommodationPolicy {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @NotNull(message = "El nombre no puede ser nulo")
    private String policy;

    @NotNull(message = "La descripcion no puede ser nula")
    private String description;
}
