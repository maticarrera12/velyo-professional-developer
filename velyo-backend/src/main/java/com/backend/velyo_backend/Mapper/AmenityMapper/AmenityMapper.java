package com.backend.velyo_backend.Mapper.AmenityMapper;

import com.backend.velyo_backend.Dto.AmenityDTO.AmenityDTO;
import com.backend.velyo_backend.Entity.Amenity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AmenityMapper {
    AmenityMapper INSTANCE = Mappers.getMapper(AmenityMapper.class);
    AmenityDTO entityToDto(Amenity amenity);
    Amenity dtoToEntity(AmenityDTO amenityDTO);
}
