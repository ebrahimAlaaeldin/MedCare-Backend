package com.example.medcare.config;


import com.example.medcare.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String Security_Key = "088TI3YCXQd26NOxZJXXKwFAIyojGuY2Y6HX7kVb9ftVp3Rz"; //
    private final TokenRepository tokenRepo;
    private Claims extractAllClaims(String token) {

        return Jwts.
                parserBuilder().
                setSigningKey(getSigningKey()).
                build().
                parseClaimsJws(token).
                getBody();
    }

    public Key getSigningKey() {
        byte[] signingKey = Decoders.BASE64.decode(Security_Key);
        return Keys.hmacShaKeyFor(signingKey);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername() )&& !isTokenExpired(token));
    }


    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    //Check if the token is expired or not by comparing the expiration date with the current date
    private boolean isTokenExpired(String token) {
        var token2 = tokenRepo.findByToken(token);
        boolean isExpired = token2.isPresent() && token2.get().isRevoked();
        return extractExpiration(token).before(new Date()) || isExpired;
    }


}
