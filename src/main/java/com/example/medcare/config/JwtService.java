package com.example.medcare.config;
import com.example.medcare.repository.TokenRepository;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.security.Key;
import java.security.interfaces.RSAPublicKey;
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
    private static final String GOOGLE_JWKS_URL = "https://www.googleapis.com/oauth2/v3/certs";

    public String extractEmail(String token) throws Exception {

        // Parse the JWT token
        JWSObject jwsObject = JWSObject.parse(token);

        // Fetch JWKS from Google's endpoint
        JWKSet jwkSet = JWKSet.load(new URL(GOOGLE_JWKS_URL));

        // Get the RSAKey using the Key ID from the JWT header
        RSAKey rsaKey = (RSAKey) jwkSet.getKeyByKeyId(jwsObject.getHeader().getKeyID());

        if (rsaKey == null) {
            throw new IllegalArgumentException("No matching key found in JWKS");
        }

        // Convert the RSAKey to RSAPublicKey
        RSAPublicKey publicKey = rsaKey.toRSAPublicKey();

        // Create a verifier using the public key
        RSASSAVerifier verifier = new RSASSAVerifier(publicKey);

        // Verify the JWT signature
        if (!jwsObject.verify(verifier)) {
            throw new IllegalArgumentException("Invalid token signature");
        }

        // Parse the JWT claims
        JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwsObject.getPayload().toString());

        // Extract and return the email claim
        return claimsSet.getStringClaim("email");
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
