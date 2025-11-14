package com.ecommerce.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    public boolean success;
    public String message;
    public T data;

    public static <T> Response<T> ok() {
        Response<T> r = new Response<>();
        r.success = true;
        r.message = "OK";
        r.data = null;
        return r;
    }

    public static <T> Response<T> okData(T data) {
        Response<T> r = new Response<>();
        r.success = true;
        r.message = "OK";
        r.data = data;
        return r;
    }

    public static <T> Response<T> fail(String msg) {
        Response<T> r = new Response<>();
        r.success = false;
        r.message = msg;
        r.data = null;
        return r;
    }
}
