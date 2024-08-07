package com.example.demo.controller.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommonResult<T> {
    private boolean success;
    private String errorMessage;
    private T data;

    public static <T> CommonResult<T> successResultWithoutData() {
        return successResult(null);
    }

    public static <T> CommonResult<T> successResult(T data) {
        return new CommonResult<>(
                true, "", data
        );
    }

    public static <T> CommonResult<T> businessFailResult(String errorMessage) {
        return new CommonResult<>(
                false, errorMessage, null
        );
    }
}
