package com.sw.club_management_system.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table(name = "participation")
public class Participation {
    @Column(value = "student_number")
    public com.sw.club_management_system.entity.User studentNumber;

    @Column(value = "schedule_id")
    public com.sw.club_management_system.entity.Schedule schedule;

    @Id
    @Column(value = "student_number")
    public Integer id;

    @Id
    @Column(value = "schedule_id")
    public Integer id1;

}