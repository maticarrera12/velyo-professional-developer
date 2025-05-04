package com.backend.velyo_backend.service.interfac;

import com.backend.velyo_backend.Dto.UserDTO.UserDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserFavoriteDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserSaveDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IUserService {

    UserDTO findById(UUID id);

    UserDTO findByEmail(String email);

    Page<UserDTO> findAll(Pageable pageable);

    void delete(UUID id);

    UserDTO updateByAdmin(UserSaveDTO userSaveDTO);

    UserDTO addFavorite(UserFavoriteDTO userFavoritesDTO);

    UserDTO removeFavorite(UserFavoriteDTO userFavoritesDTO);
}
