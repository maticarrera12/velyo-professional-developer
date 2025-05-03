package com.backend.velyo_backend.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "categories")
public class Category {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @NotNull(message = "El nombre no puede ser nulo")
    private String name;

    private String description;

    @NotNull(message = "La imagen no puede ser nula")
    private String image;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Accommodation> accommodations;

    @Override
    public int hashCode() {
        return Objects.hash(id); // Only use immutable fields
    }
}
