package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getAllRoles();
    void saveRole(Role role);
    Role getRoleById(Long id);
    void deleteRoleById(Long id);
    Role getRole(String role);

}
