package com.mxworld.mxworld.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mxworld.mxworld.interfaces.AttachmmentInterface;
import com.mxworld.mxworld.model.Attachment;
import com.mxworld.mxworld.repository.AttachmentRepository;
import com.mxworld.mxworld.syntax.ApiResponseDto;

@Service
public class AttachmentService implements AttachmmentInterface {

    private final AttachmentRepository attachmentRepository;
    private final String BASE_URL = "http://localhost:8080/mxworld/v1/attachment/files/";

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public ApiResponseDto<Map<String, String>> uploadFile(String img) {

        if (img == null || img.isEmpty()) {
            return new ApiResponseDto<>(400, "No data provided", new HashMap<>());
        }

        if (!img.contains(",")) {
            return new ApiResponseDto<>(400, "Invalid Base64 format: missing comma", new HashMap<>());
        }

        String[] parts = img.split(",");
        String mimeType = parts[0].substring(parts[0].indexOf(":") + 1, parts[0].indexOf(";"));
        String base64Data = parts[1];

        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        // Save to DB
        Attachment image = new Attachment();
        image.setFile(imageBytes);
        image.setContentType(mimeType);
        Attachment savedImage = attachmentRepository.save(image);

        String generatedId = savedImage.getId();
        String fileUrl = "http://localhost:8080/mxworld/v1/attachment/files/" + generatedId;

        Map<String, String> data = new HashMap<>();
        data.put("id", generatedId);
        data.put("fileUrl", fileUrl);

        return new ApiResponseDto<>(200, "Uploaded Successfully", data);
    };

    @Override
    public String getUri(String id) {
        String uri = BASE_URL + id;
        return uri;
    }

    @Override
    public Attachment getAttachmentById(String id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found"));
    }
}
