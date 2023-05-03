package com.vitality.clinic.service;

import com.vitality.clinic.model.Patient;
import com.vitality.clinic.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(long id) {
        return patientRepository.findById(id);
    }

    public List<Patient> getPatientsByDateOfBirth(Date dateOfBirth) {
        return patientRepository.findAllByDateOfBirth(dateOfBirth);
    }

    public List<Patient> getPatientsBornAfterDate(Date dateOfBirth) {
        return patientRepository.findAllByDateOfBirthAfter(dateOfBirth);
    }

    public List<Patient> getPatientsBornBeforeDate(Date dateOfBirth) {
        return patientRepository.findAllByDateOfBirthBefore(dateOfBirth);
    }

    public Optional<Long> addPatient(Patient patient) {
        if (patient != null)
            return Optional.of(patientRepository.save(patient).getId());
        return Optional.empty();
    }

    public void deletePatientById(long id) {
        patientRepository.deleteById(id);
    }

    public void updatePatient(Patient patient) {
        patientRepository.save(patient);
    }
}
