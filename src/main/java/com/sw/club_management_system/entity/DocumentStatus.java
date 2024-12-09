package com.sw.club_management_system.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Setter
@Getter
@Table(name = "document_status")
public class DocumentStatus {
    @Id
    @Column(value = "document_id")
    public Integer id;

    @Column(value = "document_id")
    public Document document;

    @Column(value = "status")
    public String status;

    @Column(value = "status_modified_date")
    public Instant statusModifiedDate;

}