package com.library.service;

import com.library.domain.*;
import com.library.dto.BookDTO;
import com.library.dto.request.BookRequest;
import com.library.dto.request.BookUpdateRequest;
import com.library.exception.BadRequestException;
import com.library.exception.ResourceNotFoundException;
import com.library.exception.message.ErrorMessage;
import com.library.mapper.BookMapper;
import com.library.repository.BookRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoriesService categoriesService;
    private final ImageFileService imageFileService;
    private final PublisherService publisherService;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, CategoriesService categoriesService, ImageFileService imageFileService, PublisherService publisherService, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.categoriesService = categoriesService;
        this.imageFileService = imageFileService;
        this.publisherService = publisherService;
        this.authorService = authorService;
    }

    public BookDTO getBookById(Long id) {
        Book book = findById(id);
        if (book.getImage() == null) {
            return bookMapper.bookToBookDto(book);
        } else {
            BookDTO bookDTO = bookMapper.bookToBookDto(book);
            bookDTO.setImageId(book.getImage().getId());
            return bookDTO;
        }

    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.BOOK_NOT_FOUND_EXCEPTION, id)));
    }

    public BookDTO saveBook(BookRequest bookRequest) {
        Book book = new Book();
        Publisher publisher = publisherService.findById(bookRequest.getPublisherId());
        Categories categories = categoriesService.findById(bookRequest.getCategorieId());
        if (bookRequest.getImageId() != null) {
            ImageFile imageFile = imageFileService.findImageById(bookRequest.getImageId());
            book.setImage(imageFile);
        }

        Author author = authorService.findById(bookRequest.getAuthorId());

        book.setPublisher(publisher);
        book.setCategorie(categories);

        book.setAuthor(author);

        book.setName(bookRequest.getName());
        book.setBuiltIn(bookRequest.getBuiltIn());
        book.setIsbn(bookRequest.getIsbn());
        book.setPageCount(bookRequest.getPageCount());
        book.setPublishDate(bookRequest.getPublishDate());
        book.setShelfCode(bookRequest.getShelfCode());
        book.setFeature(bookRequest.getFeature());

        bookRepository.save(book);

        if (book.getImage() == null) {
            return bookMapper.bookToBookDto(book);
        } else {
            BookDTO bookDTO = bookMapper.bookToBookDto(book);
            bookDTO.setImageId(book.getImage().getId());
            return bookDTO;
        }

    }

    public BookDTO updateBook(Long id, BookUpdateRequest updateRequest) {
        Book book = findById(id);
        if (book.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);
        }
        Publisher publisher = publisherService.findById(updateRequest.getPublisherId());
        Categories categories = categoriesService.findById(updateRequest.getCategorieId());
        if (updateRequest.getImageId() != null) {
            ImageFile imageFile = imageFileService.findImageById(updateRequest.getImageId());
            book.setImage(imageFile);
        }

        Author author = authorService.findById(updateRequest.getAuthorId());

        book.setActive(updateRequest.getActive());
        book.setIsbn(updateRequest.getIsbn());
        book.setName(updateRequest.getName());
        book.setShelfCode(updateRequest.getShelfCode());
        book.setFeature(updateRequest.getFeature());
        book.setPublishDate(updateRequest.getPublishDate());
        book.setPageCount(updateRequest.getPageCount());
        book.setPublisher(publisher);
        book.setCategorie(categories);

        book.setAuthor(author);

        bookRepository.save(book);


        if (book.getImage() == null) {
            return bookMapper.bookToBookDto(book);
        } else {
            BookDTO bookDTO = bookMapper.bookToBookDto(book);
            bookDTO.setImageId(book.getImage().getId());
            return bookDTO;
        }

    }

    public BookDTO deleteBook(Long id) {
        Book book = findById(id);
        if (book.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);
        }
        if (!book.getLoans().isEmpty()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);
        }
        if (book.getImage() == null) {
            BookDTO bookDTO = bookMapper.bookToBookDto(book);
            bookRepository.delete(book);
            return bookDTO;
        } else {
            BookDTO bookDTO = bookMapper.bookToBookDto(book);
            bookDTO.setImageId(book.getImage().getId());
            bookRepository.delete(book);
            return bookDTO;
        }

    }

    public void updateBookLoanable(Book book) {
        bookRepository.save(book);
    }

    public Long getUserCount() {
        return bookRepository.count();
    }

    public Long countUnReturnedBooks() {
        return bookRepository.countUnReturnedBooks();
    }

    public List<Book> getUnReturnedBooks() {
        return bookRepository.getUnReturnedBooks();
    }

    public Page<Book> getUnReturnedBooksByPage(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

}