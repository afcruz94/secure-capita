package com.afcruz.securecapita.responseHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.Instant;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@SuperBuilder
@JsonInclude(NON_DEFAULT)
public class HttpResponse {
    protected Instant timestamp;
    protected Integer statusCode;
    protected HttpStatus status;
    protected String reason;
    protected String description;
    protected String developerMessage;
    protected Object data;
}
