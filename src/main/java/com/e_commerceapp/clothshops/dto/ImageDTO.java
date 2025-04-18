package com.e_commerceapp.clothshops.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDTO {
    private Long imageId;
    private String imageName;
    private String downloadUrl;
}
