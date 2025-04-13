package com.backend.velyo_backend.Dto.UserDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateNameDTO {
    private String firstName;
    private String lastName;
}
