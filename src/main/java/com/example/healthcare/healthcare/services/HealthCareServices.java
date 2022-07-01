package com.example.healthcare.healthcare.services;

import com.example.healthcare.healthcare.dto.*;
import com.example.healthcare.healthcare.exceptions.CaughtException;
import com.example.healthcare.healthcare.repository.AppointmentRepository;
import com.example.healthcare.healthcare.repository.DoctorRepository;
import com.example.healthcare.healthcare.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.example.healthcare.healthcare.utils.Validations.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthCareServices implements UserDetailsService {
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        if (id != null && !id.trim().isEmpty()) {
            id = id.trim().toUpperCase();

            if (id.startsWith("PAT")) {
                Optional<Patient> patientDTO = patientRepository.findById(id);
                return patientDTO.orElseThrow(() -> new UsernameNotFoundException("Patient not found"));
            } else if (id.startsWith("DOC")) {
                Optional<Doctor> doctorDTO = doctorRepository.findById(id);
                return doctorDTO.orElseThrow(() -> new UsernameNotFoundException("Doctor not found"));
            }
        }
        throw new UsernameNotFoundException("User Id not found");
    }

    public Doctor addDoctor(Doctor doctor){
        checkDoctor(doctor);
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        return doctorRepository.save(doctor);
    }

    public Patient addPatient(Patient patient){
        checkPatient(patient);
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        return patientRepository.save(patient);
    }

    public UserDetails getUser(String id){
        if(id != null) {
            if (id.toUpperCase().contains("PAT")) {
                return patientRepository.findById(id).get();
            } else {
                return doctorRepository.findById(id).get();
            }
        }
        else{
            return null;
        }
    }

    public List<DoctorDetailDTO> getDoctorsBySpeciality(String speciality){
        if(speciality != null){
            List<Doctor> doctorList = doctorRepository.findBySpeciality(speciality);
            if(doctorList.size() != 0) {
                List<DoctorDetailDTO> listOfDoctorDetailsDto = new ArrayList<>();
                for(Doctor doctor : doctorList){
                    DoctorDetailDTO doctorDetailDTO = new DoctorDetailDTO();
                    doctorDetailDTO.setId(doctor.getId());
                    doctorDetailDTO.setEmail(doctor.getEmail());
                    doctorDetailDTO.setSpeciality(doctor.getSpeciality());
                    doctorDetailDTO.setExperience(doctor.getExperience());
                    doctorDetailDTO.setQualification(doctor.getQualification());
                    listOfDoctorDetailsDto.add(doctorDetailDTO);
                }
                return listOfDoctorDetailsDto;
            }
            else{
                throw new CaughtException("No doctor found with speciality " + speciality);
            }
        }
        else{
            throw new CaughtException("Speciality should not be null");
        }
    }

    public Appointment addAppointmentDetails(Appointment appointment){
        if(appointment.getDoctorId() != null && appointment.getPatientId() != null) {
            if (appointment != null) {
                return appointmentRepository.save(appointment);
            } else {
                throw new CaughtException("Please enter patient id and doctor id");
            }
        }
        else if(appointment.getDoctorId() == null){
            throw new CaughtException("Please enter doctor id");
        }
        else{
            throw new CaughtException("Please enter patient id");
        }
    }

    public IllnessEmailDTO sendIllnessToDoctor(PatientDescribingProblemDTO patientDescribingProblemDTO){
        verifyPatientDescribingProblemDTO(patientDescribingProblemDTO);
        Patient patient = (Patient) getUser(patientDescribingProblemDTO.getPatientId());
        Doctor doctor = (Doctor) getUser(patientDescribingProblemDTO.getDoctorId());

        IllnessEmailDTO illnessEmailDTO = new IllnessEmailDTO();
        illnessEmailDTO.setIllness(patientDescribingProblemDTO.getIllness());
        illnessEmailDTO.setDoctorId(patientDescribingProblemDTO.getDoctorId());
        illnessEmailDTO.setPatientId(patientDescribingProblemDTO.getPatientId());
        illnessEmailDTO.setDoctorName(doctor.getName());
        illnessEmailDTO.setPatientName(patient.getName());
        illnessEmailDTO.setDoctorEmail(doctor.getEmail());
        illnessEmailDTO.setPatientEmail(patient.getEmail());

        try {
            kafkaTemplate.send("send-email-doctor", illnessEmailDTO);
            return illnessEmailDTO;
        }
        catch (CaughtException ex){
            throw new CaughtException("Producer not able ");
        }
    }

    public DiagnosisDTO sendPrescriptionToPatient(PrescriptionDTO prescriptionDTO){
        verifyPrescriptionDTO(prescriptionDTO);
        Patient patient = (Patient) getUser(prescriptionDTO.getPatientId());
        Doctor doctor = (Doctor) getUser(prescriptionDTO.getDoctorId());

        DiagnosisDTO diagnosisDTO = new DiagnosisDTO();
        diagnosisDTO.setDiagnosis(prescriptionDTO.getDiagnosis());
        diagnosisDTO.setPrescription(prescriptionDTO.getPrescription());
        diagnosisDTO.setDoctorId(prescriptionDTO.getDoctorId());
        diagnosisDTO.setPatientId(prescriptionDTO.getPatientId());
        diagnosisDTO.setDoctorName(doctor.getName());
        diagnosisDTO.setPatientName(patient.getName());
        diagnosisDTO.setDoctorEmail(doctor.getEmail());
        diagnosisDTO.setPatientEmail(patient.getEmail());

        try {
            kafkaTemplate.send("send-email-patient", diagnosisDTO);
            return diagnosisDTO;
        }
        catch (CaughtException ex){
            throw new CaughtException("Producer not able ");
        }
    }

    public void sendEmail(String toEmail, String body, String subject){
        log.info("Inside sendEmail service.");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("noreplyhealthcare2@gmail.com");
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setText(body);
        simpleMailMessage.setSubject(subject);

        javaMailSender.send(simpleMailMessage);
        log.info("Email has been sent!");
    }

}
