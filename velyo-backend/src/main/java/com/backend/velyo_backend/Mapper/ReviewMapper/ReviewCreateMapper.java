package com.backend.velyo_backend.Mapper.ReviewMapper;

import com.backend.velyo_backend.Dto.ReviewDTO.ReviewCreateDTO;
import com.backend.velyo_backend.Entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewCreateMapper {

    ReviewCreateMapper INSTANCE = Mappers.getMapper(ReviewCreateMapper.class);

    @Mapping(source = "id_accommodation", target = "accommodation.id")
    Review dtoToEntity(ReviewCreateDTO reviewCreateDTO);

}
