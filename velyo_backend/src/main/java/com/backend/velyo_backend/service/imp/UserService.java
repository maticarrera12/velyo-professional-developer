package com.backend.velyo_backend.service.imp;

import com.backend.velyo_backend.Dto.UserDTO.UserDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserFavoriteDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserSaveDTO;
import com.backend.velyo_backend.Repository.*;
import com.backend.velyo_backend.Util.BaseUrl;
import com.backend.velyo_backend.service.interfac.IUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@Service
public class UserService implements IUserService, BaseUrl {

    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AmenityRepository amenityRepository;
    private final CategoryRepository categoryRepository;
    private final BookingRepository bookingRepository;

    public UserService(UserRepository userRepository, AccommodationRepository accommodationRepository, PasswordEncoder passwordEncoder, AmenityRepository amenityRepository, CategoryRepository categoryRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.accommodationRepository = accommodationRepository;
        this.passwordEncoder = passwordEncoder;
        this.amenityRepository = amenityRepository;
        this.categoryRepository = categoryRepository;
        this.bookingRepository = bookingRepository;
    }


    @Override
    public UserDTO findById(UUID id) {
        return null;
    }

    @Override
    public UserDTO findByEmail(String email) {
        return null;
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public UserDTO updateByAdmin(UserSaveDTO userSaveDTO) {
        return null;
    }

    @Override
    public UserDTO addFavorite(UserFavoriteDTO userFavoriteDTO) {
        return null;
    }

    @Override
    public UserDTO removeFavorite(UserFavoriteDTO userFavoriteDTO) {
        return null;
    }
}
