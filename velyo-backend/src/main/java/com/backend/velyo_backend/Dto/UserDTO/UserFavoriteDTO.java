package com.backend.velyo_backend.Dto.UserDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFavoriteDTO {
    private UUID id;
    private UUID favorite;
}
