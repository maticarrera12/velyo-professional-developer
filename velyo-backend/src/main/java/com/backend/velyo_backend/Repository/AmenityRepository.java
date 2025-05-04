package com.backend.velyo_backend.Repository;


import com.backend.velyo_backend.Entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, UUID> {
    Optional<Amenity> findByName(String name);
}
