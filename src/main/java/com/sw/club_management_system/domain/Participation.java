package com.sw.club_management_system.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "participation")
public class Participation {

    @Column("student_number")
    private Integer studentNumber;

    @Column("schedule_id")
    private Integer scheduleId;

}