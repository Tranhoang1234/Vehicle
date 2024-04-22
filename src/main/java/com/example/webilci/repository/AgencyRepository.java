package com.example.webilci.repository;

import com.example.webilci.entity.Agency;
import com.example.webilci.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgencyRepository extends JpaRepository<Agency, Integer> {
}
