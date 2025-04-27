package com.e_commerceapp.clothshops.response;


//this class we use to return data to front end
//@AllArgsConstructor
//@Data
public class ApiResponse {
    private String message;
    private Object data;


    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
