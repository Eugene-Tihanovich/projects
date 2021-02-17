package com.tihanovich.springwebapp.service;

import com.tihanovich.springwebapp.model.User;
import com.tihanovich.springwebapp.model.UserStatus;
import com.tihanovich.springwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    public int checkBlock() {
        int counter = 0;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        for (User user : userRepository.findAll()) {
            if (user.getName().equals(name) &&
                    UserStatus.BLOCK.toString().equals(user.getStatus())) {
                counter = counter + 1;
            }
        }
        return counter;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authenticationSuccessEvent) {
        String name = authenticationSuccessEvent.getAuthentication().getName();
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        user.setLoginDate(dateFormat.format(new Date()));
        userRepository.save(user);
    }
}