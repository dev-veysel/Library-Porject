package com.library.service;

import com.library.domain.Author;
import com.library.dto.AuthorDTO;
import com.library.dto.request.AuthorRequest;
import com.library.dto.request.AuthorUpdateRequest;
import com.library.exception.BadRequestException;
import com.library.exception.ResourceNotFoundException;
import com.library.exception.message.ErrorMessage;
import com.library.mapper.AuthorMapper;
import com.library.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }


    public AuthorDTO createAuthor(AuthorRequest request) {
        Author author = authorMapper.authorRequestToAuthor(request);
        if (request.getBuiltIn() == null) {
            author.setBuiltIn(false);}
        authorRepository.save(author);
        return authorMapper.authorToAuthorDTO(author);
    }


    public Page<AuthorDTO> getALlAuthorsPageble(Pageable pageable) {
        Page<Author> pageAll = authorRepository.findAll(pageable);
        return pageAll.map(authorMapper::authorToAuthorDTO);
    }


    public AuthorDTO getAuthorById(Long id) {
        Author author = findById(id);
        return authorMapper.authorToAuthorDTO(author);
    }


    public Author findById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));
    }


    public AuthorDTO updateAuthor(AuthorUpdateRequest authorUpdateRequest, Long id) {
        Author author = findById(id);
        if (author.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);}
        author.setBuiltIn(false);
        author.setName(authorUpdateRequest.getName());
        authorRepository.save(author);
        return authorMapper.authorToAuthorDTO(author);
    }


    public AuthorDTO deleteAuthorById(Long id) {
        Author author = findById(id);
        if (author.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);}
        if (!author.getBook().isEmpty()) {
            throw new BadRequestException(ErrorMessage.AUTHOR_HAS_BOOK_EXCEPTION);}
        AuthorDTO authorDTO = authorMapper.authorToAuthorDTO(author);
        authorRepository.deleteById(id);
        return authorDTO;
    }


    public Long getUserCount() {
        return authorRepository.count();
    }
}