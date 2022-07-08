package com.alterra.capstoneproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alterra.capstoneproject.domain.dto.TokenResponse;
import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.domain.dto.Login;
import com.alterra.capstoneproject.domain.dto.Register;
import com.alterra.capstoneproject.service.AuthService;
import com.alterra.capstoneproject.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://capstone-project-4.herokuapp.com")
public class UserController {
    private final AuthService authService;

    @PostMapping(value = "/api/auth/signup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> register (@RequestBody Register req) {
        try {
            User user = authService.register(req);
            return ResponseUtil.build("USER CREATED", user, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/api/auth/signin")
    public ResponseEntity<?> generateToken(@RequestBody Login req) {
        try {
            TokenResponse token = authService.generateToken(req);        
            return ResponseUtil.build("TOKEN CREATED", token, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
