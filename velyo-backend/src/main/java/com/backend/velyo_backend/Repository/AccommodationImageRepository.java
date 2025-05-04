package com.backend.velyo_backend.Repository;


import com.backend.velyo_backend.Entity.AccommodationImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AccommodationImageRepository extends JpaRepository<AccommodationImage, UUID> {

    @Modifying
    @Transactional
    @Query("DELETE FROM accommodation_images WHERE url IN :imageUrls")
    void deleteByUrls(@Param("imageUrls")Set<String> imagesUrls);

    Optional<AccommodationImage> findByUrl(String url);
}
