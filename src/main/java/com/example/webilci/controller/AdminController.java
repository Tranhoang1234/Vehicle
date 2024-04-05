package com.example.webilci.controller;

import com.example.webilci.entity.Admin;
import com.example.webilci.repository.AdminRepository;
import com.example.webilci.repository.VehicleRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private final AdminRepository adminRepository;
    private final VehicleRepository vehicleRepository;

    public AdminController(AdminRepository adminRepository, VehicleRepository vehicleRepository) {
        this.adminRepository = adminRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping("/admin/login")
    public String login() {
        return "signin";
    }

    @PostMapping("/admin/login/try")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session) {
        Admin admin = adminRepository.findByEmailAndPassword(email, password);
        if (admin != null) {
            session.setAttribute("admin", admin);
            return "redirect:/admin";
        }
        return "login";
    }

    @GetMapping("/admin")
    public String index(Model model) {
        return "admin/home";
    }


}
