package ru.kata.spring.boot_security.demo.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AdminService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class DBInit {

    private final AdminService adminService;
    private final RoleService roleService;

    @Autowired
    public DBInit(AdminService adminService, RoleService roleService) {
        this.adminService = adminService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void createUsersWithRoles() {

        Role role1 = new Role("ADMIN");
        Role role2 = new Role("USER");

        roleService.saveRole(role1);
        roleService.saveRole(role2);

        Set<Role> set = new HashSet<>();
        set.add(role1);
        set.add(role2);

        User user1 = new User(1L, "Admin", "LastAdmin", "postgres", new BCryptPasswordEncoder(8).encode("2288"), set);

        adminService.add(user1);
    }

}
