package com.certificate.hub.domain.service;

import com.certificate.hub.domain.dto.request.LoginDto;
import com.certificate.hub.domain.dto.request.SignUpDto;
import com.certificate.hub.domain.error.ApiRequestException;
import com.certificate.hub.domain.repository.UserRepository;
import com.certificate.hub.persistence.entity.Roles;
import com.certificate.hub.persistence.entity.UserEntity;
import com.certificate.hub.web.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String authenticate(@RequestBody LoginDto loginDto) throws RuntimeException {

        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(login);

        return this.jwtUtil.create(loginDto.getUsername());
    }

    public UserEntity getMe(String token) throws RuntimeException {
        if (token == null || !token.startsWith("Bearer")) throw new RuntimeException("Invalid access token");

        String jwt = token.split(" ")[1].trim();

        String username = this.jwtUtil.getUsername(jwt);

        return this.userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

    }

    public UserEntity signUp(SignUpDto signUpRequest) throws ApiRequestException {
        Optional<UserEntity> existingUser = userRepository.findById(signUpRequest.getUsername())
                .or(() -> this.userRepository.findByEmail(signUpRequest.getEmail()));

        if (existingUser.isPresent()) {
            if (Objects.equals(existingUser.get().getUsername(), signUpRequest.getUsername()))
                throw new ApiRequestException("A user with this username already exists");
            if (Objects.equals(existingUser.get().getEmail(), signUpRequest.getEmail()))
                throw new ApiRequestException("A user with this email already exists");
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserEntity newUser = UserEntity.builder()
                .username(signUpRequest.getUsername())
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                .rol(Roles.valueOf(signUpRequest.getRol()))
                .build();

        return userRepository.save(newUser);
    }

}
