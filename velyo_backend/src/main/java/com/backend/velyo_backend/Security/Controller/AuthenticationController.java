package com.backend.velyo_backend.Security.Controller;


import com.backend.velyo_backend.Security.DTO.AuthenticationRequest;
import com.backend.velyo_backend.Security.DTO.AuthenticationResponse;
import com.backend.velyo_backend.Security.DTO.RegisterRequest;
import com.backend.velyo_backend.Security.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authenticationService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authenticationService.register(request));

    }
}
