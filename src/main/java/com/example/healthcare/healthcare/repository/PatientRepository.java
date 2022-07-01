package com.example.healthcare.healthcare.repository;

import com.example.healthcare.healthcare.dto.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, String> {
}
