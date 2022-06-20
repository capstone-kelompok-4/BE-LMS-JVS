package com.alterra.capstoneproject.domain.dto;

import java.util.List;

import javax.persistence.Column;

import com.alterra.capstoneproject.domain.dao.RoleEnum;

import lombok.Data;

@Data
public class Register {
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private List<RoleEnum> roles;
}
