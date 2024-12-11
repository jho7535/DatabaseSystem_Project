package com.sw.club_management_system.domain;

import jakarta.validation.constraints.NotNull;
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
    public Integer documentId;

    @Column(value = "status")
    @NotNull
    public String status;

    @Column(value = "status_modified_date")
    public Instant statusModifiedDate;

}