package com.todolist.todoapplication.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class loginResponseModel {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("errorMessage")
    @Expose
    private Object errorMessage;
    @SerializedName("data")
    @Expose
    private tokenDataModel data;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public tokenDataModel getData() {
        return data;
    }

    public void setData(tokenDataModel data) {
        this.data = data;
    }
}
