package com.backend.velyo_backend.Dto.UserDTO;


import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSummaryDTO;
import com.backend.velyo_backend.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    private Set<AccommodationSummaryDTO> favorites;
}
