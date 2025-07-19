package com.e_commerceapp.clothshops.rest.controller;

import com.e_commerceapp.clothshops.data.dto.ImageDTO;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.data.model.Image;
import com.e_commerceapp.clothshops.rest.response.ApiResponse;
import com.e_commerceapp.clothshops.service.image.ImageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("${api.prefix}/images")
public class ImageRestController {

    private final ImageService imageService;

    public ImageRestController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        try {
            List<ImageDTO> imageDTOS = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload success!", imageDTOS));
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Upload Failed!", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) {
        //can be done in another method
        try {
            Image image = imageService.getImageById(imageId);

            File file = new File(image.getDownloadUrl());
            //used to read file bit by bit
            FileInputStream fin = new FileInputStream(file);
            byte [] bytes = new byte[(int) file.length()];
            //here it will read and put it in bytes array
           fin.read(bytes);
           //the return type is resource
            ByteArrayResource resources = new ByteArrayResource(bytes);
            //for safe download
            String safeFileName = StringUtils.cleanPath(image.getFileName());

            fin.close();
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType())) // set MIME type ex(image/Png)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
//                            disposition.toString()
                            "attachment; filename=\"" + image.getFileName() + "\""
                    )
                    .body(resources);
        } catch (Exception e) {
            throw new GlobalNotFoundException(
                    "there is no image" + e.getMessage()
            );
        }
    }


    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestParam MultipartFile file) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                ImageDTO imageDTO = imageService.updateImage(imageId, file);
                return ResponseEntity.ok(new ApiResponse("image updated successfully", imageDTO));
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("not found exception", e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse("update failed", INTERNAL_SERVER_ERROR), INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Image deleted successfully", null));
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Can't found image with id: " + imageId, e.getMessage()), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ApiResponse("Delete failed", INTERNAL_SERVER_ERROR), INTERNAL_SERVER_ERROR);
    }

}
