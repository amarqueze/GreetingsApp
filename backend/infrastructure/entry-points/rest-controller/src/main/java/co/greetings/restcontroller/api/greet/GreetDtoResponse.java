package co.greetings.restcontroller.api.greet;

import co.greetings.restcontroller.api.util.MessageHttpResponse;
import lombok.Getter;

@Getter
public class GreetDtoResponse<T> implements MessageHttpResponse {
    public String messageGreet;
    public T data;
}
