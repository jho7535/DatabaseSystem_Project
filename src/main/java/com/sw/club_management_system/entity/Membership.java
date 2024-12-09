package com.sw.club_management_system.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table(name = "membership")
public class Membership {
    @Column(value = "student_number")
    public com.sw.club_management_system.entity.User studentNumber;

    @Column(value = "club_id")
    public Club club;

    @Id
    @Column(value = "student_number")
    public Integer id;

    @Id
    @Column(value = "club_id")
    public Integer id1;

    @Column(value = "role")
    public String role;

}