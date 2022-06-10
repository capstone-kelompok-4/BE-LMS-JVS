package com.alterra.capstoneproject.domain.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseTakenDto {
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private Long courseId;
    private LocalDateTime takenAt;
}
