package com.sw.club_management_system.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Setter
@Getter
@Table(name = "document")
public class Document {
    @Id
    @Column(value = "document_id")
    public Integer id;

    @Column(value = "document_name")
    public String documentName;

    @Column(value = "club_id")
    public Club club;

    @Column(value = "file")
    public byte[] file;

    @Column(value = "submission_date")
    public Instant submissionDate;

}