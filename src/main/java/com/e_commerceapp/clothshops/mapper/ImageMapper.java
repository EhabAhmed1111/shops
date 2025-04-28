package com.e_commerceapp.clothshops.mapper;


import com.e_commerceapp.clothshops.dto.ImageDTO;
import com.e_commerceapp.clothshops.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Service;

//@Service
//public class ImageMapper {
//
//    public ImageDTO fromImage(Image image) {
//        return new ImageDTO(
//                image.getId(),
//                image.getFileName(),
//                image.getDownloadUrl()
//        );
//    }
//
//}
@Mapper(componentModel = "spring")
public interface ImageMapper {

     @Mappings({
             @Mapping(source = "id",target = "imageId"),
             @Mapping(source = "fileName",target = "imageName")
     })
     ImageDTO fromImage(Image image);
}
