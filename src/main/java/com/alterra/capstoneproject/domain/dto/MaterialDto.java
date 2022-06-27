package com.alterra.capstoneproject.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MaterialDto {
    private String name;
    private String type;
    private String url;
    private Long courseId;
    private Long sectionId;
}
