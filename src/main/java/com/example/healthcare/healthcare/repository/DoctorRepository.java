package com.example.healthcare.healthcare.repository;

import com.example.healthcare.healthcare.dto.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, String> {
    List<Doctor> findBySpeciality(String speciality);
}
