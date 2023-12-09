package com.example.signproject.Utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultJson<T> {
    private int code;
    private String message;
    private T data;

    public ResultJson(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultJson() {
    }

    public static <E> ResultJson<E> success(E data) {
        return new ResultJson<>(20000, "操作成功", data);
    }

    public static ResultJson<Object> success() {
        return new ResultJson<>(20000, "操作成功", null);
    }

    public static ResultJson<Object> error(String msg) {
        return new ResultJson<>(1, msg, null);
    }
}
