package com.mxworld.mxworld.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxworld.mxworld.interfaces.AttachmmentInterface;
import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Attachment.Attachment;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/mxworld/v1/attachment")
@Tag(name = "Attachment API")
public class AttachmentController {

    private final AttachmmentInterface attachmmentInterface;

    public AttachmentController(AttachmmentInterface attachmmentInterface) {
        this.attachmmentInterface = attachmmentInterface;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<ApiResponseDto<?>> uploadFile(@RequestBody Attachment attachmentBody) {
        try {
            if (attachmentBody.getBase64() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDto<>(400, "No Base 64 Data Exist", null));
            }
            String base64 = attachmentBody.getBase64();
            ApiResponseDto<?> response = attachmmentInterface.uploadFile(base64);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto<>(400, "Bad Request", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "Internal Server Error", e.getMessage()));
        }
    };

    @GetMapping("/getUri")
    public ResponseEntity<ApiResponseDto<String>> getFileUri(@RequestParam String id) {
        try {
            String uri = attachmmentInterface.getUri(id);
            return ResponseEntity.ok(new ApiResponseDto<>(200, "Success", uri));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "Internal Server Error", e.getMessage()));
        }
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        try {
            com.mxworld.mxworld.model.Attachment attachment = attachmmentInterface.getAttachmentById(id); 

            return ResponseEntity.ok()
                    .header("Content-Type", attachment.getContentType())
                    .body(attachment.getFile());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
