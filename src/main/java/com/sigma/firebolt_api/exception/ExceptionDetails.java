package com.sigma.firebolt_api.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionDetails {
    private  int status;
    private  String title;
    private  String message;
    private  String time;
}
