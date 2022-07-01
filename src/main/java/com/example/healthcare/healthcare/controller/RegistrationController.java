package com.example.healthcare.healthcare.controller;

import com.example.healthcare.healthcare.common.Messages;
import com.example.healthcare.healthcare.dto.Doctor;
import com.example.healthcare.healthcare.dto.Patient;
import com.example.healthcare.healthcare.dto.UserCreatedResponseEntity;
import com.example.healthcare.healthcare.services.HealthCareServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {
    @Autowired
    HealthCareServices healthCareServices;

    @PostMapping("/doctor")
    public ResponseEntity<?> addDoctor(@RequestBody Doctor doctor){
        Doctor doctorDto = healthCareServices.addDoctor(doctor);
        if(doctorDto != null) {
            return new ResponseEntity<>(new UserCreatedResponseEntity(doctorDto.getId(), Messages.DOCTOR_REGISTRATION_SUCCESSFULL), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new UserCreatedResponseEntity(doctorDto.getId(), Messages.ERROR_OCCURED), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/patient")
    public ResponseEntity<?> addPatient(@RequestBody Patient patient){
        Patient patientDto = healthCareServices.addPatient(patient);
        if(patientDto != null) {
            return new ResponseEntity<>(new UserCreatedResponseEntity(patientDto.getId(), Messages.PATIENT_REGISTRATION_SUCCESSFULL), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new UserCreatedResponseEntity(patientDto.getId(), Messages.ERROR_OCCURED), HttpStatus.BAD_REQUEST);
        }
    }
}
