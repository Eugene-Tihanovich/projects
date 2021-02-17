package com.tihanovich.springwebapp.controller;

import com.tihanovich.springwebapp.model.User;
import com.tihanovich.springwebapp.model.UserStatus;
import com.tihanovich.springwebapp.repository.UserRepository;
import com.tihanovich.springwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping()
    public String homePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        if (UserStatus.BLOCK.toString().equals(user.getStatus())) {
            return "redirect:/login";
        }
        model.addAttribute("users", userService.findAllUsers());
        return "home";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, params = "action=delete")
    public String delete(@RequestParam(required=false, name = "checkboxId") List<String> usersId, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        for (User user: userService.findAllUsers()) {
            if (user.getName().equals(name) &&
                    UserStatus.BLOCK.toString().equals(user.getStatus())){
                session.invalidate();
                return "redirect:/login";
            }
        }
        User user = userService.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        if (usersId != null) {
            for (String id : usersId) {
                userService.deleteUser(Integer.parseInt(id));
                if (String.valueOf(user.getId()).equals(id)) {
                    session.invalidate();
                    return "redirect:/login";
                }
            }
        }
        else {
            return "redirect:/";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, params = "action=block")
    public String blockUser(@RequestParam(required=false, name = "checkboxId") List<String> usersId, HttpSession session) {
        if (userService.checkBlock() >=1){
            session.invalidate();
            return "redirect:/login";
        }

        if (usersId != null) {
            for (String id : usersId) {
                User user = userService.findUserById(Integer.parseInt(id))
                        .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
                user.setStatus(UserStatus.BLOCK.toString());
                userRepository.save(user);
            }
        }
        else {
            return "redirect:/";
        }


        return "redirect:/";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, params = "action=unblock")
    public String unblockUser(@RequestParam(required=false, name = "checkboxId") List<String> usersId, HttpSession session) {
        if (userService.checkBlock() >=1){
                session.invalidate();
                return "redirect:/login";
            }

        if (usersId != null) {
            for (String id : usersId) {
                User user = userService.findUserById(Integer.parseInt(id))
                        .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
                user.setStatus(UserStatus.UNBLOCK.toString());
                userRepository.save(user);
            }
        }
        else{
            return "redirect:/";
        }
        return "redirect:/";
    }
}