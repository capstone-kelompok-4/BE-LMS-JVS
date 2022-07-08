package com.alterra.capstoneproject.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressDto {
    private String detailAddress;
    private String country;
    private String stateProvince;
    private String city;
    private String zipCode;
    private String email;
}
