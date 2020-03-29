package com.zhuyz.adminuser.entity;

import java.io.Serializable;

public class ResponseEntity<T> {



    private int code;
    private String msg;
    private T data;

    public ResponseEntity() {
    }

    public ResponseEntity(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseEntity(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
