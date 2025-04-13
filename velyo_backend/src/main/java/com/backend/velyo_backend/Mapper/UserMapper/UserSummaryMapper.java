package com.backend.velyo_backend.Mapper.UserMapper;


import com.backend.velyo_backend.Dto.UserDTO.UserSummaryDTO;
import com.backend.velyo_backend.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserSummaryMapper {

    UserSummaryMapper INSTANCE = Mappers.getMapper(UserSummaryMapper.class);

    UserSummaryDTO entityToDto(User user);
}
