package com.backend.velyo_backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "accommodations")
public class Accommodation {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @NotNull(message = "El nombre no puede ser nulo")
    private String name;

    private String description;

    @NotNull(message = "Las imagenes no pueden ser nulas")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccommodationImage> images;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(
            name="accommodation_amenity",
            joinColumns = @JoinColumn(name="accommodation_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities;

    @NotNull(message = "El precio no puede ser nulo")
    @PositiveOrZero(message = "El precio debe ser 0 o mayor")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "id_category", foreignKey = @ForeignKey(name = "FK_id_category"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @NotNull(message = "La direccion no puede ser nula")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_address")
    private Address address;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    @PositiveOrZero(message = "El rating debe ser 0 o positivo")
    private Double avgRating = 0.0;

    @NotNull(message = "Policies cannot be null")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccommodationPolicy> policies;

    @Override
    public int hashCode() {
        return Objects.hash(id); // Only use immutable fields
    }
}
