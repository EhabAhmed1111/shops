package com.e_commerceapp.clothshops.mapper;


import com.e_commerceapp.clothshops.data.dto.ImageDTO;
import com.e_commerceapp.clothshops.data.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ImageMapper {

     @Mappings({
             @Mapping(source = "id",target = "imageId"),
             @Mapping(source = "fileName",target = "imageName")
     })
     ImageDTO fromImage(Image image);
}
