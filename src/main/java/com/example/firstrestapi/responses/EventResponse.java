package com.example.firstrestapi.responses;

import jakarta.annotation.Nullable;
import org.apache.logging.log4j.util.Strings;

public record EventResponse<T> (boolean success, String message, @Nullable T result) {
    public static EventResponse<?> withoutResult(boolean success , String message){
        return new EventResponse<>(success,message, Strings.EMPTY);
    }
    public static EventResponse<?> failed(String message){
        return new EventResponse<>(false, message, null);
    }
}
