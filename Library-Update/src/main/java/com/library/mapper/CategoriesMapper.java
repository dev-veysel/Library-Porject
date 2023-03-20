package com.library.mapper;

import com.library.domain.Categories;
import com.library.dto.CategoriesDTO;
import com.library.dto.request.CategoriesRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {

    CategoriesDTO categoryToCategoryDTO(Categories categories);

    Categories categoryRequestToCategory(CategoriesRequest categoriesRequest);
}