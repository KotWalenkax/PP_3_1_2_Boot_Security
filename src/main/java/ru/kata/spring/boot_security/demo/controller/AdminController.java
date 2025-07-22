package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPanel(Model model, Principal principal) {
        User admin = (User) userService.loadUserByLogin(principal.getName());
        List<User> users = userService.getAllUsers();
        List<Role> roles = (List<Role>) roleService.getAllRoles();

        model.addAttribute("admin", admin);
        model.addAttribute("users", users);
        model.addAttribute("allRoles", roles);
        model.addAttribute("newUser", new User());

        return "admin";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("newUser") User user,
                             @RequestParam Set<Long> roles) {
        setRolesForUser(user, roles);
        userService.add(user);
        return "redirect:/admin";
    }

    @PutMapping("/users/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute User user,
                             @RequestParam Set<Long> roles) {
        setRolesForUser(user, roles);
        userService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/users/{id}")
    @Transactional
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    private void setRolesForUser(User user, Set<Long> rolesIds) {
        if (rolesIds != null && !rolesIds.isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (Long roleId : rolesIds) {
                roles.add(roleService.getRoleById(roleId));
            }
            user.setRoles(roles);
        }
    }
}