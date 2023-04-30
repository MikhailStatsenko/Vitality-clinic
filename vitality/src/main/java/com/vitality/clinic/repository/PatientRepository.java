package com.vitality.clinic.repository;

import com.vitality.clinic.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAllByDateOfBirth(Date dateOfBirth);
    List<Patient> findAllByDateOfBirthAfter(Date dateOfBirth);
    List<Patient> findAllByDateOfBirthBefore(Date dateOfBirth);
    List<Patient> findAllByLastNameAndFirstNameAndMiddleName(String lastName, String firstName, String middleName);
}
