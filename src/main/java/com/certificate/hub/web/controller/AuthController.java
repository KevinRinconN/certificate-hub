package com.certificate.hub.web.controller;

import com.certificate.hub.domain.dto.request.LoginDto;
import com.certificate.hub.domain.dto.request.SignUpDto;
import com.certificate.hub.domain.dto.response.ApiResponse;
import com.certificate.hub.domain.dto.response.AuthResponse;
import com.certificate.hub.domain.dto.response.Status;
import com.certificate.hub.domain.error.ApiRequestException;
import com.certificate.hub.domain.service.AuthService;
import com.certificate.hub.persistence.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginDto loginDto) throws RuntimeException {

        return ResponseEntity.ok()
                .body(ApiResponse.<AuthResponse>builder()
                        .data(AuthResponse.builder()
                                .accessToken(authService.authenticate(loginDto))
                                .build())
                        .message("Successful Authorization")
                        .status(Status.SUCCESS)
                        .build());
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserEntity>> getMe(@RequestHeader("Authorization") String token) throws RuntimeException {

        return ResponseEntity.ok()
                .body(ApiResponse.<UserEntity>builder()
                        .data(authService.getMe(token))
                        .message("User data successfully")
                        .status(Status.SUCCESS)
                        .build());
    }

    @PostMapping("/sign-up")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<UserEntity>> signUp(@RequestBody @Valid SignUpDto signUpRequest) {
        return ResponseEntity.ok()
                .body(ApiResponse.<UserEntity>builder()
                        .data(authService.signUp(signUpRequest))
                        .message("User has been successfully created")
                        .status(Status.SUCCESS)
                        .build());
    }

    @GetMapping("/hola")
    public String saludar() {
        return "Hola mundo";
    }
}

