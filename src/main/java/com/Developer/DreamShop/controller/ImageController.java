package com.Developer.DreamShop.controller;

import com.Developer.DreamShop.dto.ImageDto;
import com.Developer.DreamShop.exceptions.ImageNotFoundException;
import com.Developer.DreamShop.model.Image;
import com.Developer.DreamShop.response.ApiResponse;
import com.Developer.DreamShop.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse>  saveImages(@RequestParam List<MultipartFile> file ,@RequestParam long productID){
        try {
            List<ImageDto> imageDtos = imageService.saveImages(file,productID);
            return ResponseEntity.ok(new ApiResponse( "upload success!", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("upload Failed!",
                    e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImages (@PathVariable long imageId) throws SQLException {
        // created byte array form blob(Binary large Object)
        Image image = imageService.getImageById(imageId);
        // created resource and returned as a file
        ByteArrayResource resource =
                new ByteArrayResource(image.getImage()
                        .getBytes(1, (int)image.getImage().length()));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment ; filename=\"" + image.getFileName() + "\"")
                .body(resource);
    }
    @PutMapping("/image/{imageID}/update")
    public ResponseEntity<ApiResponse> UpdateImage(@RequestBody MultipartFile  file ,@PathVariable  long imageId){
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null){
                imageService.UpdateImage(file, imageId);
            return ResponseEntity.ok(new ApiResponse("Success image Updated",null));
            }
        } catch (ImageNotFoundException e) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ApiResponse("Image Not Found"+ e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Update Failed ! Error", INTERNAL_SERVER_ERROR));
    }



    @DeleteMapping("/image/{imageID}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable  long imageId){
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Success Deleted image ",null));
            }
        } catch (ImageNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Image Not Found"+ e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Delete Failed ! Error", INTERNAL_SERVER_ERROR));
    }
}
