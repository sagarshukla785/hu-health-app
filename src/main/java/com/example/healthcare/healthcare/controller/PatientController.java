package com.example.healthcare.healthcare.controller;

import com.example.healthcare.healthcare.common.Messages;
import com.example.healthcare.healthcare.dto.*;
import com.example.healthcare.healthcare.services.HealthCareServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

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

    @GetMapping("/listdoctorsbyspeciality")
    public ResponseEntity<?> getDoctors(@RequestBody SpecialitySearchDTO specialitySearchDTO){
        List<DoctorDetailDTO> doctorList = healthCareServices.getDoctorsBySpeciality(specialitySearchDTO.getSpeciality());
        return new ResponseEntity<>(doctorList, HttpStatus.OK);
    }

    @PostMapping("/selectdoctor")
    public ResponseEntity<?> selectDoctor(@RequestBody Appointment appointment){
        healthCareServices.addAppointmentDetails(appointment);
        AcknowledgementEntity acknowledgementEntity = new AcknowledgementEntity();
        acknowledgementEntity.setStatus(HttpStatus.OK);
        acknowledgementEntity.setMessage("Appointment is done");
        return new ResponseEntity<>(acknowledgementEntity, HttpStatus.OK);
    }

    @PostMapping("/describeproblem")
    public ResponseEntity<?> describingProblem(@RequestBody PatientDescribingProblemDTO patientDescribingProblemDTO){
        IllnessEmailDTO illnessEmailDTO = healthCareServices.sendIllnessToDoctor(patientDescribingProblemDTO);
        AcknowledgementEntity acknowledgementEntity = new AcknowledgementEntity();
        acknowledgementEntity.setMessage("Hello Mr. " + illnessEmailDTO.getPatientName() + ". Your appointment has been booked with Dr. " + illnessEmailDTO.getDoctorName() + ".");
        acknowledgementEntity.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(acknowledgementEntity, HttpStatus.OK);
    }
}
