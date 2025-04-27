package com.e_commerceapp.clothshops.service.image;

import com.e_commerceapp.clothshops.dto.ImageDTO;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.mapper.ImageMapper;
import com.e_commerceapp.clothshops.model.Image;
import com.e_commerceapp.clothshops.model.Product;
import com.e_commerceapp.clothshops.repository.ImageRepository;
import com.e_commerceapp.clothshops.service.product.IProductService;
import com.e_commerceapp.clothshops.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor
@Transactional
public class ImageService implements IImageService {


    private final IProductService productService;

    private final ImageRepository imageRepository;

    private final ImageMapper imageMapper;


    @Autowired
    public ImageService(IProductService productService, ImageRepository imageRepository, ImageMapper imageMapper) {
        this.productService = productService;
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    @Override
    public Image getImageById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(
                () -> new GlobalNotFoundException(
                        "there is no image with id: " + imageId
                )
        );
    }

    @Override
    public void deleteImageById(Long imageId) {
        imageRepository.findById(imageId).ifPresentOrElse(image -> {
            File file = new File(image.getDownloadUrl());
            file.delete();
        }, () -> {
            throw new GlobalNotFoundException(
                    "there is no image with id: " + imageId
            );
        });
    }

    @Override
    public List <ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
//        if (files.isEmpty()){
//            throw new ImageUploadException(
//                    "can't Upload images"
//            );
//        }
        Product product = productService.getProductById(productId);
        List<ImageDTO> savedImageDTOS = new ArrayList<>();
        for (MultipartFile file : files) {

            String buildDownloadUrl = Constants.directoryPath;
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

            // this will only save url not all image
            Image savedImage =  imageRepository.save(image);

//            savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
//            imageRepository.save(savedImage);

            savedImageDTOS.add(imageMapper.fromImage(savedImage));
        }
        return savedImageDTOS;
    }

    private Image createNewImage(String buildDownloadUrl, Product product, MultipartFile file){
        try {
            Image image = new Image();
//            byte [] bytes = file.getBytes();
//            ByteArrayResource byteArrayResource = new ByteArrayResource(bytes);

            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setProduct(product);
            //i need to understand what is serial blob
            //and i need to understand what is Blob too
//            image.setImage(new SerialBlob(file.getBytes()));
            String downloadUrl = buildDownloadUrl + image.getFileName();
            image.setDownloadUrl(downloadUrl);
            FileOutputStream fou = new FileOutputStream(downloadUrl);
            fou.write(file.getBytes());
            fou.flush();
            fou.close();
            return image;
        }catch (Exception e){
            throw new RuntimeException("couldn't find image: " + e.getMessage() , e);
        }
    }

    @Override
    public ImageDTO updateImage(Long imageId, MultipartFile file) {
        Image image = getImageById(imageId);
        try {
            String downloadUrl= Constants.directoryPath + file.getOriginalFilename();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
//            image.setImage(new SerialBlob(file.getBytes()));
            File file1 = new File(image.getDownloadUrl());
            file1.delete();
            image.setDownloadUrl(downloadUrl);
            FileOutputStream fou = new FileOutputStream(downloadUrl);
            fou.write(file.getBytes());
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
