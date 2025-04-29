package com.backend.velyo_backend.service.imp;

import com.backend.velyo_backend.Dto.UserDTO.UserDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserFavoriteDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserSaveDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserUpdateNameDTO;
import com.backend.velyo_backend.Entity.Accommodation;
import com.backend.velyo_backend.Entity.User;
import com.backend.velyo_backend.Exception.ResourceAlreadyExistsException;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import com.backend.velyo_backend.Mapper.UserMapper.UserMapper;
import com.backend.velyo_backend.Mapper.UserMapper.UserSaveMapper;
import com.backend.velyo_backend.Repository.*;
import com.backend.velyo_backend.Util.BaseUrl;
import com.backend.velyo_backend.Util.DashboardResponse;
import com.backend.velyo_backend.service.interfac.IUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

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
        log.debug("Buscando usuario por id: {}id", id);
        return userRepository.findById(id)
                .map(UserMapper.INSTANCE::entityToDto)
                .orElseThrow(()->{
                    log.error("El usuario con id: {} no fue encontrado", id);
                    return new ResourceNotFoundException("El usuario con id: " + id + " no fue encontrado");
                });
    }

    @Override
    public UserDTO findByEmail(String email) {
        log.debug("Buscando usuario por email: {}", email);
        UserDTO userDTO = userRepository.findByEmail(email)
                .map(UserMapper.INSTANCE::entityToDto)
                .orElseThrow(()->{
                    log.error("El usuario con email: {} no fue encontrado", email);
                    return new ResourceNotFoundException("El usuario con email " + email + " no fue encontrado");
                });
        userDTO.getFavorites().forEach(favoriteAccommodation -> favoriteAccommodation.setImages(favoriteAccommodation.getImages().stream()
                .map(image -> getBaseUrl() + "/api/accommodations/images/" + image)
                .collect(Collectors.toSet())));
        return userDTO;
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        log.debug("Buscando todos los usuarios paginados: {}", pageable);
        Page<UserDTO> pageUsers = userRepository.findAll(pageable).map(UserMapper.INSTANCE::entityToDto);
        log.info("Encontrados {} usuarios", pageUsers.getTotalElements());
        return pageUsers;
    }

    public void save(UserSaveDTO userSaveDTO){
        log.debug("Agregando usuario: {}", userSaveDTO.getEmail());

        if (userRepository.existsByEmail(userSaveDTO.getEmail())){
            log.error("El usuario con email :{} ya existe", userSaveDTO.getEmail());
            throw new ResourceAlreadyExistsException("El usuario con email" + userSaveDTO.getEmail() + " ya existe");
        }

        User user = UserSaveMapper.INSTANCE.dtoToEntity(userSaveDTO);
        user.setPassword(passwordEncoder.encode(userSaveDTO.getPassword()));
        userRepository.save(user);
        log.info("Usuario agregado: {}", user.getEmail());

    }

    @Override
    public void delete(UUID id) {
        log.debug("Borrando el usuario: {}", id);
        User userToDelete = userRepository.findById(id).orElseThrow(()->{
            log.error("El usuario con id: {} no fue encontrado", id);
            return new ResourceNotFoundException("El usuario con id: " + id + " no fue encontrado");
        });
        userRepository.delete(userToDelete);
        log.info("El usuario ha sido borrado: {}", userToDelete.getEmail());
    }

    @Override
    public UserDTO updateByAdmin(UserSaveDTO userSaveDTO) {
        log.debug("Actualizando usuario por admin: {}", userSaveDTO.getId());
        System.out.println("userSaveDTO.getId() = " + userSaveDTO.getId());

        User userToUpdate = userRepository.findById(userSaveDTO.getId()).orElseThrow(()->{
            log.error("El usuario con id: {} no fue encontrado", userSaveDTO.getId());
            return new ResourceNotFoundException("El usuario con id: " + userSaveDTO.getId() + " no fue encontrado");
        });

        if (!userToUpdate.getEmail().equals(userSaveDTO.getEmail())
        && userRepository.existsByEmail(userSaveDTO.getEmail())){
            log.error("El usuario con email: {} ya existe", userSaveDTO.getEmail());
            throw new ResourceAlreadyExistsException("El usuario con email: " + userSaveDTO.getEmail() + " ya existe");
        }

        userToUpdate.setFirstname(userSaveDTO.getFirstname());
        userToUpdate.setLastname(userSaveDTO.getLastname());
        userToUpdate.setEmail(userSaveDTO.getEmail());
        userToUpdate.setRole(userSaveDTO.getRole());

        if (userSaveDTO.getPassword() != null && !userSaveDTO.getPassword().isEmpty()){
            userToUpdate.setPassword(passwordEncoder.encode(userSaveDTO.getPassword()));
        }

        userRepository.save(userToUpdate);
        log.info("El usuario fue actualizado: {}", userToUpdate.getEmail());
        return UserMapper.INSTANCE.entityToDto(userToUpdate);
    }

    @Override
    public UserDTO addFavorite(UserFavoriteDTO userFavoritesDTO) {
        log.info("Agregando a favoritos para el usuario con id: {}", userFavoritesDTO.getId());
        User user  = userRepository.findById(userFavoritesDTO.getId()).orElseThrow(()->{
            log.error("El usuario con id: {} no fue encontrado", userFavoritesDTO.getId());
            return new ResourceNotFoundException("El usuario con id: {}" + userFavoritesDTO.getId() + " no fue encontrado");
        });
        Accommodation accommodation = accommodationRepository.findById(userFavoritesDTO.getFavorite()).orElseThrow(()->{
            log.error("El alojaminento con id: {}", userFavoritesDTO.getFavorite());
            return new ResourceNotFoundException("El alojamiento con id: " + userFavoritesDTO.getFavorite() + " no fue encontrado");
        });

        user.getFavorites().add(accommodation);
        userRepository.save(user);
        log.info("Favorito agregado al usuario con id: {}", userFavoritesDTO.getId());
        UserDTO userDTO = UserMapper.INSTANCE.entityToDto(user);
        userDTO.getFavorites().forEach(favoriteAccommodation -> favoriteAccommodation.setImages(favoriteAccommodation.getImages().stream()
                .map(image-> getBaseUrl() + "/api/accommodations/images/" + image)
                .collect(Collectors.toSet())));
        return userDTO;
    }

    @Override
    public UserDTO removeFavorite(UserFavoriteDTO userFavoritesDTO) {
        log.error("El usuario con id: {} no fue encontrado", userFavoritesDTO.getId());
        User user = userRepository.findById(userFavoritesDTO.getId()).orElseThrow(()->{
            log.error("El usuario con id: {} no fue encontrado", userFavoritesDTO.getId());
            return  new ResourceNotFoundException("El usuario con id: " + userFavoritesDTO.getId() + " no fue encontrado");
        });

        Accommodation accommodation = accommodationRepository.findById(userFavoritesDTO.getFavorite()).orElseThrow(()->{
            log.error("El alojamiento con id: {} no fue encontrado", userFavoritesDTO.getFavorite());
            return new ResourceNotFoundException("El alojamiento con id: " + userFavoritesDTO.getFavorite() + " no fue encontrado");
        });
        user.getFavorites().remove(accommodation);
        userRepository.save(user);
        log.info("Favorito removido del usuario con id: {}", userFavoritesDTO.getId());
        UserDTO userDTO = UserMapper.INSTANCE.entityToDto(user);
        userDTO.getFavorites().forEach(favoriteAccommodation -> favoriteAccommodation.setImages(favoriteAccommodation.getImages().stream()
                .map(image -> getBaseUrl() + "/api/accommodations/images/" + image).collect(Collectors.toSet())));
        return userDTO;
    }

    public UserDTO updateName(UserUpdateNameDTO userUpdateNameDTO, String email){
        log.debug("Actualizando el nombre del usuario con el email: {}", email);
        User userToUpdate = userRepository.findByEmail(email).orElseThrow(()->{
            log.error("El usuario con email: {} no fue encontrado", email);
            return new ResourceNotFoundException("El usuario con email: " + email + " no fue encontrado");
        });
        userToUpdate.setFirstname(userUpdateNameDTO.getFirstname());
        userToUpdate.setLastname(userUpdateNameDTO.getLastname());
        userRepository.save(userToUpdate);
        log.info("El nombre del usuario con email: {} fue actualizado", email);
        return UserMapper.INSTANCE.entityToDto(userToUpdate);
    }

    public DashboardResponse getDashboardInfo(){
        log.debug("Obteniendo informacion del dashboard");
        int totalUsers = userRepository.findAll().size();
        int totalAccommodations = accommodationRepository.findAll().size();
        int totalAmenities = amenityRepository.findAll().size();
        int totalCategories = categoryRepository.findAll().size();
        int totalBookings = bookingRepository.findAll().size();
        log.info("Devolviendo informacion del dashboard");
        return  new DashboardResponse(totalUsers, totalAccommodations, totalAmenities, totalCategories, totalBookings);
    }
}
