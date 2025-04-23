package com.backend.velyo_backend.Mapper.AccommodationMapper;

import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationSaveDTO;
import com.backend.velyo_backend.Entity.Accommodation;
import com.backend.velyo_backend.Entity.AccommodationImage;
import com.backend.velyo_backend.Entity.Amenity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper
public interface AccommodationSaveMapper {
    AccommodationSaveMapper INSTANCE = Mappers.getMapper(AccommodationSaveMapper.class);

    @Mapping(source = "images", target = "images", qualifiedByName = "accommodationImagesToStrings")
    @Mapping(source = "amenities", target = "amenities", qualifiedByName = "amenitiesToUuids")
    @Mapping(source = "category.id", target = "category_id")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "description", target = "description")
    AccommodationSaveDTO entityToDto(Accommodation accommodation);

    @Mapping(source = "images", target = "images", qualifiedByName = "stringsToAccommodationImages")
    @Mapping(source = "amenities", target = "amenities", qualifiedByName = "uuidsToAmenities")
    @Mapping(source = "category_id", target = "category.id")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "description", target = "description")
    Accommodation dtoToEntity(AccommodationSaveDTO accommodationSaveDTO);

    @Named("accommodationImagesToStrings")
    default Set<String> accommodationImagesToStrings(Set<AccommodationImage> accommodationImages) {
        return accommodationImages.stream()
                .map(AccommodationImage::getUrl)
                .collect(Collectors.toSet());
    }

    @Named("stringsToAccommodationImages")
    default Set<AccommodationImage> stringsToAccommodationImages(Set<String> strings) {
        return strings.stream()
                .map(url -> {
                    AccommodationImage accommodationImage = new AccommodationImage();
                    accommodationImage.setUrl(url);
                    return accommodationImage;
                })
                .collect(Collectors.toSet());
    }

    @Named("amenitiesToUuids")
    default Set<UUID> amenitiesToUuids(Set<Amenity> amenities) {
        return amenities.stream()
                .map(Amenity::getId)
                .collect(Collectors.toSet());
    }

    @Named("uuidsToAmenities")
    default Set<Amenity> uuidsToAmenities(Set<UUID> uuids) {
        return uuids.stream()
                .map(uuid -> {
                    Amenity amenity = new Amenity();
                    amenity.setId(uuid);
                    return amenity;
                })
                .collect(Collectors.toSet());
    }
}
