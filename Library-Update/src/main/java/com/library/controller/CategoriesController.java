package com.library.controller;


import com.library.dto.CategoriesDTO;
import com.library.dto.request.CategoriesRequest;
import com.library.dto.request.CategoriesUpdateRequest;
import com.library.service.CategoriesService;
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
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }


    @GetMapping("/categories/page")
    public ResponseEntity<Page<CategoriesDTO>> getCategoriesByPage(@RequestParam(required = false ,value="page",defaultValue ="0") int page,
                                                                   @RequestParam(required = false ,value="size",defaultValue ="20") int size,
                                                                   @RequestParam(required = false ,value="sort",defaultValue ="name") String prop,
                                                                   @RequestParam(required = false ,value="type",defaultValue ="ASC") Sort.Direction type){
        Pageable pageable= PageRequest.of(page, size, Sort.by(type, prop));
        Page<CategoriesDTO> pages=categoriesService.getAllByPage(pageable);
        return ResponseEntity.ok(pages);
    }


    @GetMapping("/categories/page/{id}")
    public ResponseEntity<CategoriesDTO> getCategoryById(@PathVariable("id") Long id){
        CategoriesDTO categoriesDTO=categoriesService.findByCategoryId(id);
        return ResponseEntity.ok(categoriesDTO);
    }


    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriesDTO> createCategories(@Valid @RequestBody CategoriesRequest categoriesRequest){
        CategoriesDTO categoriesDTO=categoriesService.createCategory(categoriesRequest);
        return new ResponseEntity<>(categoriesDTO, HttpStatus.CREATED);
    }


    @PutMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriesDTO> updateCategories(@PathVariable("id") Long id, @RequestBody CategoriesUpdateRequest request){
        CategoriesDTO categoriesDTO=categoriesService.updateCategories(id,request);
        return new ResponseEntity<>(categoriesDTO,HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriesDTO> deleteCategories(@PathVariable("id") Long id){
        CategoriesDTO categoriesDTO=categoriesService.deleteCategories(id);
        return ResponseEntity.ok(categoriesDTO);
    }
}