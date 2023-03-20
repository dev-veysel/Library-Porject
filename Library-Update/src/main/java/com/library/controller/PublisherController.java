package com.library.controller;

import com.library.dto.AuthorDTO;
import com.library.dto.PublishersDTO;
import com.library.dto.request.AuthorUpdateRequest;
import com.library.dto.request.PublisherRequest;
import com.library.dto.request.PublisherUpdateRequest;
import com.library.service.PublisherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }


    @PostMapping("/publishers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublishersDTO> savePublisher(@Valid @RequestBody PublisherRequest request) {
        PublishersDTO publisher = publisherService.savePublisher(request);
        return new ResponseEntity<>(publisher, HttpStatus.CREATED);
    }


    @GetMapping("/publishers/{id}")
    public ResponseEntity<PublishersDTO> getPublisherById(@PathVariable("id") Long id) {
        PublishersDTO publishersDTO = publisherService.findByPublisherId(id);
        return ResponseEntity.ok(publishersDTO);
    }


    @GetMapping("/publishers/page")
    public ResponseEntity<Page<PublishersDTO>> getAuthoryByPage(@RequestParam(required = false, value = "page", defaultValue = "0") int page,
                                                                @RequestParam(required = false, value = "size", defaultValue = "20") int size,
                                                                @RequestParam(required = false, value = "sort", defaultValue = "name") String prop,
                                                                @RequestParam(required = false, value = "type", defaultValue = "ASC") Sort.Direction type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));
        Page<PublishersDTO> publishersDTO = publisherService.getAllPublishersByPage(pageable);
        return ResponseEntity.ok(publishersDTO);
    }


    @PutMapping("/publishers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublishersDTO> updatePublisher(@PathVariable("id") Long id,@Valid @RequestBody PublisherUpdateRequest UpdateRequest){
        PublishersDTO publishersDTO=publisherService.updateAuthor(UpdateRequest,id);
        return ResponseEntity.ok(publishersDTO);
    }


    @DeleteMapping("/publishers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PublishersDTO> deletePublisher(@PathVariable Long id){
        PublishersDTO publishersDTO = publisherService.deletePublisher(id);
        return ResponseEntity.ok(publishersDTO);
    }
}