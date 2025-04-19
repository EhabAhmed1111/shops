package com.e_commerceapp.clothshops.service.image;

import com.e_commerceapp.clothshops.dto.ImageDTO;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.exceptionhandler.ImageUploadException;
import com.e_commerceapp.clothshops.mapper.ImageMapper;
import com.e_commerceapp.clothshops.model.Image;
import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.repository.ImageRepository;
import com.e_commerceapp.clothshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {


    private final IProductService productService;

    private final ImageRepository imageRepository;

    private final ImageMapper imageMapper;


    @Override
    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no image with id: " + imageId
                )
        );
    }

    @Override
    @Transactional
    public void deleteImageById(Long imageId) {
        imageRepository.findById(imageId).ifPresentOrElse(imageRepository::delete, () -> {
            throw new GlobalNotFoundException(
                    "there is no image with id: " + imageId
            );
        });
    }

    @Override
    @Transactional
    public List <ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
//        if (files.isEmpty()){
//            throw new ImageUploadException(
//                    "can't Upload images"
//            );
//        }
        Product product = productService.getProductById(productId);
        List<ImageDTO> savedImageDTOS = new ArrayList<>();
        for (MultipartFile file : files) {

            String buildDownloadUrl =  "/api/v1/images/image/download";
            Image image = createNewImage(buildDownloadUrl,product,file);
//                image.setFileName(file.getOriginalFilename());
//                image.setFileType(file.getContentType());
//                image.setProduct(product);
//                //i need to understand what is serial blob
//                //and i need to understand what is Blob too
//                image.setImage(new SerialBlob(file.getBytes()));
//
//                String downloadUrl = buildDownloadUrl + image.getId();
//                image.setDownloadUrl(downloadUrl);

            Image savedImage =  imageRepository.save(image);

            savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
            imageRepository.save(savedImage);

            savedImageDTOS.add(imageMapper.fromImage(savedImage));
        }
        return savedImageDTOS;
    }

    private Image createNewImage(String buildDownloadUrl, Product product, MultipartFile file){
        try {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setProduct(product);
            //i need to understand what is serial blob
            //and i need to understand what is Blob too
            image.setImage(new SerialBlob(file.getBytes()));
            String downloadUrl = buildDownloadUrl + image.getId();
            image.setDownloadUrl(downloadUrl);
            return image;
        }catch (IOException|SQLException e){
            throw new RuntimeException("couldn't find image: " + e.getMessage() , e);
        }
    }

    @Override
    @Transactional
    public ImageDTO updateImage(Long imageId, MultipartFile file) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
            return imageMapper.fromImage(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(
                    "Failed to update image" + e.getMessage(), e
            );
        }
    }
}
