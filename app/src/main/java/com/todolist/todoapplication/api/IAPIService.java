package com.todolist.todoapplication.api;

import com.todolist.todoapplication.api.model.loginResponseModel;
import com.todolist.todoapplication.api.model.registerResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IAPIService {

    @FormUrlEncoded
    @POST("login")
    Call<loginResponseModel> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register")
    Call<registerResponseModel> registerUser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

}
