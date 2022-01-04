package co.greetings.restcontroller.api.util;

import java.time.Instant;

import lombok.Getter;

public class ErrorMessageHttpResponse implements MessageHttpResponse {
    @Getter public static class Error {
        private Instant timestamp;
        private String path;
        private int status;
        private String code;
        private String title;
        private String detail;
    }

    public static ErrorMessageHttpResponse builder() {
        var errorMessageHttpResponse = new ErrorMessageHttpResponse();
        errorMessageHttpResponse.error = new Error();
        errorMessageHttpResponse.error.code = "0";
        errorMessageHttpResponse.error.timestamp = Instant.now();
        return errorMessageHttpResponse;
    }

    @Getter private Error error;

    public ErrorMessageHttpResponse setTimestamp(Instant timestamp) {
        error.timestamp = timestamp;
        return this;
    }

    public ErrorMessageHttpResponse setPath(String path) {
        error.path = path;
        return this;
    }

    public ErrorMessageHttpResponse setStatus(int status) {
        error.status = status;
        return this;
    }

    public ErrorMessageHttpResponse setCode(String code) {
        error.code = code;
        return this;
    }

    public ErrorMessageHttpResponse setTitle(String title) {
        error.title = title;
        return this;
    }

    public ErrorMessageHttpResponse setdetail(String detail) {
        error.detail = detail;
        return this;
    }
}
