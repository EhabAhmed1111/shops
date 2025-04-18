package com.e_commerceapp.clothshops.controller;

import ch.qos.logback.core.util.StringUtil;
import com.e_commerceapp.clothshops.dto.ImageDTO;
import com.e_commerceapp.clothshops.exceptionhandler.GlobalNotFoundException;
import com.e_commerceapp.clothshops.model.Image;
import com.e_commerceapp.clothshops.response.ApiResponse;
import com.e_commerceapp.clothshops.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("/${api.prefix}/images")
public class ImageRestController {

    private final IImageService imageService;

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
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId)  {
        try {
            Image image = imageService.getImageById(imageId);

            ByteArrayResource resources = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));

            String safeFileName = StringUtils.cleanPath(image.getFileName());
            ContentDisposition disposition=ContentDisposition.attachment().filename(safeFileName).build();

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType())) // set MIME type ex(image/Png)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            disposition.toString()
//                            "attachment; filename=\"" + image.getFileName() + "\""
                    )
                    .body(resources);
        }catch (SQLException e){
            throw new GlobalNotFoundException(
                    "there is no image"
            );
        }
    }
}
