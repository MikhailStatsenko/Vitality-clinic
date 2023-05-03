package com.vitality.clinic.controller;

import com.vitality.clinic.model.Patient;
import com.vitality.clinic.model.User;
import com.vitality.clinic.service.UserService;
import com.vitality.clinic.utils.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/guest")
public class GuestController {
    private final UserService userService;
    @Autowired
    public GuestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/edit/{guestId}")
    public String editUserPage(@PathVariable long  guestId, Model model) {
        User user = userService.getUserById(guestId);
        model.addAttribute("user", user);
        model.addAttribute("patient", new Patient());
        return "/guest/add-patient-data-to-guest";
    }

    @PostMapping("/edit/{guestId}")
    public String guestToPatient(@ModelAttribute User user,
                                 @ModelAttribute Patient patient,
                                 @PathVariable long guestId) {
        User existingUser = userService.getUserById(guestId);
        existingUser.setFirstName(user.getFirstName());
        existingUser.setMiddleName(user.getMiddleName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPatient(patient);
        existingUser.setRole(UserRole.ROLE_PATIENT);
        patient.setUser(existingUser);
        userService.updateUser(existingUser);
        return "redirect:/auth/login?submit";
    }
}
