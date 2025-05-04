package com.backend.velyo_backend.Controller;

import com.backend.velyo_backend.Dto.UserDTO.UserDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserFavoriteDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserSaveDTO;
import com.backend.velyo_backend.Dto.UserDTO.UserUpdateNameDTO;
import com.backend.velyo_backend.Util.ApiPageResponse;
import com.backend.velyo_backend.Util.DashboardResponse;
import com.backend.velyo_backend.service.imp.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api/users")
public class UserController {

    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserSaveDTO userSaveDTO){
        log.debug("Recibida la solicitud para crear usuario");
        userService.save(userSaveDTO);
        log.info("Usuario creado");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("El usuario se ha creado correctamente");
    }

    @GetMapping
    public ResponseEntity<ApiPageResponse<List<UserDTO>>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        log.debug("Recibida la solicitud para obtener todos los usuarios");
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> pageUsers = userService.findAll(pageable);
        List<UserDTO> users = pageUsers.getContent();
        log.info("Devolviendo {} users", users.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ApiPageResponse<>(
                                pageUsers.getTotalPages(),
                                (int) pageUsers.getTotalElements(),
                                users,
                                "Usuarios devueltos"
                        )
                );
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserSaveDTO userSaveDTO){
        log.debug("Recibida la solicitud para actualizar el usuario con id: {}", userSaveDTO.getId());
        System.out.println(userSaveDTO);
        UserDTO updatedUser = userService.updateByAdmin(userSaveDTO);
        log.info("El usuario con id: {} fue actualizado", userSaveDTO.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUser);
    }

    @PutMapping("/update-name")
    public  ResponseEntity<UserDTO> updateName(@RequestBody UserUpdateNameDTO userUpdateNameDTO){
        log.debug("Recibida la solicitud para actualizar: {}", userUpdateNameDTO);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDTO updatedUser = userService.updateName(userUpdateNameDTO, userDetails.getUsername());
        log.info("El nombre de usuario con id: {} fue actualizado", updatedUser.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUser);
    }

    @GetMapping("me")
    public ResponseEntity<UserDTO> getUser(){
        log.debug("Recibida la solicitud para obtener usuario");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDTO userSaveDTO = userService.findByEmail(userDetails.getUsername());
        log.info("Devolviendo usuario");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userSaveDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteUser(@PathVariable("id")UUID id){
        log.debug("Recibida la solicitud para borrar el usuario con id: {}", id);
        userService.delete(id);
        log.info("El usuario con id: {} fue borrado", id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/remove-favorite")
    public ResponseEntity<UserDTO> removeFavorite(@RequestBody UserFavoriteDTO userFavoriteDTO){
        log.debug("Recibida la solicitud para eliminar un favorito del usuario con id: {}", userFavoriteDTO.getId());
        UserDTO userDTO = userService.removeFavorite(userFavoriteDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTO);
    }

    @PostMapping("/add-favorite")
    public ResponseEntity<UserDTO> addFavorite(@RequestBody UserFavoriteDTO userFavoriteDTO){
        log.debug("Recibida la solicitud para agregar a favorito el usuario con id: {}", userFavoriteDTO.getId());
        UserDTO userDTO = userService.addFavorite(userFavoriteDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public  ResponseEntity<DashboardResponse> getDaashboardInfo(){
        log.debug("Recibida la solicitud para obetener la informacion del dashboard");
        DashboardResponse dashboardResponse = userService.getDashboardInfo();
        log.info("Devolviendo la informacion del dashboard");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dashboardResponse);
    }


}
