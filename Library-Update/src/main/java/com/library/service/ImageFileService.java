package com.library.service;

import com.library.domain.ImageData;
import com.library.domain.ImageFile;
import com.library.dto.ImageFileDto;
import com.library.exception.ResourceNotFoundException;
import com.library.exception.message.ErrorMessage;
import com.library.repository.ImageFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    public ImageFileService(ImageFileRepository imageFileRepository) {
        this.imageFileRepository = imageFileRepository;
    }


    public String saveImage(MultipartFile file) {
        ImageFile imageFile=null;
        String fileName= StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            ImageData imageData=new ImageData(file.getBytes());
            imageFile=new ImageFile(fileName,file.getContentType(),imageData);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        imageFileRepository.save(imageFile);
        return imageFile.getId();
    }


    public ImageFile downloadFile(String id) {
        return imageFileRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_EXCEPTION,id)));
    }


    public List<ImageFileDto> getAllImages() {
        List<ImageFile> imageFiles = imageFileRepository.findAll();
        // image1 : localhost:8080/files/download/id
        List<ImageFileDto> imageFileDTOS =imageFiles.stream().map(imFile->{
            // URI
            String imageUri = ServletUriComponentsBuilder.
                    fromCurrentContextPath(). // localhost:8080
                            path("/files/download/"). // localhost:8080/files/download
                            path(imFile.getId()).toUriString();// localhost:8080/files/download/id
            return new ImageFileDto(imFile.getName(),
                    imFile.getType(),
                    imageUri,
                    imFile.getLength());
        }).collect(Collectors.toList());
        return imageFileDTOS;
    }


    public void deleteImage(String id) {
        ImageFile imageFile=downloadFile(id);
        imageFileRepository.delete(imageFile);
    }


    public ImageFile findImageById(String imageId) {
        return imageFileRepository.findImageById(imageId).orElseThrow(()->new ResourceNotFoundException(
                String.format(ErrorMessage.IMAGE_NOT_FOUND_EXCEPTION,imageId)));
    }
}