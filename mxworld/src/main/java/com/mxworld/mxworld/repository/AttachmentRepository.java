package com.mxworld.mxworld.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mxworld.mxworld.model.Attachment;
import java.util.Optional;
import jakarta.persistence.Id;


public interface AttachmentRepository extends JpaRepository<Attachment, String> {
   Optional<Attachment> findById(Id id);
}