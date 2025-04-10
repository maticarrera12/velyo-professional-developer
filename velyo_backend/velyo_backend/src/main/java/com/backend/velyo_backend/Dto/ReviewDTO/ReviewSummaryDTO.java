package com.backend.velyo_backend.Dto.ReviewDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewSummaryDTO {

    private UUID id;
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;

}
