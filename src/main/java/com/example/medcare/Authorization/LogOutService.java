package com.example.medcare.Authorization;

import com.example.medcare.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class LogOutService implements LogoutHandler {

    private final TokenRepository tokenRepo;


    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication)
    {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepo.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setRevoked(true);
            tokenRepo.save(storedToken);
            SecurityContextHolder.clearContext();
        }

    }
}
