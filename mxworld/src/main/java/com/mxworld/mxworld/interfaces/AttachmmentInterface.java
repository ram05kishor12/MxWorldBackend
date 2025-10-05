package com.mxworld.mxworld.interfaces;

import java.util.Map;

import com.mxworld.mxworld.model.Attachment;
import com.mxworld.mxworld.syntax.ApiResponseDto;

public interface AttachmmentInterface {

    ApiResponseDto<Map<String, String>> uploadFile(String img);

    String getUri(String id);

    Attachment getAttachmentById(String id);

}
