package com.alterra.capstoneproject.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alterra.capstoneproject.domain.dto.TokenResponse;
import com.alterra.capstoneproject.domain.dto.UsernamePassword;
import com.alterra.capstoneproject.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody UsernamePassword req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@RequestBody UsernamePassword req) {
        TokenResponse token = authService.generateToken(req);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token.getToken());
        
        return ResponseEntity.ok().headers(responseHeaders).body(token);
    }
}
