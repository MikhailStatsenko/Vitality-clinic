package com.vitality.clinic.service;

import com.vitality.clinic.model.Appointment;
import com.vitality.clinic.model.Doctor;
import com.vitality.clinic.model.DoctorSchedule;
import com.vitality.clinic.model.Patient;
import com.vitality.clinic.repository.AppointmentRepository;
import com.vitality.clinic.repository.DoctorRepository;
import com.vitality.clinic.repository.DoctorScheduleRepository;
import com.vitality.clinic.repository.PatientRepository;
import com.vitality.clinic.utils.enums.AppointmentStatus;
import com.vitality.clinic.utils.enums.DayAvailability;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class AppointmentService {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

    public AppointmentService(DoctorRepository doctorRepository,
                              PatientRepository patientRepository,
                              AppointmentRepository appointmentRepository,
                              DoctorScheduleRepository doctorScheduleRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorScheduleRepository = doctorScheduleRepository;
    }


    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByDoctorAndDate(Doctor doctor, LocalDate date) {
        return appointmentRepository.findAllByDoctorAndDate(doctor, date);
    }

    public Optional<Appointment> getAppointmentById(long id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        return appointmentRepository.findAllByDoctor(doctor);
    }

    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        return appointmentRepository.findAllByPatient(patient);
    }

    public List<Appointment> getAppointmentsAfterDateForDoctor(Doctor doctor, LocalDate date) {
        return appointmentRepository.findAllByDoctorAndDateAfter(doctor, date);
    }

    public long addAppointment(
            long doctorId, long patientId, LocalDate date, LocalTime startTime) {

        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(
                () -> new EntityNotFoundException("Doctor not found"));
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new EntityNotFoundException("Patient not found"));

        Appointment appointment = new Appointment(doctor, patient, date, startTime);
        appointment.setStatus(AppointmentStatus.ACTIVE);
        doctor.addAppointment(appointment);
        patient.addAppointment(appointment);

        return appointmentRepository.save(appointment).getId();
    }

    public void deleteAppointmentById(long id) {
        Appointment appointment = appointmentRepository.getById(id);
        appointment.getDoctor().removeAppointment(appointment);
        appointment.getPatient().removeAppointment(appointment);
        appointmentRepository.delete(appointment);
    }

    public long updateAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment).getId();
    }

    public List<LocalDate> getDatesOfWeek(int weekFromNow) {
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).plusDays(7L * weekFromNow);
        List<LocalDate> datesOfWeek = new ArrayList<>(7);
        for (int i = 0; i < 7; i++)
            datesOfWeek.add(startOfWeek.plusDays(i));
        return datesOfWeek;
    }

    public List<LocalTime> getAvailableTimeForDoctorByDate(Doctor doctor, LocalDate date) {
        Optional<DoctorSchedule> scheduleOptional =
                doctorScheduleRepository.getByDoctorAndDayOfWeek(doctor, date.getDayOfWeek());
        if (scheduleOptional.isEmpty())
            return null;

        DoctorSchedule schedule = scheduleOptional.get();
        LocalTime start = schedule.getWorkdayStart();
        LocalTime end = schedule.getWorkdayEnd();
        int duration = doctor.getAppointmentDuration();

        List<LocalTime> availableTime = new ArrayList<>();
        while (start.isBefore(end)) {
            availableTime.add(start);
            start = start.plusMinutes(duration);
        }

        List<Appointment> appointments =
                appointmentRepository.findAllByDoctorAndDateAndStatus(doctor, date, AppointmentStatus.ACTIVE);
        for (Appointment appointment : appointments) {
            availableTime.remove(appointment.getStartTime());
        }
        return availableTime;
    }

    public Map<Long, Map<LocalDate, DayAvailability>> getAvailabilityMap(List<Doctor> doctors, List<LocalDate> dates){
        Map<Long, Map<LocalDate, DayAvailability>> availabilityMap = new HashMap<>();
        for (Doctor doctor : doctors) {
            Map<LocalDate, DayAvailability> dateAvailabilityMap = new HashMap<>();
            for (LocalDate date : dates) {
                List<LocalTime> availableTime = this.getAvailableTimeForDoctorByDate(doctor, date);
                dateAvailabilityMap.put(date, availableTime == null ? DayAvailability.NOT_IN_SCHEDULE :
                        availableTime.size() == 0 ? DayAvailability.BUSY : DayAvailability.AVAILABLE);
            }
            availabilityMap.put(doctor.getId(), dateAvailabilityMap);
        }
        return availabilityMap;
    }
}
