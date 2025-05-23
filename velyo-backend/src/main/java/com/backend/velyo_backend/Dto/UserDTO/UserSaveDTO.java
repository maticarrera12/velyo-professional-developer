package com.backend.velyo_backend.Dto.UserDTO;

import com.backend.velyo_backend.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSaveDTO {
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    private String password;
}
