package com.alterra.capstoneproject.domain.dto;

import java.util.List;

import com.alterra.capstoneproject.domain.dao.RoleEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Register {
    private String name;
    private String email;    
    private String password;    
    private Long specializationId;
    private String phoneNumber;
    private String imageUrl;
    private AddressDto address;
    private List<RoleEnum> roles;
}
