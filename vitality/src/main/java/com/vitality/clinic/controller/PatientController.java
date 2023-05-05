package com.vitality.clinic.controller;

import com.vitality.clinic.model.Patient;
import com.vitality.clinic.model.User;
import com.vitality.clinic.service.PatientService;
import com.vitality.clinic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patient")
public class PatientController {
    private final UserService userService;
    private final PatientService patientService;

    @Autowired
    public PatientController(UserService userService, PatientService patientService) {
        this.userService = userService;
        this.patientService = patientService;
    }

    @GetMapping("/edit/{patientId}")
    public String editPatientPage(@PathVariable long patientId, Model model) {
        Patient patient = patientService.getPatientById(patientId).get();
        model.addAttribute("patient", patient);
        model.addAttribute("user", patient.getUser());
        return "/patient/edit-patient";
    }

    @PostMapping("/edit/{patientId}")
    public String editPatient(@ModelAttribute Patient patient,
                              @ModelAttribute User user,
                              @PathVariable long patientId) {
        Patient existingPatient = patientService.getPatientById(patientId).get();
        existingPatient.setDateOfBirth(patient.getDateOfBirth());
        User existingUser = existingPatient.getUser();
        existingUser = userService.updateExistingUserObject(existingUser, user);
        userService.updateUser(existingUser);
        return "redirect:/home";
    }
}
