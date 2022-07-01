package com.example.healthcare.healthcare.utils;

import com.example.healthcare.healthcare.dto.Doctor;
import com.example.healthcare.healthcare.dto.Patient;
import com.example.healthcare.healthcare.dto.PatientDescribingProblemDTO;
import com.example.healthcare.healthcare.dto.PrescriptionDTO;
import com.example.healthcare.healthcare.exceptions.CaughtException;

public class Validations {
    public static boolean checkDoctor(Doctor doctor){
        if(doctor.getEmail() != null && doctor.getDateOfBirth() != null && doctor.getPassword() != null && doctor.getName() != null && doctor.getQualification() != null && doctor.getSpeciality() != null && doctor.getExperience() != 0){
            return true;
        }
        else {
            throw new CaughtException("Please enter all the mandatory fields. dataOfBirth, email, password, name, qualification, speciality, experience");
        }
    }

    public static boolean checkPatient(Patient patient){
        if(patient.getEmail() != null && patient.getDateOfBirth() != null && patient.getPassword() != null && patient.getName() != null){
            return true;
        }
        else {
            throw new CaughtException("Please enter all the mandatory fields. dataOfBirth, email, password, name");
        }
    }

    public static boolean verifyPatientDescribingProblemDTO(PatientDescribingProblemDTO patientDescribingProblemDTO){
        if(patientDescribingProblemDTO.getPatientId() != null && patientDescribingProblemDTO.getDoctorId() != null && patientDescribingProblemDTO.getIllness() != null){
            return true;
        }
        else{
            throw new CaughtException("Please enter all the mandatory fields. doctorId, patientId, illness");
        }
    }

    public static boolean verifyPrescriptionDTO(PrescriptionDTO prescriptionDTO){
        if(prescriptionDTO.getPrescription() != null && prescriptionDTO.getDoctorId() != null && prescriptionDTO.getDiagnosis() != null && prescriptionDTO.getPatientId() != null){
            return true;
        }
        else{
            throw new CaughtException("Please enter all the mandatory fields. doctorId, patientId, diagnosis, prescription");
        }
    }
}