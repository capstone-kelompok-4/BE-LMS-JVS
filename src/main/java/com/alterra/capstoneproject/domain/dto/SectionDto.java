package com.alterra.capstoneproject.domain.dto;

import javax.persistence.Column;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SectionDto {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long courseId;
}
