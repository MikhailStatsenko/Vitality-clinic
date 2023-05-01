package com.vitality.clinic.controller;

import com.vitality.clinic.model.Doctor;
import com.vitality.clinic.model.Patient;
import com.vitality.clinic.model.User;
import com.vitality.clinic.service.DoctorService;
import com.vitality.clinic.service.PatientService;
import com.vitality.clinic.service.RegistrationService;
import com.vitality.clinic.service.UserService;
import com.vitality.clinic.utils.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final RegistrationService registrationService;

    @Autowired
    public AdminController(UserService userService, DoctorService doctorService,
                           RegistrationService registrationService, PatientService patientService) {
        this.userService = userService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.registrationService = registrationService;
    }

    @GetMapping("")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin/admin";
    }

    @GetMapping("/patient/new")
    public String addPatientPage() {
        return "/admin/new-patient";
    }

    @PostMapping("/patient/new")
    public String addPatient(@ModelAttribute Patient patient,
                             @ModelAttribute User user) {
        registrationService.register(user);
        user.setRole(UserRole.ROLE_PATIENT);
        patient.setUser(user);
        patientService.addPatient(patient);
        return "redirect:/admin";
    }

    @GetMapping("/doctor/new")
    public String addDoctorPage() {
        return "/admin/new-doctor";
    }

    @PostMapping("/doctor/new")
    public String addDoctor(@ModelAttribute Doctor doctor,
                            @ModelAttribute User user) {
        registrationService.register(user);
        user.setRole(UserRole.ROLE_DOCTOR);
        doctor.setUser(user);
        doctorService.addDoctor(doctor);
        return "redirect:/admin";
    }

    @GetMapping("/user/edit/{userId}")
    public String editUserPage(@PathVariable long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "/admin/edit-user";
    }

    @PostMapping("/user/edit/{userId}")
    public String editUser(@ModelAttribute User user,
                           @PathVariable long userId) {
        User existingUser = userService.getUserById(userId);
        existingUser = userService.updateExistingUserObject(existingUser, user);
        userService.updateUser(existingUser);
        return "redirect:/admin";
    }

    @GetMapping("/patient/edit/{patientId}")
    public String editPatientPage(@PathVariable long patientId, Model model) {
        Patient patient = patientService.getPatientById(patientId).get();
        model.addAttribute("patient", patient);
        model.addAttribute("user", patient.getUser());
        return "/admin/edit-patient";
    }

    @PostMapping("/patient/edit/{patientId}")
    public String editPatient(@ModelAttribute Patient patient,
                              @ModelAttribute User user,
                              @PathVariable long patientId) {
        Patient existingPatient = patientService.getPatientById(patientId).get();
        existingPatient.setDateOfBirth(patient.getDateOfBirth());
        User existingUser = existingPatient.getUser();
        existingUser = userService.updateExistingUserObject(existingUser, user);
        userService.updateUser(existingUser);
        return "redirect:/admin";
    }

    @GetMapping("/doctor/edit/{doctorId}")
    public String editDoctorPage(@PathVariable long doctorId, Model model) {
        Doctor doctor = doctorService.getDoctorById(doctorId).get();
        model.addAttribute("doctor", doctor);
        model.addAttribute("user", doctor.getUser());
        return "/admin/edit-doctor";
    }

    @PostMapping("/doctor/edit/{doctorId}")
    public String editDoctor(@ModelAttribute Doctor doctor,
                              @ModelAttribute User user,
                              @PathVariable long doctorId) {
        Doctor existingDoctor = doctorService.getDoctorById(doctorId).get();
        existingDoctor.setSpeciality(doctor.getSpeciality());
        User existingUser = existingDoctor.getUser();
        existingUser = userService.updateExistingUserObject(existingUser, user);
        userService.updateUser(existingUser);
        return "redirect:/admin";
    }

    @PostMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable long userId) {
        userService.deleteUserById(userId);
        return "redirect:/admin";
    }
}
