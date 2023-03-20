package com.library.service;

import com.library.domain.Book;
import com.library.domain.User;
import com.library.dto.BookDTO;
import com.library.dto.response.TotalReportResponse;
import com.library.dto.response.UserResponse;
import com.library.exception.message.ErrorMessage;
import com.library.mapper.BookMapper;
import com.library.report.ExcelReporter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
@Service
public class ReportService {

    private final BookService bookService;
    private final UserService userService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final CategoriesService categoriesService;
    private final LoanService loanService;
    private final BookMapper bookMapper;

    public ReportService(BookService bookService, UserService userService, AuthorService authorService,
                         PublisherService publisherService, CategoriesService categoriesService,
                         LoanService loanService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.userService = userService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.categoriesService = categoriesService;
        this.loanService = loanService;
        this.bookMapper = bookMapper;
    }


    public ByteArrayInputStream getTotalReport()  {
        Long userCount =userService.getUserCount();
        Long loanCount =loanService.getUserCount();
        Long bookCount =bookService.getUserCount();
        Long publisherCount =publisherService.getUserCount();
        Long categoryCount =categoriesService.getUserCount();
        Long authorCount =authorService.getUserCount();
        Long unReturnedBooks=bookService.countUnReturnedBooks();
        Long expiredBook=loanService.expiredBooks();

        try {
            return ExcelReporter.getTotalExcelReport(userCount, loanCount, bookCount, publisherCount,
                    categoryCount, authorCount,unReturnedBooks,expiredBook);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.EXCEL_REPORT_ERROR_MESSAGE);
        }
    }


    public ByteArrayInputStream getBookReport() {
        List<Book> books = bookService.getUnReturnedBooks();
        List<BookDTO> bookDTOS=bookMapper.map(books);
        try {
            return ExcelReporter.getBookExcelReport(bookDTOS);
        } catch (IOException e) {
            throw new RuntimeException(ErrorMessage.EXCEL_REPORT_ERROR_MESSAGE);
        }
    }


    public TotalReportResponse getTotalRaports() {
        TotalReportResponse reportResponse=new TotalReportResponse();
        reportResponse.setTotalAuthor(authorService.getUserCount());
        reportResponse.setTotalBook(bookService.getUserCount());
        reportResponse.setTotalCategory(categoriesService.getUserCount());
        reportResponse.setTotalPublisher(publisherService.getUserCount());
        reportResponse.setTotalUser(userService.getUserCount());
        reportResponse.setTotalLoan(loanService.getUserCount());
        reportResponse.setUnReturnedBooks(bookService.countUnReturnedBooks());
        reportResponse.setExpiredBooks(loanService.expiredBooks());
        return reportResponse;
    }


    public Page<BookDTO> getUnReturnedBooks(Pageable pageable) {
        Page<Book> books=bookService.getUnReturnedBooksByPage(pageable);
        return books.map(bookMapper::bookToBookDto);
    }


    public Page<BookDTO> getExpiredBooks(Pageable pageable) {
        Page<Book> books=loanService.expiredBooksByPage(pageable);
        return books.map(bookMapper::bookToBookDto);
    }

    public Page<UserResponse> getMostBorrowers(Pageable pageable) {
        return loanService.findAllUser(pageable);
    }
}