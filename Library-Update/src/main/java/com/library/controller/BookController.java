package com.library.controller;

import com.library.dto.BookDTO;
import com.library.dto.request.BookRequest;
import com.library.dto.request.BookUpdateRequest;
import com.library.service.BookService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable("id") Long id) {
        BookDTO bookDTO=bookService.getBookById(id);
        return ResponseEntity.ok(bookDTO);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> saveBook(@Valid @RequestBody BookRequest bookRequest){
        BookDTO bookDTO=bookService.saveBook(bookRequest);
        return new ResponseEntity<>(bookDTO, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> updateBook(@PathVariable("id") Long id, @Valid @RequestBody BookUpdateRequest updateRequest){
        BookDTO bookDTO=bookService.updateBook(id, updateRequest);
        return ResponseEntity.ok(bookDTO);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable("id") Long id){
        BookDTO bookDTO=bookService.deleteBook(id);
        return ResponseEntity.ok(bookDTO);
    }
}