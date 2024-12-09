package com.sw.club_management_system.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Setter
@Getter
@Table(name = "schedule")
public class Schedule {
    @Id
    @Column(value = "schedule_id")
    public Integer id;

    @Column(value = "club_id")
    @NotNull
    public Integer clubId;

    @Column(value = "schedule_name")
    @NotNull
    public String scheduleName;

    @Column(value = "date")
    public LocalDate date;

    @Column(value = "location")
    public String location;

    @Column(value = "description")
    public String description;

}