package com.example.medcare.DAO;

import com.example.medcare.dto.loginDTO;
import com.example.medcare.dto.responseDTO;
import com.example.medcare.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDAOImpl implements UserDAO {
    EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public responseDTO signup(User user) {
        TypedQuery<User> thequery=entityManager.createQuery("SELECT u FROM User u WHERE u.email = :data1", User.class);
        thequery.setParameter("data1", user.getEmail());
        try {
            User user1 = thequery.getSingleResult();
            return new responseDTO("User already exists", false, 400, null);
        }catch (Exception e){
            TypedQuery<User> thequery1=entityManager.createQuery("SELECT u FROM User u WHERE u.username = :data1", User.class);
            thequery1.setParameter("data1", user.getUsername());
            try {
                User user1 = thequery1.getSingleResult();
                return new responseDTO("Username already exists", false, 400, null);
            }catch (Exception e1){
                entityManager.persist(user);
                return new responseDTO("User created successfully", true,200, user);
            }
        }
    }

    @Override
    public responseDTO login(loginDTO user) {
        try {
            TypedQuery<User> thequery = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :data1 AND u.password = :data2", User.class);
            thequery.setParameter("data1", user.getEmail());
            thequery.setParameter("data2", user.getPassword());
            User user1 = thequery.getSingleResult();
            return new responseDTO("User found", true, 200, user1);
        }catch (Exception e){
            return new responseDTO("User not found", false, 404, null);
        }
    }
}
