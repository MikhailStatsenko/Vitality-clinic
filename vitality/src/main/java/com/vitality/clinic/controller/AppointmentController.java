package com.vitality.clinic.controller;

import com.vitality.clinic.model.Appointment;
import com.vitality.clinic.model.Doctor;
import com.vitality.clinic.service.AppointmentService;
import com.vitality.clinic.service.DoctorService;
import com.vitality.clinic.service.PatientService;
import com.vitality.clinic.utils.enums.AppointmentStatus;
import com.vitality.clinic.utils.enums.MedicalSpecialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(DoctorService doctorService,
                                 PatientService patientService,
                                 AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/schedule")
    public String globalSchedulePage(@RequestParam(required = false) MedicalSpecialty speciality,
                                     @RequestParam int week, Model model) {
        List<LocalDate> dates = appointmentService.getDatesOfWeek(week);
        List<Doctor> doctors = speciality == null ?
                doctorService.getAllDoctors() : doctorService.getDoctorsBySpeciality(speciality);

        model.addAttribute("dates", dates);
        model.addAttribute("doctors", doctors);
        model.addAttribute("availabilityMap", appointmentService.getAvailabilityMap(doctors, dates));
        model.addAttribute("specialties", MedicalSpecialty.values());
        model.addAttribute("tomorrow", LocalDate.now().plusDays(1));
        return "/appointment/schedule";
    }

    @GetMapping("/schedule/{doctorId}/{date}")
    public String doctorSchedulePage(@PathVariable long doctorId,
                                     @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, Model model,
                                     @ModelAttribute Appointment appointment) {
        Doctor doctor = doctorService.getDoctorById(doctorId).get();
        List<LocalTime> availableTime = appointmentService.getAvailableTimeForDoctorByDate(doctor, date);

        model.addAttribute("available_time", availableTime);
        model.addAttribute("doctor", doctor);
        model.addAttribute("date", date);
        return "/appointment/doctor-schedule";
    }

    @PostMapping( "/new")
    public String makeAppointment(@RequestParam long doctorId,
                                  @RequestParam long patientId,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                  @RequestParam LocalTime startTime) {
        appointmentService.addAppointment(doctorId, patientId, date, startTime);
        return "redirect:/home";
    }

    @PostMapping("/cancel")
    public String cancelAppointment(@RequestParam long appointmentId,
                                    @RequestParam long patientId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId).get();
        appointment.setStatus(AppointmentStatus.CANCELED);
        appointmentService.updateAppointment(appointment);
        return "redirect:/patient/appointments/" + patientId;
    }
}
