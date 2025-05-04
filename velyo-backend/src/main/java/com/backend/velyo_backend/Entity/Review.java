package com.backend.velyo_backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @NotNull
    @Positive
    private Integer rating;

    private String comment;

    @NotNull
    private LocalDateTime reviewDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_accommodation", nullable = false, foreignKey = @ForeignKey(name = "FK_id_accommodation"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Accommodation accommodation;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Add proper equals() implementation
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id);
    }
}
