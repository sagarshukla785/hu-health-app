package com.example.healthcare.healthcare.controller;

import com.example.healthcare.healthcare.common.Messages;
import com.example.healthcare.healthcare.dto.*;
import com.example.healthcare.healthcare.services.HealthCareServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {
    @Autowired
    HealthCareServices healthCareServices;

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestBody SearchDTO searchDTO){
        UserDetails userDetails = healthCareServices.getUser(searchDTO.getUserId());
        if(userDetails != null ){
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new UserCreatedResponseEntity(userDetails.getUsername(), Messages.ERROR_OCCURED), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/prescription")
    public ResponseEntity<?> sendPrescription(@RequestBody PrescriptionDTO prescriptionDTO){
        DiagnosisDTO diagnosisDTO = healthCareServices.sendPrescriptionToPatient(prescriptionDTO);
        AcknowledgementEntity acknowledgementEntity = new AcknowledgementEntity();
        acknowledgementEntity.setMessage("Email sent to Mr. " + diagnosisDTO.getPatientName());
        acknowledgementEntity.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(acknowledgementEntity, HttpStatus.OK);
    }
}
