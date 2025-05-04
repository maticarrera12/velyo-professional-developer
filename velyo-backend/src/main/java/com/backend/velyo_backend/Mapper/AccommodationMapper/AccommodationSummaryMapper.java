package com.backend.velyo_backend.Mapper.AccommodationMapper;


import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSummaryDTO;
import com.backend.velyo_backend.Entity.Accommodation;
import com.backend.velyo_backend.Entity.AccommodationImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface AccommodationSummaryMapper {

    AccommodationSummaryMapper INSTANCE = Mappers.getMapper(AccommodationSummaryMapper.class);

    @Mapping(source = "avgRating", target = "avgRating")
    @Mapping(source = "price", target = "price")
    @Mapping(target = "totalReviews", ignore = true)
    @Mapping(source = "images", target = "images", qualifiedByName = "accommodationImagesToStrings")
    AccommodationSummaryDTO entityToDto(Accommodation accommodation);

    @Named("accommodationImagesToStrings")
    default Set<String>stayImagesToStrings(Set<AccommodationImage> accommodationImages){
        return accommodationImages.stream()
                .map(AccommodationImage::getUrl)
                .collect(Collectors.toSet());
    }
}
