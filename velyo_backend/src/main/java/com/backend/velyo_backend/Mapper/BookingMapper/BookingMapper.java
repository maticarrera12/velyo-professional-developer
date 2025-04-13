package com.backend.velyo_backend.Mapper.BookingMapper;

import com.backend.velyo_backend.Dto.BookingDTO.BookingDTO;
import com.backend.velyo_backend.Entity.Booking;
import com.backend.velyo_backend.Mapper.AccommodationMapper.AccommodationSummaryMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AccommodationSummaryMapper.class})
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "accommodation.images", target = "accommodation.images", qualifiedByName = "accommodationImagesToStrings")
    @Mapping(source = "confirmed", target = "confirmed")
    @Mapping(source = "reviewed", target = "reviewed")
    BookingDTO entityToDto(Booking booking);
}
