package com.example.webilci.repository;

import com.example.webilci.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    List<Vehicle> findAllBySales_Id(int id);

    List<Vehicle> findAllByVehicleType_Id(Integer id);


    @Query("SELECT SUM(v.price) FROM Vehicle v WHERE v.vehicleType.id IN (2, 4)")
    Long count2Wheels();

    @Query("SELECT SUM(v.price) FROM Vehicle v WHERE v.vehicleType.id IN (1, 3)")
    Long count4Wheels();

    Integer countAllByVehicleType_Id(Integer id);

    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.agency.id = :agencyId")
    Integer countVehiclesByAgencyId(@Param("agencyId") Integer agencyId);
}
