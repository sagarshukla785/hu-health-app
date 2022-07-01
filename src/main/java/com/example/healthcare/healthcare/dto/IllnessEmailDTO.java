package com.example.healthcare.healthcare.dto;

import lombok.Data;

@Data
public class IllnessEmailDTO {
    private String patientId;
    private String patientName;
    private String patientEmail;
    private String illness;
    private String doctorId;
    private String doctorName;
    private String doctorEmail;
}
