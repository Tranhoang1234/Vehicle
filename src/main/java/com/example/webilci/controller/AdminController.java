package com.example.webilci.controller;

import com.example.webilci.entity.Admin;
import com.example.webilci.repository.AdminRepository;
import com.example.webilci.repository.ReservationRepository;
import com.example.webilci.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final VehicleRepository vehicleRepository;

    public AdminController(AdminRepository adminRepository, UserRepository userRepository, ReservationRepository reservationRepository, VehicleRepository vehicleRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping("/admin/login")
    public String login() {
        return "signin-admin";
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
        return "redirect:/admin/login";
    }

    @GetMapping("/admin")
    public String index(Model model) {
        model.addAttribute("agencyCount", userRepository.countAllByRole("SALES"));
        model.addAttribute("carCount", vehicleRepository.findAllByVehicleType_Id(1).size());
        model.addAttribute("truckCount", vehicleRepository.findAllByVehicleType_Id(3).size());
        model.addAttribute("motorcycleCount", vehicleRepository.findAllByVehicleType_Id(2).size());
        model.addAttribute("userCount", userRepository.countAllByRole("CLIENTS"));
        model.addAttribute("reservationCount", reservationRepository.count());
        model.addAttribute("total2Wheels", vehicleRepository.count2Wheels());
        model.addAttribute("total4Wheels", vehicleRepository.count4Wheels());
        model.addAttribute("total", vehicleRepository.count2Wheels() + vehicleRepository.count4Wheels());
        return "admin/home";
    }
}
