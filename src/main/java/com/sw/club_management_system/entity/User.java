package com.sw.club_management_system.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table(name = "user")
public class User {
    @Id
    @Column(value = "student_number")
    public Integer id;

    @Column(value = "email")
    public String email;

    @Column(value = "password")
    public String password;

    @Column(value = "department")
    public String department;

}