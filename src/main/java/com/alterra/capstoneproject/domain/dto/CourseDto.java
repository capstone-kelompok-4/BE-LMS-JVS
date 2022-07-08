package com.alterra.capstoneproject.domain.dto;

import java.util.List;

import com.alterra.capstoneproject.domain.dao.MethodologyEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseDto {
    private String name;
    private String bannerUrl;
    private String description;
    private List<String> targetLearner;
    private List<String> objectiveLearner;
    private List<MethodologyEnum> methodologyLearnings;
    private Long specializationId;
}
