package com.todolist.todoapplication.api;

public class UtilsAPI {
    public static IAPIService getApiService(){
        return APIClient.getClient().create(IAPIService.class);
    }
}
