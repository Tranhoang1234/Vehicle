package com.example.webilci.controller;

import com.example.webilci.entity.User;
import com.example.webilci.entity.Vehicle;
import com.example.webilci.payloads.dto.VehicleDto;
import com.example.webilci.entity.VehicleType;
import com.example.webilci.repository.UserRepository;
import com.example.webilci.repository.VehicleRepository;
import com.example.webilci.repository.VehicleTypeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/vehicle")
public class AdminVehicleController {
    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public AdminVehicleController(VehicleTypeRepository vehicleTypeRepository, VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/admin/vehicle")
    public String vehicles(Model model) {
        model.addAttribute("vehicles", vehicleRepository.findAll());
        return "admin/list-vehicle";
    }

    @GetMapping("/admin/vehicle/form-create-vehicle")
    public String getFormCreateVehicle(Model model){
        VehicleDto vehicleDto = new VehicleDto();
        model.addAttribute("vehicle",vehicleDto);
        model.addAttribute("users", userRepository.findAllByRole("SALES"));
        model.addAttribute("types", vehicleTypeRepository.findAll());
        return "admin/vehicle-create";
    }

    @PostMapping("/admin/vehicle/save")
    public String saveVehicle(@ModelAttribute(name = "vehicle") VehicleDto vehicleDto){
        Vehicle vehicle = new Vehicle();
        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleDto.getVehicleTypeId()).get();
        User user = userRepository.findById(vehicleDto.getUserId()).get();
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setCylinder(vehicleDto.getCylinder());
        vehicle.setPrice(vehicleDto.getPrice());
        vehicle.setWeight(vehicleDto.getWeight());
        vehicle.setName(vehicleDto.getName());
        vehicle.setDoorNumbers(vehicleDto.getDoorNumbers());
        vehicle.setVehicleType(vehicleType);
        vehicle.setSales(user);
        vehicleRepository.save(vehicle);
        return "redirect:/admin/vehicle";
    }

    @GetMapping("/admin/vehicle/form-update-vehicle/{id}")
    public String getFormUpdateVehicle(@PathVariable("id") int id, Model model){
        Vehicle vehicle = vehicleRepository.findById(id).get();
        model.addAttribute("types", vehicleTypeRepository.findAll());
        model.addAttribute("users", userRepository.findAllByRole("SALES"));
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setId(vehicle.getId());
        vehicleDto.setColor(vehicle.getColor());
        vehicleDto.setCylinder(vehicle.getCylinder());
        vehicleDto.setPrice(vehicle.getPrice());
        vehicleDto.setWeight(vehicle.getWeight());
        vehicleDto.setName(vehicle.getName());
        vehicleDto.setDoorNumbers(vehicle.getDoorNumbers());
        vehicleDto.setVehicleTypeId(vehicle.getVehicleType().getId());
        vehicleDto.setVehicleTypeName(vehicle.getVehicleType().getName());
        vehicleDto.setUserId(vehicle.getSales().getId());
        vehicleDto.setFullName(vehicle.getSales().getFullName());
        model.addAttribute("vehicle",vehicleDto);
        return "admin/vehicle-update";
    }

    @PostMapping("/admin/vehicle/update")
    public String update(@ModelAttribute(name = "vehicle") VehicleDto vehicleDto){
        Vehicle vehicle = vehicleRepository.findById(vehicleDto.getId()).get();
        VehicleType vehicleType = vehicleTypeRepository.findById(vehicleDto.getVehicleTypeId()).get();
        User user = userRepository.findById(vehicleDto.getUserId()).get();
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setCylinder(vehicleDto.getCylinder());
        vehicle.setPrice(vehicleDto.getPrice());
        vehicle.setWeight(vehicleDto.getWeight());
        vehicle.setName(vehicleDto.getName());
        vehicle.setDoorNumbers(vehicleDto.getDoorNumbers());
        vehicle.setVehicleType(vehicleType);
        vehicle.setSales(user);
        vehicleRepository.save(vehicle);
        return "redirect:/admin/vehicle";
    }

    @GetMapping("/admin/vehicle/delete/{id}")
    public String delete(@PathVariable("id") int id){
        vehicleRepository.deleteById(id);
        return "redirect:/admin/vehicle";
    }

}
