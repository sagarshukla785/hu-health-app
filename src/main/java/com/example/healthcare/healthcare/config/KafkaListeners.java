package com.example.healthcare.healthcare.config;

import com.example.healthcare.healthcare.dto.DiagnosisDTO;
import com.example.healthcare.healthcare.dto.IllnessEmailDTO;
import com.example.healthcare.healthcare.services.HealthCareServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaListeners {

    @Autowired
    private HealthCareServices healthCareServices;

    @KafkaListener(topics = "send-email-doctor", groupId = "patient")
    void listener(String data) throws JsonProcessingException {
        log.info("Inside kafka listener.");
        ObjectMapper objectMapper = new ObjectMapper();
        IllnessEmailDTO illnessEmailDTO = objectMapper.readValue(data, IllnessEmailDTO.class);
        String doctorEmailBody = "Hello Dr. " + illnessEmailDTO.getDoctorName() + ","+ "\n\nYour appointment has been confirmed with " + "Mr. " + illnessEmailDTO.getPatientName() +".\n\nPlease find the patient details below:\n\nIllness: " + illnessEmailDTO.getIllness() + "\nEmail: " + illnessEmailDTO.getPatientEmail() + "\n\nThanks,\nHealthCareTeam";
        String doctorEmailSubject = "Appointment booked with Mr. " + illnessEmailDTO.getPatientName();
        log.info("Going to call email service...");
        healthCareServices.sendEmail(illnessEmailDTO.getDoctorEmail(), doctorEmailBody, doctorEmailSubject);

        String patientEmailSubject = "Appointment booked with Dr. " + illnessEmailDTO.getDoctorName();
        String patientEmailBody = "Hello Mr. " + illnessEmailDTO.getPatientName() + "," + "\n\nYour appointment has been booked with Mr. " + illnessEmailDTO.getDoctorName() + "." + "\n\nYou can reach out to him at " + illnessEmailDTO.getDoctorEmail() + "\n\nThanks,\nHealthCareTeam";
        log.info("Going to call email service...");
        healthCareServices.sendEmail(illnessEmailDTO.getPatientEmail(), patientEmailBody, patientEmailSubject);
    }

    @KafkaListener(topics = "send-email-patient", groupId = "patient")
    void listener2(String data) throws JsonProcessingException {
        log.info("Inside kafka listener.");
        ObjectMapper objectMapper = new ObjectMapper();
        DiagnosisDTO diagnosisDTO = objectMapper.readValue(data, DiagnosisDTO.class);

        String patientEmailSubject = "Update!!! Appointment with Dr. " + diagnosisDTO.getDoctorName() + " | Diagnosis report.";
        String patientEmailBody = "Hello Mr. " + diagnosisDTO.getPatientName() + "," + "\n\nPlease find your diagnosis report below:" + "\n\nDiagnosis: " + diagnosisDTO.getDiagnosis() + "\nPrescription: " + diagnosisDTO.getPrescription() + "\n\nYou can reach out to him at " + diagnosisDTO.getDoctorEmail() + "\n\nThanks,\nHealthCareTeam";
        log.info("Going to call email service...");
        healthCareServices.sendEmail(diagnosisDTO.getPatientEmail(), patientEmailBody, patientEmailSubject);

        String doctorEmailSubject = "Update!!! Appointment with Mr. " + diagnosisDTO.getPatientName() + " | Diagnosis report sent.";
        String doctorEmailBody = "Hello Dr. " + diagnosisDTO.getDoctorName() + "\n\nDiagnosis report has been sent to Mr. " + diagnosisDTO.getPatientName() + "\n\nYou can reach out to him at " + diagnosisDTO.getPatientEmail() + "\n\nThanks,\nHealthCareTeam";
        log.info("Going to call email service...");
        healthCareServices.sendEmail(diagnosisDTO.getDoctorEmail(), doctorEmailBody, doctorEmailSubject);
    }
}
