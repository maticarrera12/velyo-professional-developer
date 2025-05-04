package com.backend.velyo_backend.Mapper.UserMapper;


import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSummaryDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserDTO;
import com.backend.velyo_backend.Entity.Accommodation;
import com.backend.velyo_backend.Entity.User;
import com.backend.velyo_backend.Mapper.AccommodationMapper.AccommodationSummaryMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {AccommodationSummaryMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "favorites", target = "favorites", qualifiedByName = "mapAccommodationsToAccommodationsDTOs")
    UserDTO entityToDto(User user);

    @Named("mapAccommodationsToAccommodationsDTOs")
    default Set<AccommodationSummaryDTO> mapAccommodationsToAccommodationsDTOs(Set<Accommodation> accommodations){
        if (accommodations == null) {
            return new HashSet<>();
        }

        return accommodations.stream()
                .map(AccommodationSummaryMapper.INSTANCE::entityToDto)
                .collect(Collectors.toSet());
    }
}
