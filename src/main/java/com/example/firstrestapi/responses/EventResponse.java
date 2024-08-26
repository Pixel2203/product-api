package com.example.firstrestapi.responses;

import com.example.firstrestapi.util.ErrorCode;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.util.Strings;

public record EventResponse<T> (boolean success, String message, @Nullable T result, int code) {
    public static EventResponse<?> withoutResult(boolean success , String message, ErrorCode code){
        return new EventResponse<>(success,message, Strings.EMPTY, code.getCode());
    }
    public static EventResponse<?> failed(String message, ErrorCode code){
        return new EventResponse<>(false, message, null, code.getCode());
    }
}
