package com.library.mapper;

import com.library.domain.Author;
import com.library.dto.AuthorDTO;
import com.library.dto.request.AuthorRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author authorRequestToAuthor(AuthorRequest authorRequest);

    AuthorDTO authorToAuthorDTO(Author author);
}