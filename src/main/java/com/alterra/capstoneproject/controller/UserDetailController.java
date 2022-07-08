package com.alterra.capstoneproject.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.domain.dto.ChangePassword;
import com.alterra.capstoneproject.domain.dto.Register;
import com.alterra.capstoneproject.service.AuthService;
import com.alterra.capstoneproject.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
@CrossOrigin(origins = "https://capstone-project-4.herokuapp.com")
public class UserDetailController {
    @Autowired
    private AuthService authService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers() {
        try {
            List<User> users = authService.getUsers();
            return ResponseUtil.build("GET USERS", users, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            User user = authService.getUser(id);
            return ResponseUtil.build("GET USER ID " + id, user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getUser(Principal principal) {
        try {
            User user = authService.getUser(principal.getName());
            return ResponseUtil.build("GET LOGGED IN USER DATA", user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } 

    @PutMapping("/edit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUser(Principal principal, @RequestBody Register req) {
        try {
            req.setEmail(principal.getName());
            User user = authService.updateUser(req);
            return ResponseUtil.build("USER UPDATED BY USER", user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserByAdmin(@RequestBody Register req) {
        try {
            User user = authService.updateUser(req);
            return ResponseUtil.build("USER UPDATED BY ADMIN", user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(Principal principal, @RequestBody ChangePassword req) {
        try {
            req.setEmail(principal.getName());
            User user = authService.changePassword(req);
            return ResponseUtil.build("PASSWORD CHANGED", user, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            authService.deleteUser(id);
            return ResponseUtil.build("USER ID " + id + " DELETED", null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
