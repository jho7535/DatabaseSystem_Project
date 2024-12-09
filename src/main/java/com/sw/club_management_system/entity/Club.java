package com.sw.club_management_system.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table(name = "club")
public class Club {
    @Id
    @Column(value = "club_id")
    public Integer id;

    @Column(value = "club_name")
    public String clubName;

    @Column(value = "contact_info")
    public String contactInfo;

    @Column(value = "description")
    public String description;

    @Column(value = "supervisor")
    public String supervisor;

}