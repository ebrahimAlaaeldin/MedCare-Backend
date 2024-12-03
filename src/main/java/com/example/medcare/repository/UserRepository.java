package com.example.medcare.repository;
import org.springframework.stereotype.Repository;
import com.example.medcare.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
interface userRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
@Repository
public class UserRepository {
    private userRepo userRepo;
    public UserRepository(userRepo userRepo) {
        this.userRepo = userRepo;
    }
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
    public User save(User user) {
        return userRepo.save(user);
    }
}
