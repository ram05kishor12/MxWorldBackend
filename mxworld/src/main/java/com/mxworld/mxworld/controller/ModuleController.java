package com.mxworld.mxworld.controller;

import org.springframework.web.bind.annotation.RestController;

import com.mxworld.mxworld.interfaces.ModuleInterface;
import com.mxworld.mxworld.syntax.ApiResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@Tag(name = "Module Api")
@AllArgsConstructor
public class ModuleController {

    private final ModuleInterface moduleInterface;

    @PostMapping("/addModule/{courseId}")
    public ResponseEntity<ApiResponseDto<?>> addModule(
            @PathVariable String courseId,
            @RequestBody com.mxworld.mxworld.syntax.Module.Module moduleRequest) {
        try {
            ApiResponseDto<?> response = moduleInterface.addModule(courseId, moduleRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "Internal Server Error", null));
        }
    }

    @GetMapping("/getAllModule")
    public ResponseEntity<ApiResponseDto<?>> getAllModule(@RequestParam String course_Id) {
        try {
            ApiResponseDto<?> respone = moduleInterface.getAllModule(course_Id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(respone);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>(500, "Internal Server Error", null));
        }
    }

}
