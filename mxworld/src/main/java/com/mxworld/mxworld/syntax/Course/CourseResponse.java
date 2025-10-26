package com.mxworld.mxworld.syntax.Course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private String id;
    private String courseName;
    private String courseDescription;
    private String thumbNail;
}