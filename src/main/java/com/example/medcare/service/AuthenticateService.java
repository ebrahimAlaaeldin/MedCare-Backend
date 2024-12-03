package com.example.medcare.service;


import com.example.medcare.Authorization.AuthenticationResponse;
import com.example.medcare.config.JwtService;
import com.example.medcare.dto.AuthenticationRequest;
import com.example.medcare.dto.ResponseMessageDto;
import com.example.medcare.entities.Token;
import com.example.medcare.repository.TokenRepository;
import com.example.medcare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;


    public Object authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())

            );
        }
        catch (Exception e) {
            Object response = ResponseMessageDto.builder()
                    .message("Invalid credentials")
                    .success(false)
                    .statusCode(401)
                    .build();

            log.error("Invalid credentials", e);
            return response;
        }
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var token = jwtService.generateToken(user);
        var tokenEntity = Token.builder()
                .token(token)
                .user(user)
                .revoked(false)
                .build();
        tokenRepository.save(tokenEntity);

        return ResponseMessageDto.builder()
                .message("Authentication successful")
                .success(true)
                .statusCode(200)
                .data(AuthenticationResponse.builder().token(token).build())
                .build();


    }





}
