package com.Developer.DreamShop.service.image;

import com.Developer.DreamShop.dto.ImageDto;
import com.Developer.DreamShop.model.Image;
import com.Developer.DreamShop.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(long id);
    void  deleteImageById(long id);
    List<ImageDto>  saveImages(List<MultipartFile>  file , long productID);
    Image UpdateImage(MultipartFile  file ,long imageId);
}
