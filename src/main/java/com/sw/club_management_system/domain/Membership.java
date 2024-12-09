package com.sw.club_management_system.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table(name = "membership")
public class Membership {
    @Column(value = "student_number")
    public Integer studentNumber;

    @Column(value = "club_id")
    public Integer clubId;

    @Column(value = "role")
    public String role;

}