package com.example.healthcare.healthcare.repository;

import com.example.healthcare.healthcare.dto.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
}
