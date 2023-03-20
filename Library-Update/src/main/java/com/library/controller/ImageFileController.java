package com.library.controller;

import com.library.domain.ImageFile;
import com.library.dto.ImageFileDto;
import com.library.dto.response.ImageSavedResponse;
import com.library.dto.response.ResponseMessage;
import com.library.dto.response.SfResponse;
import com.library.service.ImageFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class ImageFileController {

    private final ImageFileService imageFileService;

    public ImageFileController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }


    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageSavedResponse> uploadFile(@RequestParam("file") MultipartFile file){
        String imageId=imageFileService.saveImage(file);
        ImageSavedResponse response=new ImageSavedResponse(ResponseMessage.IMAGE_UPDATE_MESSAGE,true,imageId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id){
        ImageFile imageFile = imageFileService.downloadFile(id);
        return ResponseEntity.ok().header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + imageFile.getName()).
                body(imageFile.getImageData().getData());
    }


    @GetMapping("/display/{id}")
    public ResponseEntity<byte[]> displayFile(@PathVariable String id) {
        ImageFile imageFile = imageFileService.downloadFile(id);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(
                imageFile.getImageData().getData(),
                header,
                HttpStatus.OK);
    }


    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImageFileDto>> getAllImages() {
        List<ImageFileDto> list=imageFileService.getAllImages();
        return new ResponseEntity<>(list,HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SfResponse> deleteImage(@PathVariable String id){
        imageFileService.deleteImage(id);
        SfResponse response=new SfResponse(ResponseMessage.DELETE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }
}