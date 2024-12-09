package com.sw.club_management_system.entity;

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
    public Club club;

    @Column(value = "schedule_name")
    public String scheduleName;

    @Column(value = "date")
    public LocalDate date;

    @Column(value = "location")
    public String location;

    @Column(value = "description")
    public String description;

}