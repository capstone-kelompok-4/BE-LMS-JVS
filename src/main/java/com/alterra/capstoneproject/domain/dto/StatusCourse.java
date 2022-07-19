package com.alterra.capstoneproject.domain.dto;

import com.alterra.capstoneproject.domain.dao.StatusEnum;

import lombok.Data;

@Data
public class StatusCourse {
    private StatusEnum status;
    private String certificateCode;
}
