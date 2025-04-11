package com.backend.velyo_backend.Mapper.BookingMapper;

import com.backend.velyo_backend.Dto.BookingDTO.BookingCreateDTO;
import com.backend.velyo_backend.Entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingCreateMapper {

    BookingCreateMapper INSTANCE = Mappers.getMapper(BookingCreateMapper.class);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "accommodation.id", target = "id_accommodation")
    @Mapping(source = "user.id", target = "id_user")
    BookingCreateDTO entityToDto(Booking booking);

    @Mapping(source = "id_accommodation", target = "accommodation.id")
    @Mapping(source = "id_user", target = "user.id")
    Booking dtoToEntity(BookingCreateDTO bookingDTO);
}
