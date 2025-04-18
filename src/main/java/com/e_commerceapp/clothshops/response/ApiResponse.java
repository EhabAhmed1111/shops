package com.e_commerceapp.clothshops.response;


import lombok.AllArgsConstructor;
import lombok.Data;


//this class we use to return data to front end
@AllArgsConstructor
@Data
public class ApiResponse {
    private String message;
    private Object data;
}
