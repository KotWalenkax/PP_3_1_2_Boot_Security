package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        User user = get(id);
        userRepository.delete(user);
    }

    @Override
    public User get(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void set(Long id, User newUser) {
        userRepository.saveAndFlush(newUser);
    }

    @Override
    public UserDetails loadUserByLogin(String username) {
        return userRepository.findByUsername(username);
    }
}