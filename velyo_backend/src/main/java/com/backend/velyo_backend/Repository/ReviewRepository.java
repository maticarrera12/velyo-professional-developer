package com.backend.velyo_backend.Repository;

import com.backend.velyo_backend.Entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Set<Review> findByAccommodationId(UUID accommodationId);

    Page<Review> findByAccommodationId(UUID accommodationId, Pageable pageable);
}
