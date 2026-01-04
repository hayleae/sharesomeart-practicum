package com.example.demo3.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo3.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

    public Optional<User> findByUsernameOrEmail(String username, String email);

    @SuppressWarnings("null")
    public Optional<User> findById(Long id);

    public List<User> findByUsernameContains(String keyword);

    Optional<User> findByUsername(String username);
}
