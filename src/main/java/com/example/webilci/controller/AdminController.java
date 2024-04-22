package com.example.webilci.controller;

import com.example.webilci.entity.Admin;
import com.example.webilci.entity.Agency;
import com.example.webilci.entity.User;
import com.example.webilci.payloads.dto.AgencyDTO;
import com.example.webilci.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final VehicleRepository vehicleRepository;
    private final AgencyRepository agencyRepository;

    public AdminController(AdminRepository adminRepository, UserRepository userRepository, ReservationRepository reservationRepository, VehicleRepository vehicleRepository, AgencyRepository agencyRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.vehicleRepository = vehicleRepository;
        this.agencyRepository = agencyRepository;
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
        model.addAttribute("total2Wheels", reservationRepository.count2Wheels());
        model.addAttribute("total4Wheels", reservationRepository.count4Wheels());
        model.addAttribute("total", reservationRepository.count2Wheels() + reservationRepository.count4Wheels());

        Integer car = vehicleRepository.countAllByVehicleType_Id(1);
        Integer truck = vehicleRepository.countAllByVehicleType_Id(3);
        Integer twoWheels = vehicleRepository.countAllByVehicleType_Id(2) + vehicleRepository.countAllByVehicleType_Id(4);
        Integer sum = car + truck + twoWheels;


        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double carPercent = Double.parseDouble(decimalFormat.format((double) car / sum * 100));
        double truckPercent = Double.parseDouble(decimalFormat.format((double) truck / sum * 100));
        double twoWheelPercent = Double.parseDouble(decimalFormat.format((double) twoWheels / sum * 100));

        model.addAttribute("carPercent", carPercent);
        model.addAttribute("truckPercent", truckPercent);
        model.addAttribute("twoWheelPercent", twoWheelPercent);

        Long count = vehicleRepository.count();
        List<AgencyDTO> agencyDTOS = new ArrayList<>();
        List<Agency> agencies = agencyRepository.findAll();
        for(Agency agency : agencies) {
            Integer countVehicle = vehicleRepository.countVehiclesByAgencyId(agency.getId());
            double percent = Double.parseDouble(decimalFormat.format((double) countVehicle / count * 100));
            AgencyDTO agencyDTO = new AgencyDTO();
            agencyDTO.setId(agency.getId());
            agencyDTO.setName(agency.getName());
            agencyDTO.setPercent(percent);
            agencyDTOS.add(agencyDTO);
        }
        model.addAttribute("agencies", agencyDTOS);
        return "admin/home";
    }
}
