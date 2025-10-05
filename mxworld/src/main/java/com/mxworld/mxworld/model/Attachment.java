package com.mxworld.mxworld.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(name = "attachments")

@Getter
@Setter
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false , columnDefinition = "LONGBLOB")
    private byte[] file;

    @Column(nullable = false)
    private String contentType;
    
}
