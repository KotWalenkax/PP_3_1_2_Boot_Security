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

    private AdminService adminService;
    private RoleService roleService;


    @Autowired
    public AdminController(AdminService adminService, RoleService roleService) {
        this.adminService = adminService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        List<User> list = adminService.getAllUsers();
        model.addAttribute("attribute", list);
        return "default";
    }

    @GetMapping("/addNewUser")
    public String addNewUser(Model model) {
        model.addAttribute("user", new User());
        return "add";
    }

    @PostMapping("/create")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam(required = false) String roleAdmin) {
        System.out.println(user.getUsername());

        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRole("USER"));
        if (roleAdmin != null && roleAdmin.equals("ADMIN")) {
            roles.add(roleService.getRole("ADMIN"));
        }

        user.setRoles(roles);
        adminService.add(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {

        ModelAndView modelAndView = new ModelAndView("edit");
        User user = adminService.get(id);

        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public String editUser(@ModelAttribute("user") User user,
                           @RequestParam(required = false) String roleAdmin) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.getRole("USER"));
        if (roleAdmin != null && roleAdmin.equals("ADMIN")) {
            roles.add(roleService.getRole("ADMIN"));
        }
        user.setRoles(roles);
        adminService.set(user.getId(), user);
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
}
