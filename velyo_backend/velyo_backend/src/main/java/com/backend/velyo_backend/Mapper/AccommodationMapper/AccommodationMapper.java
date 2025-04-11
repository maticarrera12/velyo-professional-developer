package com.backend.velyo_backend.Mapper.AccommodationMapper;


import com.backend.velyo_backend.Dto.AccommodationDTO.AccommodationDTO;
import com.backend.velyo_backend.Dto.AddressDTO.AddressDTO;
import com.backend.velyo_backend.Dto.AmenityDTO.AmenityDTO;
import com.backend.velyo_backend.Entity.Accommodation;
import com.backend.velyo_backend.Entity.AccommodationImage;
import com.backend.velyo_backend.Entity.Address;
import com.backend.velyo_backend.Entity.Amenity;
import com.backend.velyo_backend.Mapper.ReviewMapper.ReviewSummaryMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {ReviewSummaryMapper.class})
public interface AccommodationMapper {

    AccommodationMapper INSTANCE = Mappers.getMapper(AccommodationMapper.class);

    @Mapping(source = "images", target = "images", qualifiedByName = "accommodationImagesToStrings")
    @Mapping(source = "amenities", target = "amenities", qualifiedByName = "amenitiesToAmenitiesDTO")
    @Mapping(source = "category.id", target = "category_id")
    @Mapping(source = "address", target = "address", qualifiedByName = "addressToAddressDTO")
    @Mapping(source = "avgRating", target = "avgRating")
    @Mapping(target = "unavailableDates", ignore = true)
    @Mapping(target =  "reviews", ignore = true)
    @Mapping(target = "totalReviews", ignore = true)
    AccommodationDTO entityToDto(Accommodation accommodation);


    @Mapping(source = "images", target = "images", qualifiedByName = "stringsToAccommodationImages")
    @Mapping(source = "amenities", target = "amenities", qualifiedByName = "amenitiesDTOToAmenities")
    @Mapping(source = "category_id", target = "category.id")
    @Mapping(source = "avgRating", target = "avgRating")
    @Mapping(source = "address", target = "address", qualifiedByName = "addressDTOToAddress")
    Accommodation dtoToEntity(AccommodationDTO accommodationDTO);

    @Named("accommodationImagesToStrings")
    default Set<String> accommodationImagesToStrings(Set<AccommodationImage> accommodationImages){
        return accommodationImages.stream()
                .map(AccommodationImage::getUrl)
                .collect(Collectors.toSet());
    }

    @Named("stringsToAccommodationImages")
    default Set<AccommodationImage> stringsToAccommodationImages(Set<String>strings){
        return strings.stream()
                .map(url->{
                    AccommodationImage accommodationImage = new AccommodationImage();
                    accommodationImage.setUrl(url);
                    return accommodationImage;
                })
                .collect(Collectors.toSet());
    }

    @Named("amenitiesToAmenitiesDTO")
    default Set<AmenityDTO> amenitiesToAmenitiesDTO(Set<Amenity> amenities){
        return amenities.stream()
                .map(amenity -> {
                    AmenityDTO amenityDTO = new AmenityDTO();
                    amenityDTO.setId(amenity.getId());
                    amenityDTO.setName(amenity.getName());
                    amenityDTO.setIcon(amenity.getIcon());
                    return amenityDTO;
                })
                .collect(Collectors.toSet());
    }

    @Named("amenitiesDTOToAmenities")
    default Set<Amenity> amenitiesDTOToAmenities(Set<AmenityDTO> amenities){
        return amenities.stream()
                .map(amenityDTO -> {
                    Amenity amenity = new Amenity();
                    amenity.setId(amenityDTO.getId());
                    return amenity;
                })
                .collect(Collectors.toSet());
    }

    @Named("addressToAddressDTO")
    default AddressDTO addressToAddressDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }

    @Named("addressDTOToAddress")
    default Address addressDTOToAddress(AddressDTO addressDTO) {
        return Address.builder()
                .id(addressDTO.getId())
                .street(addressDTO.getStreet())
                .city(addressDTO.getCity())
                .country(addressDTO.getCountry())
                .build();
    }
}
