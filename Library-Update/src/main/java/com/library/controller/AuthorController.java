package com.library.controller;

import com.library.dto.AuthorDTO;
import com.library.dto.request.AuthorRequest;
import com.library.dto.request.AuthorUpdateRequest;
import com.library.service.AuthorService;
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
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    @PostMapping("/author")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorRequest request){
        AuthorDTO authorDTO=authorService.createAuthor(request);
        return new ResponseEntity<>(authorDTO, HttpStatus.CREATED);
    }


    @GetMapping("/authors")
    public ResponseEntity<Page<AuthorDTO>> getAuthoryByPage(@RequestParam(required = false ,value="page",defaultValue ="0") int page,
                                                            @RequestParam(required = false ,value="size",defaultValue ="20") int size,
                                                            @RequestParam(required = false ,value="sort",defaultValue ="name") String prop,
                                                            @RequestParam(required = false ,value="type",defaultValue ="ASC") Sort.Direction type){
        Pageable pageable= PageRequest.of(page, size, Sort.by(type, prop));
        Page<AuthorDTO> authorDTO= authorService.getALlAuthorsPageble(pageable);
        return ResponseEntity.ok(authorDTO);
    }


    @GetMapping("/authors/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable("id") Long id){
        AuthorDTO authorDTO=authorService.getAuthorById(id);
        return ResponseEntity.ok(authorDTO);
    }


    @PutMapping("/author/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable("id") Long id,@Valid @RequestBody AuthorUpdateRequest authorUpdateRequest){
        AuthorDTO authorDTO=authorService.updateAuthor(authorUpdateRequest,id);
        return ResponseEntity.ok(authorDTO);
    }


    @DeleteMapping("/author/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDTO> deleteAuthorById(@PathVariable Long id){
        AuthorDTO authorDTO = authorService.deleteAuthorById(id);
        return ResponseEntity.ok(authorDTO);
    }
}