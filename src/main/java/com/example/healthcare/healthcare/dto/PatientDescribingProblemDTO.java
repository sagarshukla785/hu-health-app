package com.example.healthcare.healthcare.dto;

import lombok.Data;

@Data
public class PatientDescribingProblemDTO {
    private String patientId;
    private String doctorId;
    private String illness;
}
