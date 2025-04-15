package com.backend.velyo_backend.Security.Service;

import com.backend.velyo_backend.Dto.UserDTO.UserDTO;
import com.backend.velyo_backend.Entity.Role;
import com.backend.velyo_backend.Entity.User;
import com.backend.velyo_backend.Exception.ResourceAlreadyExistsException;
import com.backend.velyo_backend.Exception.ResourceNotFoundException;
import com.backend.velyo_backend.Mapper.UserMapper.UserMapper;
import com.backend.velyo_backend.Repository.UserRepository;
import com.backend.velyo_backend.Security.DTO.AuthenticationRequest;
import com.backend.velyo_backend.Security.DTO.AuthenticationResponse;
import com.backend.velyo_backend.Security.DTO.RegisterRequest;
import com.backend.velyo_backend.Util.BaseUrl;
import com.backend.velyo_backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationService implements BaseUrl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationResponse register(RegisterRequest request){
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        if(userRepository.existsByEmail(user.getEmail())){
            throw new ResourceAlreadyExistsException("El Mail ya esta registrado");
        }

        userRepository.save(user);
        try{
            emailService.sendConfirmationEmail(user.getFirstName(), user.getLastName(), user.getEmail());
        }catch (Exception e){
            log.error("Error enviando el mail: {}", e.getMessage());
        }

        var jwt = jwtService.generateToken(user);
        UserDTO userDTO = UserMapper.INSTANCE.entityToDto(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .user(userDTO)
                .build();
    }
    public AuthenticationResponse login(AuthenticationRequest request){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        }catch (BadCredentialsException ex){
            throw new BadCredentialsException("Email o contraseña incorrectos");
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrado"));

        var jwt = jwtService.generateToken(user);

        UserDTO userDTO = UserMapper.INSTANCE.entityToDto(user);

        userDTO.getFavorites().forEach(favoriteAccommodation-> favoriteAccommodation.setImages(favoriteAccommodation.getImages().stream()
                .map(image->getBaseUrl() + "/api/accommodation/images/" + image)
                .collect(Collectors.toSet())));

        return  AuthenticationResponse.builder()
                .token(jwt)
                .user(userDTO)
                .build();
    }
}
