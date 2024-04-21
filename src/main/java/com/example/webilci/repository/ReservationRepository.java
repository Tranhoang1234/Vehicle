package com.example.webilci.repository;

import com.example.webilci.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findAllByUser_Id(Integer id);
    List<Reservation> findAllByVehicle_Sales_Id(Integer id);

    @Query("SELECT COALESCE(SUM(r.totalPrice), 0) FROM Reservation r JOIN r.vehicle v WHERE v.vehicleType.id IN (2, 4)")
    Float count2Wheels();

    @Query("SELECT COALESCE(SUM(r.totalPrice), 0) FROM Reservation r JOIN r.vehicle v WHERE v.vehicleType.id IN (1, 3)")
    Float count4Wheels();
}
