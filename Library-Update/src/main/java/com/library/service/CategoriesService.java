package com.library.service;

import com.library.domain.Categories;
import com.library.dto.AuthorDTO;
import com.library.dto.CategoriesDTO;
import com.library.dto.request.CategoriesRequest;
import com.library.dto.request.CategoriesUpdateRequest;
import com.library.exception.BadRequestException;
import com.library.exception.ResourceNotFoundException;
import com.library.exception.message.ErrorMessage;
import com.library.mapper.CategoriesMapper;
import com.library.repository.CategoriesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;

    public CategoriesService(CategoriesRepository categoriesRepository, CategoriesMapper categoriesMapper) {
        this.categoriesRepository = categoriesRepository;
        this.categoriesMapper = categoriesMapper;
    }


    public Page<CategoriesDTO> getAllByPage(Pageable pageable) {
        Page<Categories> list=categoriesRepository.findAll(pageable);
        return list.map(categoriesMapper::categoryToCategoryDTO);
    }


    public CategoriesDTO findByCategoryId(Long id) {
        Categories categories=findById(id);
        return categoriesMapper.categoryToCategoryDTO(categories);
    }


    public Categories findById(Long id) {
        return categoriesRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessage.CATEGORY_NOT_FOUND_EXCEPTION,id)));
    }


    public CategoriesDTO createCategory(CategoriesRequest categoriesRequest) {
        Categories categories=categoriesMapper.categoryRequestToCategory(categoriesRequest);
        List<Integer> sequences=categoriesRepository.getAllSequences();
        if(categoriesRequest.getSequence()==null){
            int max=0;
            for (int i:sequences){
                max=Math.max(max,i);}
            categories.setSequence(max+1);}
        categoriesRepository.save(categories);
        return categoriesMapper.categoryToCategoryDTO(categories);
    }


    public CategoriesDTO updateCategories(Long id, CategoriesUpdateRequest request) {
        Categories categories=findById(id);
        if(categories.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);}
        categories.setName(request.getName());
        categories.setSequence(request.getSequence());
        List<Integer> sequences=categoriesRepository.getAllSequences();

        if(request.getSequence()==null){
            int max=0;
            for (int i:sequences){
                max=Math.max(max,i);}
            categories.setSequence(max+1);
        }
        categories.setBuiltIn(false);
        categoriesRepository.save(categories);
        return categoriesMapper.categoryToCategoryDTO(categories);
    }


    public CategoriesDTO deleteCategories(Long id) {
        Categories categories=findById(id);
        if(categories.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);}
        if(!categories.getBooks().isEmpty()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);}
        CategoriesDTO categoriesDTO=categoriesMapper.categoryToCategoryDTO(categories);
        categoriesRepository.delete(categories);
        return categoriesDTO;
    }


    public Long getUserCount() {
        return categoriesRepository.count();
    }
}