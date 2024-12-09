package com.sw.club_management_system.domain;

import jakarta.validation.constraints.NotNull;
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
    public Integer studentNumber;

    @Column(value = "email")
    @NotNull
    public String email;

    @Column(value = "password")
    @NotNull
    public String password;

    @Column(value = "department")
    @NotNull
    public String department;

}