package com.deepu.productcatlogservice.response;

import com.deepu.productcatlogservice.enumeration.ResponseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonResponse {
    private ResponseStatus status;
    private String errorMessage;
    private String successMessage;
    private Object data;
    private int code;
}