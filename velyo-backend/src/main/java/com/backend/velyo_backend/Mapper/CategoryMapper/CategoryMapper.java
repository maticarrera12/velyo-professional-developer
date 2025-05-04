package com.backend.velyo_backend.Mapper.CategoryMapper;


import com.backend.velyo_backend.Dto.CategoryDTO.CategoryDTO;
import com.backend.velyo_backend.Entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO entityToDto(Category category);

    Category dtoToEntity(CategoryDTO categoryDTO);
}
