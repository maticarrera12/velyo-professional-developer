package com.backend.velyo_backend.Repository;

import com.backend.velyo_backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);


    @Query("SELECT u FROM users u JOIN u.favorites f WHERE f.id IN :favoritesIds")
    Set<User> findByFavoritesId(Set<UUID> favoritesIds);
}
