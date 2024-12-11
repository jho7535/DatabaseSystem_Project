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
@Table(name = "documentId")
public class Document {
    @Id
    @Column(value = "document_id")
    public Integer id;

    @Column(value = "document_name")
    @NotNull
    public String documentName;

    @Column(value = "club_id")
    public Integer clubId;

    @Column(value = "file")
    public String file;

    @Column(value = "submission_date")
    public Instant submissionDate;

}