package com.mxworld.mxworld.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "lectures")
@Data
public class Lecture {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String lectureName;

    @Column(nullable = false)
    private String lectureContent;

    @ManyToOne
    @JoinColumn(name = "module_id" , nullable = false)
    private Module module;
    

}
