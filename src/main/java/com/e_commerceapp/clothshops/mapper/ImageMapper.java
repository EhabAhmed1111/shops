package com.e_commerceapp.clothshops.mapper;


import com.e_commerceapp.clothshops.dto.ImageDTO;
import com.e_commerceapp.clothshops.model.Image;
import org.springframework.stereotype.Service;

@Service
public class ImageMapper {

    public ImageDTO fromImage(Image image) {
        return new ImageDTO(
                image.getId(),
                image.getFileName(),
                image.getDownloadUrl()
        );
    }

}
