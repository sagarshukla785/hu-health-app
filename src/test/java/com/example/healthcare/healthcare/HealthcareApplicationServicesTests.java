package com.example.healthcare.healthcare;

import com.example.healthcare.healthcare.dto.*;
import com.example.healthcare.healthcare.exceptions.CaughtException;
import com.example.healthcare.healthcare.repository.AppointmentRepository;
import com.example.healthcare.healthcare.repository.DoctorRepository;
import com.example.healthcare.healthcare.repository.PatientRepository;
import com.example.healthcare.healthcare.services.HealthCareServices;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class HealthcareApplicationServicesTests {

	@MockBean
	DoctorRepository doctorRepository;

	@MockBean
	PatientRepository patientRepository;

	@MockBean
	AppointmentRepository appointmentRepository;

	@Autowired
	HealthCareServices healthCareServices;

	@Test
	void Test_loadUserByUsername_validPatient() {
		String id = "PAT101";
		Patient patient = new Patient();
		patient.setId(id);
		patient.setPassword("password");
		patient.setDateOfBirth(LocalDate.of(1997, 10, 10));
		patient.setEmail("manthan@gmail.com");
		when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
		assertEquals("PAT101", healthCareServices.loadUserByUsername(id).getUsername());
	}

	@Test
	void Test_loadUserByUsername_InvalidPatient() {
		UsernameNotFoundException thrown = Assertions
				.assertThrows(UsernameNotFoundException.class, () -> healthCareServices.loadUserByUsername("PAT1000"), "Patient not found");

		assertEquals("Patient not found", thrown.getMessage());
	}

	@Test
	void Test_loadUserByUsername_validDoctor(){
		String id = "DOC101";
		Doctor doctor = new Doctor();
		doctor.setId(id);
		doctor.setEmail("karhik@gmail.com");
        doctor.setQualification("mbbs");
		doctor.setName("karthik Sharma");
		doctor.setPassword("password");
		doctor.setSpeciality("en");
		doctor.setExperience(10);
		when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
		assertEquals("DOC101", healthCareServices.loadUserByUsername(id).getUsername());
	}

	@Test
	void Test_loadUserByUsername_InvalidDoctor() {
		UsernameNotFoundException thrown = Assertions
				.assertThrows(UsernameNotFoundException.class, () -> healthCareServices.loadUserByUsername("DOC1000"), "Doctor not found");

		assertEquals("Doctor not found", thrown.getMessage());
	}

	@Test
	void Test_addPatient(){
		Patient patient = new Patient();
		patient.setDateOfBirth(LocalDate.of(1997, 10, 10));
		patient.setPassword("password");
		patient.setEmail("sagarshukla@gmail.com");
		patient.setName("sagar_shukla");
		healthCareServices.addPatient(patient);
		verify(patientRepository).save(patient);
	}

	@Test
	void Test_addPatient_without_dob(){
		Patient patient = new Patient();
		patient.setPassword("password");
		patient.setEmail("sagarshukla@gmail.com");
		patient.setName("sagar_shukla");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addPatient(patient));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name", caughtException.getMessage());
	}

	@Test
	void Test_addPatient_without_password(){
		Patient patient = new Patient();
		patient.setDateOfBirth(LocalDate.of(1997, 10, 10));
		patient.setEmail("sagarshukla@gmail.com");
		patient.setName("sagar_shukla");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addPatient(patient));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name", caughtException.getMessage());
	}

	@Test
	void Test_addPatient_without_name(){
		Patient patient = new Patient();
		patient.setDateOfBirth(LocalDate.of(1997, 10, 10));
		patient.setPassword("password");
		patient.setEmail("sagarshukla@gmail.com");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addPatient(patient));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name", caughtException.getMessage());
	}

	@Test
	void Test_addPatient_without_email(){
		Patient patient = new Patient();
		patient.setDateOfBirth(LocalDate.of(1997, 10, 10));
		patient.setPassword("password");
		patient.setName("sagar");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addPatient(patient));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name", caughtException.getMessage());
	}

	@Test
	void Test_addPatient_without_no_parameter(){
		Patient patient = new Patient();
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addPatient(patient));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name", caughtException.getMessage());
	}

	@Test
	void Test_addDoctor(){
		Doctor doctor = new Doctor();
		doctor.setDateOfBirth(LocalDate.of(1997, 10, 10));
		doctor.setPassword("password");
		doctor.setEmail("sagarshukla@gmail.com");
		doctor.setName("sagar_shukla");
		doctor.setExperience(10);
		doctor.setSpeciality("en");
		doctor.setQualification("mbbs");
		healthCareServices.addDoctor(doctor);
		verify(doctorRepository).save(doctor);
	}

	@Test
	void Test_addDoctor_without_dob(){
		Doctor doctor = new Doctor();
		doctor.setPassword("password");
		doctor.setEmail("sagarshukla@gmail.com");
		doctor.setName("sagar_shukla");
		doctor.setExperience(10);
		doctor.setSpeciality("en");
		doctor.setQualification("mbbs");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addDoctor(doctor));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name, qualification, speciality, experience", caughtException.getMessage());
	}

	@Test
	void Test_addDoctor_without_password(){
		Doctor doctor = new Doctor();
		doctor.setDateOfBirth(LocalDate.of(1997, 10, 10));
		doctor.setEmail("sagarshukla@gmail.com");
		doctor.setName("sagar_shukla");
		doctor.setExperience(10);
		doctor.setSpeciality("en");
		doctor.setQualification("mbbs");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addDoctor(doctor));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name, qualification, speciality, experience", caughtException.getMessage());
	}

	@Test
	void Test_addDoctor_without_email(){
		Doctor doctor = new Doctor();
		doctor.setDateOfBirth(LocalDate.of(1997, 10, 10));
		doctor.setPassword("password");
		doctor.setName("sagar_shukla");
		doctor.setExperience(10);
		doctor.setSpeciality("en");
		doctor.setQualification("mbbs");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addDoctor(doctor));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name, qualification, speciality, experience", caughtException.getMessage());
	}

	@Test
	void Test_addDoctor_without_name(){
		Doctor doctor = new Doctor();
		doctor.setDateOfBirth(LocalDate.of(1997, 10, 10));
		doctor.setPassword("password");
		doctor.setEmail("sgar@gmail.com");
		doctor.setExperience(10);
		doctor.setSpeciality("en");
		doctor.setQualification("mbbs");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addDoctor(doctor));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name, qualification, speciality, experience", caughtException.getMessage());
	}

	@Test
	void Test_addDoctor_without_experience(){
		Doctor doctor = new Doctor();
		doctor.setDateOfBirth(LocalDate.of(1997, 10, 10));
		doctor.setEmail("sgar@gmail.com");
		doctor.setSpeciality("en");
		doctor.setQualification("mbbs");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addDoctor(doctor));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name, qualification, speciality, experience", caughtException.getMessage());
	}

	@Test
	void Test_addDoctor_without_qualification(){
		Doctor doctor = new Doctor();
		doctor.setDateOfBirth(LocalDate.of(1997, 10, 10));
		doctor.setEmail("sgar@gmail.com");
		doctor.setSpeciality("en");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addDoctor(doctor));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name, qualification, speciality, experience", caughtException.getMessage());
	}

	@Test
	void Test_addDoctor_without_speciality(){
		Doctor doctor = new Doctor();
		doctor.setDateOfBirth(LocalDate.of(1997, 10, 10));
		doctor.setEmail("sgar@gmail.com");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addDoctor(doctor));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name, qualification, speciality, experience", caughtException.getMessage());
	}

	@Test
	void Test_addDoctor_without_no_parameter(){
		Doctor doctor = new Doctor();
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addDoctor(doctor));
		Assertions.assertEquals("Please enter all the mandatory fields. dataOfBirth, email, password, name, qualification, speciality, experience", caughtException.getMessage());
	}

	@Test
	void Test_getUser_Patient(){
		String id = "PAT101";
		Patient patient = new Patient();
		patient.setId(id);
		patient.setEmail("sagar@gmail.com");
		patient.setPassword("password");
		patient.setDateOfBirth(LocalDate.of(1991, 10, 10));

		when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

		Patient actualPatient = (Patient) healthCareServices.getUser(id);

		assertEquals(id, actualPatient.getId());
	}

	@Test
	void Test_getUser_Doctor(){
		String id = "DOC101";
		Doctor doctor = new Doctor();
		doctor.setId(id);
		doctor.setName("sgar");
		doctor.setEmail("sagar@gmail.com");
		doctor.setPassword("password");
		doctor.setDateOfBirth(LocalDate.of(1991, 10, 10));
		doctor.setExperience(10);
		when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
		Doctor actualDoctor = (Doctor) healthCareServices.getUser(id);
		assertEquals(id, actualDoctor.getId());
	}

	@Test
	void Test_getDoctorsBySpeciality(){
		String speciality = "enn";
		List<Doctor> doctorList = new ArrayList<>();
		Doctor doctor1 = new Doctor();
		doctor1.setName("sagar");
		doctor1.setSpeciality("enn");
		doctor1.setExperience(10);
		doctor1.setEmail("sagar@gmail.com");
		doctor1.setPassword("password");
		doctor1.setQualification("mbbs");

		Doctor doctor2 = new Doctor();
		doctor2.setName("sagar");
		doctor2.setSpeciality("enn");
		doctor2.setExperience(10);
		doctor2.setEmail("sagar@gmail.com");
		doctor2.setPassword("password");
		doctor2.setQualification("mbbs");

		doctorList.add(doctor1);
		doctorList.add(doctor2);

		when(doctorRepository.findBySpeciality(speciality)).thenReturn(doctorList);
		List<DoctorDetailDTO> actualList = healthCareServices.getDoctorsBySpeciality("enn");
		assertEquals(2, actualList.size());
	}

	@Test
	void Test_getDoctorsBySpeciality_no_doctor_exist_with_given_speciality(){
		String speciality = "ennnnn";
		List<Doctor> doctorList = new ArrayList<>();
		Doctor doctor1 = new Doctor();
		doctor1.setName("sagar");
		doctor1.setSpeciality("enn");
		doctor1.setExperience(10);
		doctor1.setEmail("sagar@gmail.com");
		doctor1.setPassword("password");
		doctor1.setQualification("mbbs");

		Doctor doctor2 = new Doctor();
		doctor2.setName("sagar");
		doctor2.setSpeciality("enn");
		doctor2.setExperience(10);
		doctor2.setEmail("sagar@gmail.com");
		doctor2.setPassword("password");
		doctor2.setQualification("mbbs");

		doctorList.add(doctor1);
		doctorList.add(doctor2);

		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.getDoctorsBySpeciality(speciality));
		Assertions.assertEquals("No doctor found with speciality " + speciality, caughtException.getMessage());
	}

	@Test
	void Test_getDoctorsBySpeciality_without_speciality(){
		String specality = null;
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.getDoctorsBySpeciality(null));
		Assertions.assertEquals("Speciality should not be null", caughtException.getMessage());
	}

	@Test
	void Test_addAppointmentDetails(){
		Appointment appointment = new Appointment();
		appointment.setDoctorId("PAT107");
		appointment.setPatientId("DOC111");
		appointment.setPatientName("Akshay patni");
		appointment.setDoctorName("Sahil Singla");
		healthCareServices.addAppointmentDetails(appointment);
		verify(appointmentRepository).save(appointment);
	}

	@Test
	void Test_addAppointmentDetails_without_patientId(){
		Appointment appointment = new Appointment();
		appointment.setDoctorId("PAT107");
		appointment.setPatientName("Akshay patni");
		appointment.setDoctorName("Sahil Singla");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addAppointmentDetails(appointment));
		Assertions.assertEquals("Please enter patient id", caughtException.getMessage());
	}

	@Test
	void Test_addAppointmentDetails_without_doctorId(){
		Appointment appointment = new Appointment();
		appointment.setPatientId("DOC107");
		appointment.setPatientName("Akshay patni");
		appointment.setDoctorName("Sahil Singla");
		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.addAppointmentDetails(appointment));
		Assertions.assertEquals("Please enter doctor id", caughtException.getMessage());
	}

	@Test
	void Test_sendIllnessToDoctor_without_patient_id(){
		PatientDescribingProblemDTO patientDescribingProblemDTO = new PatientDescribingProblemDTO();
		patientDescribingProblemDTO.setDoctorId("DOC107");
		patientDescribingProblemDTO.setIllness("I have headache");

		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.sendIllnessToDoctor(patientDescribingProblemDTO));
		assertEquals("Please enter all the mandatory fields. doctorId, patientId, illness", caughtException.getMessage());
	}

	@Test
	void Test_sendIllnessToDoctor_without_doctor_id(){
		PatientDescribingProblemDTO patientDescribingProblemDTO = new PatientDescribingProblemDTO();
		patientDescribingProblemDTO.setPatientId("PAT111");
		patientDescribingProblemDTO.setIllness("I have headache");

		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.sendIllnessToDoctor(patientDescribingProblemDTO));
		assertEquals("Please enter all the mandatory fields. doctorId, patientId, illness", caughtException.getMessage());
	}

	@Test
	void Test_sendIllnessToDoctor_without_illness(){
		PatientDescribingProblemDTO patientDescribingProblemDTO = new PatientDescribingProblemDTO();
		patientDescribingProblemDTO.setPatientId("PAT111");
		patientDescribingProblemDTO.setDoctorId("DOC107");

		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.sendIllnessToDoctor(patientDescribingProblemDTO));
		assertEquals("Please enter all the mandatory fields. doctorId, patientId, illness", caughtException.getMessage());
	}

	@Test
	void Test_sendIllnessToDoctor_without_sendPrescriptionToPatient_without_patient_id(){
		PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
		prescriptionDTO.setDoctorId("DOC107");
		prescriptionDTO.setDiagnosis("Viral fever");
		prescriptionDTO.setPrescription("Take medicine xyx 1-2-1.");

		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.sendPrescriptionToPatient(prescriptionDTO));
		assertEquals("Please enter all the mandatory fields. doctorId, patientId, diagnosis, prescription", caughtException.getMessage());
	}

	@Test
	void Test_sendIllnessToDoctor_without_sendPrescriptionToPatient_without_doctor_id(){
		PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
		prescriptionDTO.setPatientId("PAT111");
		prescriptionDTO.setDiagnosis("Viral fever");
		prescriptionDTO.setPrescription("Take medicine xyx 1-2-1.");

		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.sendPrescriptionToPatient(prescriptionDTO));
		assertEquals("Please enter all the mandatory fields. doctorId, patientId, diagnosis, prescription", caughtException.getMessage());
	}

	@Test
	void Test_sendIllnessToDoctor_without_sendPrescriptionToPatient_without_diagnosis(){
		PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
		prescriptionDTO.setPatientId("PAT111");
		prescriptionDTO.setDoctorId("DOC107");
		prescriptionDTO.setPrescription("Take medicine xyx 1-2-1.");

		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.sendPrescriptionToPatient(prescriptionDTO));
		assertEquals("Please enter all the mandatory fields. doctorId, patientId, diagnosis, prescription", caughtException.getMessage());
	}

	@Test
	void Test_sendIllnessToDoctor_without_sendPrescriptionToPatient_without_prescription(){
		PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
		prescriptionDTO.setPatientId("PAT111");
		prescriptionDTO.setDoctorId("DOC107");
		prescriptionDTO.setDiagnosis("Viral fever");

		CaughtException caughtException = Assertions.assertThrows(CaughtException.class, () -> healthCareServices.sendPrescriptionToPatient(prescriptionDTO));
		assertEquals("Please enter all the mandatory fields. doctorId, patientId, diagnosis, prescription", caughtException.getMessage());
	}

}
