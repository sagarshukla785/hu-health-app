package com.example.healthcare.healthcare.dto;

import lombok.Data;

@Data
public class DiagnosisDTO {
    private String patientId;
    private String patientName;
    private String patientEmail;
    private String diagnosis;
    private String prescription;
    private String doctorId;
    private String doctorName;
    private String doctorEmail;
}
