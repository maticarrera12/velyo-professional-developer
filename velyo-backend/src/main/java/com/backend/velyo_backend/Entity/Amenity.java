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
@Entity(name = "amenities")
public class Amenity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @NotNull(message = "El nombre no puede ser nulo")
    private String name;
    @NotNull(message = "El icono no puede ser nulo")
    private String icon;
}
