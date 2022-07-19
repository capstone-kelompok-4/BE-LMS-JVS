package com.alterra.capstoneproject.domain.dto;

import com.alterra.capstoneproject.domain.dao.RequestEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseTakenDto {
    private String email;
    private Long courseId;
    private Long materialId;
    private RequestEnum requestType;
    private String requestDetail;
    private Integer score;
    private String certificateCode;
}
