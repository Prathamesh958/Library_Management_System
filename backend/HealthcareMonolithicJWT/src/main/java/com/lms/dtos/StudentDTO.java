package com.lms.dtos;

import com.lms.entities.Status;

import lombok.Data;

@Data
public class StudentDTO {
    private Long userId;
    private String name;
    private String email;
    private String rollNo;
    private String course;
    private int year;
    private Status status;
}