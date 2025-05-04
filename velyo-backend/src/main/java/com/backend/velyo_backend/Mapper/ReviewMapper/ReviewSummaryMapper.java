package com.backend.velyo_backend.Mapper.ReviewMapper;


import com.backend.velyo_backend.Dto.ReviewDTO.ReviewSummaryDTO;
import com.backend.velyo_backend.Entity.Review;
import com.backend.velyo_backend.Mapper.UserMapper.UserSummaryMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserSummaryMapper.class})
public interface ReviewSummaryMapper {
    ReviewSummaryMapper INSTANCE = Mappers.getMapper(ReviewSummaryMapper.class);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "comment", target = "comment")
    ReviewSummaryDTO entityToDto(Review review);

    @Mapping(source = "user", target = "user")
    Review dtoToEntity(ReviewSummaryDTO reviewDTO);
}
