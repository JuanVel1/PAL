package com.example.pal.dto;

import java.util.List;

public class ResponseDTO<T> {
    private String message;
    private List<T> data;


    public ResponseDTO(String message, T data) {
        this.message = message;
        //Si data es null instanciar una lista vacía
        if (data == null) {
            this.data = List.of();
        } else {
            this.data = List.of(data);
        }
    }
    public ResponseDTO(String message, List<T> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }
}
