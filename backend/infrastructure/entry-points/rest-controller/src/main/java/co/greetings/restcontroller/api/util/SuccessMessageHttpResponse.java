package co.greetings.restcontroller.api.util;

import lombok.Getter;

public class SuccessMessageHttpResponse<T> implements MessageHttpResponse {
    @Getter public T data;

    public SuccessMessageHttpResponse(T data) {
        this.data = data;
    }
}
