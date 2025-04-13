package com.backend.velyo_backend.Repository;

import com.backend.velyo_backend.Entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, UUID> {

    Optional<Accommodation> findByName(String name);

    Set<Accommodation> findByCategory_IdIn(Set<UUID> categoryIds);

//    @Query("""
//            SELECT a FROM Accommodations a
//            WHERE (:categoryIds IS NULL OR a.category.id IN :categoryIds)
//            AND (:searchTerm IS NULL OR LOWER(a.address.country) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
//            OR LOWER(a.address.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
//            AND (:checkIn IS NULL OR :checkOut IS NULL OR NOT EXISTS (
//            SELECT b FROM Bookings b
//            WHERE b.accommodation = a.id
//            AND b.checkIn <= :checkOut
//            AND b.checkOut >= :checkIn
//            ))
//            """)
//    Set<Accommodation> findByCategoryAndCountryOrCityContainingIgnoreCase(
//            @Param("categoryIds") Set<UUID> categoryIds,
//            @Param("searchTerm")String searchTerm,
//            @Param("checkIn")LocalDate checkIn,
//            @Param("checkOut") LocalDate checkOut
//            );
@Query("""
    SELECT a FROM accommodations a
    WHERE (:categoryIds IS NULL OR a.category.id IN :categoryIds)
      AND (:searchTerm IS NULL OR LOWER(a.address.country) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(a.address.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
      AND (:checkIn IS NULL OR :checkOut IS NULL OR NOT EXISTS (
        SELECT b FROM bookings b
        WHERE b.accommodation.id = a.id
          AND b.checkIn <= :checkOut
          AND b.checkOut >= :checkIn
      ))
""")
Set<Accommodation> findByCategoryAndCountryOrCityContainingIgnoreCase(
        @Param("categoryIds") Set<UUID> categoryIds,
        @Param("searchTerm") String searchTerm,
        @Param("checkIn") LocalDate checkIn,
        @Param("checkOut") LocalDate checkOut
);


    @Query("""
            SELECT a FROM accommodations a
            JOIN a.amenities am
            WHERE am.id IN :featureIds
            """)
    Set<Accommodation> findByAmenityIds(@Param("featureIds") Set<UUID> amenityIds);

}
