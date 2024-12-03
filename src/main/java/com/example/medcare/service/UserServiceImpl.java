package com.example.medcare.service;

import com.example.medcare.DAO.UserDAO;
import com.example.medcare.dto.loginDTO;
import com.example.medcare.dto.responseDTO;
import com.example.medcare.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    UserDAO userDTO;

    public UserServiceImpl(UserDAO userDTO) {
        this.userDTO = userDTO;
    }


    @Override
    public responseDTO signup(User user) {
        return userDTO.signup(user);
    }

    @Override
    public responseDTO login(loginDTO user) {
        return userDTO.login(user);
    }
}
