package com.example.medcare.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.medcare.entities.User;
import com.example.medcare.repository.UserRepository;
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User ThirdPartyLoginOrSignUp(OAuth2AuthenticationToken authentication) {    
        OAuth2User authenticator=authentication.getPrincipal();
        String email=authenticator.getAttribute("email");
        User user=userRepository.findByEmail(email);
        if(user==null) {
            user=new User();
            user.setEmail(email);
            user.setFirstName(authenticator.getAttribute("given_name"));
            user.setLastName(authenticator.getAttribute("family_name"));
            user=userRepository.save(user);
        }
        return user;
    }
}
