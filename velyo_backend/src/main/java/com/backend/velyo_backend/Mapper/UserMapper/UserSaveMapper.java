package com.backend.velyo_backend.Mapper.UserMapper;

import com.backend.velyo_backend.Dto.UserDTO.UserSaveDTO;
import com.backend.velyo_backend.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserSaveMapper {
    UserSaveMapper INSTANCE = Mappers.getMapper(UserSaveMapper.class);

    UserSaveDTO entityToDto(User user);

    User dtoToEntity(UserSaveDTO userSaveDTO);
}
