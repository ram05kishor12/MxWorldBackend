package com.mxworld.mxworld.interfaces;

import com.mxworld.mxworld.syntax.ApiResponseDto;
import com.mxworld.mxworld.syntax.Module.Module;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface ModuleInterface {
    ApiResponseDto<?> addModule(String id , @RequestBody Module moduleRequest);
    ApiResponseDto<?> getAllModule(String id);
}
