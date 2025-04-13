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
@Entity(name="addresses")
public class Address {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    @NotNull
    private String country;
    @NotNull
    private String city;
    @NotNull
    private String street;


}
