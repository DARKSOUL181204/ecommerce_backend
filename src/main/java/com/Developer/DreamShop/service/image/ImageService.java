package com.Developer.DreamShop.service.image;

import com.Developer.DreamShop.dto.ImageDto;
import com.Developer.DreamShop.exceptions.ImageNotFoundException;
import com.Developer.DreamShop.model.Image;
import com.Developer.DreamShop.model.Product;
import com.Developer.DreamShop.repository.ImageRepository;
import com.Developer.DreamShop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(long id) {
        return imageRepository.findById(id).orElseThrow(()-> new ImageNotFoundException("Image not Found" + id));
    }

    @Override
    public void deleteImageById(long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::save,()->{
            throw new ImageNotFoundException("Image not Found" + id);
        });
    }

    @Override
    public  List<ImageDto>  saveImages(List<MultipartFile> file, long productID) {
        //product and find
        // images save
        // all details
        // dto doesn't save in db only for transferring backend to frontend
        Product product = productService.getProductById(productID);
        List<ImageDto> savedImageDtos = new ArrayList<>();
        for (MultipartFile f: file){
            try {
                // try to save each image and setting it's all attributes
                Image image = new Image();
                image.setFileName(f.getOriginalFilename());
                image.setFileType(f.getContentType());
                image.setImage(new SerialBlob(f.getBytes()));
                image.setProduct(product);

                //want a downloadable url to download a image
                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl+image.getImageId();
                image.setDownloadUrl(downloadUrl);
                // redone the saved image because at the point of
                // setting up url we were not able to get the image id because it's not saved till that point
                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getImageId());
                /* saved the saveImage in image repository */
                imageRepository.save(savedImage);


                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getImageId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDtos.add(imageDto);


            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return savedImageDtos;
    }

    @Override
    public Image UpdateImage(MultipartFile file, long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileType(file.getContentType());
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            return imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }


}
