package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void add(User user);
    void delete(Long id);
    User get(Long id);
    List<User> getAllUsers();
    void update(Long id, User newUser);
    UserDetails loadUserByLogin(String username);
}
