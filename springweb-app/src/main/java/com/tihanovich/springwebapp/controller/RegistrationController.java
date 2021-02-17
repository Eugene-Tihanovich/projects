package com.tihanovich.springwebapp.controller;

import com.tihanovich.springwebapp.model.User;
import com.tihanovich.springwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;


    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (!user.getPassword().equals(user.getRepeatPassword())) {
            bindingResult.rejectValue("password", "", "Your passwords do not match");
            return "registration";
        }

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        user.setRegistrationDate(dateFormat.format(new Date()));
        userService.saveUser(user);
        return "redirect:/";
    }
}