package com.alterra.capstoneproject.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alterra.capstoneproject.domain.dto.TokenResponse;
import com.alterra.capstoneproject.domain.dto.Login;
import com.alterra.capstoneproject.domain.dto.Register;
import com.alterra.capstoneproject.service.AuthService;
import com.alterra.capstoneproject.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;

    @PostMapping(value = "/api/auth/signup")
    public ResponseEntity<?> register (@RequestBody Register req) {
        try {
            authService.register(req);
            return ResponseUtil.build("USER CREATED", req, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/api/auth/signin")
    public ResponseEntity<?> generateToken(@RequestBody Login req) {
        TokenResponse token = authService.generateToken(req);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token.getToken());
        
        return ResponseEntity.ok().headers(responseHeaders).body(token);
    }

    
}
