package com.backend.velyo_backend.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @NotNull(message = "El checkIn no puede ser nulo")
    private LocalDate checkIn;
    @NotNull(message = "El checkOut no puede ser nulo")
    private LocalDate checkOut;

    @NotNull(message = "El total no puede ser nulo")
    private Double total;
    private Boolean reviewed = false;
    private Boolean confirmed = false;

    @OneToOne
    private Review review;

    @NotNull(message = "El alojamiento no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "id_accommodation", foreignKey = @ForeignKey(name = "FK_id_accommodation"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Accommodation accommodation;

    @NotNull(message = "El usuario no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

}
