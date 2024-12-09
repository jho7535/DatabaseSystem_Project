package com.sw.club_management_system.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Table(name = "admin")
public class Admin {
    @Id
    public String email;

    @Column(value = "password")
    @NotNull
    public String password;

}