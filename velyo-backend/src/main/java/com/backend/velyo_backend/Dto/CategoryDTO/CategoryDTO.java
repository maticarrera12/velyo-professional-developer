package com.backend.velyo_backend.Dto.CategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO implements Serializable {

    @Serial
    private static  final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String description;
    private String image;
}
