package com.alterra.capstoneproject.domain.dto;

import javax.persistence.Column;

import lombok.Data;

@Data
public class SpecializationDto {
    @Column(nullable = false)
    private String name;
}
