package com.e_commerceapp.clothshops.service.image;

import com.e_commerceapp.clothshops.dto.ImageDTO;
import com.e_commerceapp.clothshops.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long imageId);

    void deleteImageById(Long imageId);

    List <ImageDTO> saveImages(List<MultipartFile> files, Long productId) ;

    void updateImage(Long imageId,MultipartFile file) ;

}
