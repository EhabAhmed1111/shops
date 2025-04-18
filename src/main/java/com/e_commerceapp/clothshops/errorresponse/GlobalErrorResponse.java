package com.e_commerceapp.clothshops.errorresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GlobalErrorResponse {

    private int status;

    private String message;

    private long timeStamp;
}

