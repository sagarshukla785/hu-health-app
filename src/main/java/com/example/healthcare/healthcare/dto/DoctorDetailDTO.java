package com.example.healthcare.healthcare.dto;

import lombok.Data;

@Data
public class DoctorDetailDTO {
    private String id;
    private String email;
    private String qualification;
    private String speciality;
    private int experience;
}
