package com.e_commerceapp.clothshops.service.image;

import com.e_commerceapp.clothshops.data.dto.ImageDTO;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.mapper.ImageMapper;
import com.e_commerceapp.clothshops.data.model.Image;
import com.e_commerceapp.clothshops.data.model.Product;
import com.e_commerceapp.clothshops.data.repository.ImageRepository;
import com.e_commerceapp.clothshops.service.product.ProductService;
import com.e_commerceapp.clothshops.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ImageService  {


    private final ProductService productService;

    private final ImageRepository imageRepository;

    private final ImageMapper imageMapper;


    @Autowired
    public ImageService(ProductService productService, ImageRepository imageRepository, ImageMapper imageMapper) {
        this.productService = productService;
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no image with id: " + imageId
                )
        );
    }

    public void deleteImageById(Long imageId) {
        imageRepository.findById(imageId).ifPresentOrElse(image -> {
            //this will get the file in os
            File file = new File(image.getDownloadUrl());
            file.delete();
        }, () -> {
            throw new GlobalNotFoundException(
                    "there is no image with id: " + imageId
            );
        });
    }

    public List <ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDTO> savedImageDTOS = new ArrayList<>();
        for (MultipartFile file : files) {

            String buildDownloadUrl = Constants.directoryPath;
            Image image = createNewImage(buildDownloadUrl,product,file);
            Image savedImage =  imageRepository.save(image);
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
//            image.setImage(new SerialBlob(file.getBytes()));
            String downloadUrl = buildDownloadUrl + image.getFileName();
            image.setDownloadUrl(downloadUrl);
            //here it will start write image in path you give
            FileOutputStream fou = new FileOutputStream(downloadUrl);
            fou.write(file.getBytes());
            fou.flush();
            fou.close();
            return image;
        }catch (Exception e){
            throw new RuntimeException("couldn't find image: " + e.getMessage() , e);
        }
    }

    public ImageDTO updateImage(Long imageId, MultipartFile file) {
        Image image = getImageById(imageId);
        try {
            String downloadUrl= Constants.directoryPath + file.getOriginalFilename();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
//            image.setImage(new SerialBlob(file.getBytes()));
            //the process is to delete the previous file and write new one
            File existedFile = new File(image.getDownloadUrl());
            existedFile.delete();
            image.setDownloadUrl(downloadUrl);
            FileOutputStream fou = new FileOutputStream(downloadUrl);
            fou.write(file.getBytes());
            //to clean the channel
            fou.flush();
            fou.close();
            imageRepository.save(image);
            return imageMapper.fromImage(image);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to update image" + e.getMessage(), e
            );
        }
    }
}
