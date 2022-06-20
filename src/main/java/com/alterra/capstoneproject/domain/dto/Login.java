package com.alterra.capstoneproject.domain.dto;

import javax.persistence.Column;

import lombok.Data;

@Data
public class Login {
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
}