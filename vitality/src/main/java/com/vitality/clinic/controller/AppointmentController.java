package com.vitality.clinic.controller;

import com.vitality.clinic.service.DoctorService;
import com.vitality.clinic.service.UserService;
import com.vitality.clinic.utils.enums.MedicalSpecialty;
import com.vitality.clinic.utils.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {
    private final UserService userService;
    private final DoctorService doctorService;

    @Autowired
    public AppointmentController(UserService userService, DoctorService doctorService) {
        this.userService = userService;
        this.doctorService = doctorService;
    }

    @GetMapping("/schedule")
    public String globalSchedulePage(@RequestParam MedicalSpecialty speciality, @RequestParam int week, Model model) {
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY).plusDays(7L * week);
        List<LocalDate> datesOfWeek = new ArrayList<>(7);
        for (int i = 0; i < 7; i++)
            datesOfWeek.add(startOfWeek.plusDays(i));

        System.out.println(Arrays.toString(datesOfWeek.toArray()));

        model.addAttribute("dates", datesOfWeek);
        model.addAttribute("doctors", speciality.equals(MedicalSpecialty.ANY) ?
                        doctorService.getAllDoctors() : doctorService.getDoctorBySpeciality(speciality));
        return "/appointment/schedule";
    }

    @GetMapping("/schedule/{doctorId}/{date}")
    public String doctorSchedulePage(@PathVariable long doctorId,
                                     @PathVariable LocalDate date, Model model) {
        model.addAttribute("doctor", doctorService.getDoctorById(doctorId));
        model.addAttribute("date", date);
        return "/appointment/doctor-schedule";
    }

}
