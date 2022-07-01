package com.example.healthcare.healthcare.dto;

import lombok.Data;

@Data
public class PrescriptionDTO {
    private String patientId;
    private String doctorId;
    private String diagnosis;
    private String prescription;
}
