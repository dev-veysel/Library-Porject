package com.library.exception.message;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Getter
public class ApiResponseError {

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-mm-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    private String requestUrl;

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    private ApiResponseError() {
        timestamp=LocalDateTime.now();
    }

    public ApiResponseError(HttpStatus status) {
        this();
        this.status = status;
        this.message = "Unexpected error";
    }

    public ApiResponseError(HttpStatus status, String message, String requestUrl) {
        this(status);
        this.message = message;
        this.requestUrl = requestUrl;
    }
}