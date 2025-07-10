package com.e_commerceapp.clothshops.controller;

import com.e_commerceapp.clothshops.dto.ImageDTO;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.model.Image;
import com.e_commerceapp.clothshops.response.ApiResponse;
import com.e_commerceapp.clothshops.service.image.IImageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

//@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageRestController {

    private final IImageService imageService;

    public ImageRestController(IImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(@RequestParam List<MultipartFile> files, @RequestParam Long productId) {
        try {
            List<ImageDTO> imageDTOS = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload success!", imageDTOS));
        } catch (Exception e) {
//            throw new Exception("Upload Failed!",e);
//            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload Failed!",e.getMessage()));
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
            ContentDisposition disposition = ContentDisposition.attachment().filename(safeFileName).build();

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


    //his method need some Q/A
    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestParam MultipartFile file) {
//        ImageDTO imageDTO = imageService.updateImage(imageId, file);
//        return ResponseEntity.ok(new ApiResponse("image updated successfully", imageDTO));
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
