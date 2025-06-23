package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AdminService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping()
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final RoleService roleService;

    @Autowired
    public AdminController(AdminService adminService, RoleService roleService) {
        this.adminService = adminService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        List<User> list = adminService.getAllUsers();
        model.addAttribute("users", list);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "default";
    }

    @GetMapping("/addNewUser")
    public String addNewUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "add";
    }

    @PostMapping("/create")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam Set<Long> roles) {
        Set<Role> roleSet = new HashSet<>();
        for (Long roleId : roles) {
            roleSet.add(roleService.getRoleById(roleId));
        }
        user.setRoles(roleSet);
        adminService.add(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable(name = "id") Long id, Model model) {
        User user = adminService.get(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "edit";
    }

    @PostMapping("/update/{id}")
    public String editUser(@ModelAttribute("user") User user,
                           @PathVariable Long id,
                           @RequestParam Set<Long> roles) {
        Set<Role> roleSet = new HashSet<>();
        for (Long roleId : roles) {
            roleSet.add(roleService.getRoleById(roleId));
        }
        user.setRoles(roleSet);
        adminService.update(id, user);
        return "redirect:/admin";
    }


    @GetMapping("/delete/{id}")
    @Transactional
    public String delete(@PathVariable(name = "id") Long id) {
        adminService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/myInfo")
    public String showUserInfo(Principal principal, Model model) {
        model.addAttribute("user", adminService.loadUserByLogin(principal.getName()));
        return "admin-info";
    }

    @ModelAttribute("hasRole")
    public boolean hasRole(@RequestParam(value = "userId", required = false) Long userId,
                           Principal principal,
                           @ModelAttribute("user") User user,
                           @RequestParam(value = "roleId", required = false) Long roleId) {
        if (roleId == null) return false;

        if (user != null && user.getRoles() != null) {
            return user.getRoles().stream().anyMatch(r -> r.getId().equals(roleId));
        }

        return false;
    }
}
