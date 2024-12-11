package com.sw.club_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleRequest {

    @NotBlank(message = "Schedule name is required.")
    private String scheduleName; // 스케줄 이름

    @NotNull(message = "Date is required.")
    private LocalDate date; // 날짜

    @NotBlank(message = "Location is required.")
    private String location; // 위치

    private String description; // 스케줄 설명 (선택적)
}
