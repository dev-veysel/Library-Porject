package com.library.service;

import com.library.domain.*;
import com.library.dto.LoanDTO;
import com.library.dto.LoanResponse;
import com.library.dto.request.LoanRequest;
import com.library.dto.request.LoanUpdateRequest;
import com.library.dto.response.UserResponse;
import com.library.exception.BadRequestException;
import com.library.exception.ResourceNotFoundException;
import com.library.exception.message.ErrorMessage;
import com.library.mapper.BookMapper;
import com.library.mapper.LoanMapper;
import com.library.repository.LoanRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final BookMapper bookMapper;
    private final BookService bookService;
    private final UserService userService;

    public LoanService(LoanRepository loanRepository, LoanMapper loanMapper, BookMapper bookMapper, BookService bookService,@Lazy UserService userService) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
        this.bookMapper = bookMapper;
        this.bookService = bookService;
        this.userService = userService;
    }


    public Page<LoanDTO> getLoansByPage(User user, Pageable pageable) {
        Page<Loan> loans =loanRepository.findAllByUser(user, pageable);
        return loans.map(loanMapper::loanToLonaDTO);
    }


    public LoanDTO createLoan(LoanRequest request,User user,Long bookId) {
        Book book=bookService.findById(bookId);
        if(!book.getLoanable()){
            throw new BadRequestException(ErrorMessage.LOANABLE_EXCEPTION);
        }
        if(user.getLoans().size()>5){
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);
        }
        if(user.getLoans().size()==4&&user.getScore()<2){
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);
        }
        if(user.getLoans().size()==3&&user.getScore()<1){
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);
        }
        if(user.getLoans().size()==2&&user.getScore()<0){
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);
        }
        if(user.getLoans().size()==1&&user.getScore()<-1){
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);
        }
        if(!user.getLoans().isEmpty()){
            Set<Loan> userloan=loanRepository.findAllByUser(user);
            for(Loan userloans : userloan)
       if(userloans.getExpireDate().isBefore(LocalDateTime.now())&&!userloans.getBook().getLoanable()){
            throw new BadRequestException(ErrorMessage.RETURNDATE_EXCEPTION);}
        }


        Loan loan=new Loan();
        if(user.getScore()<-1){
            loan.setExpireDate(LocalDateTime.now().plusDays(3));
        }else if(user.getScore()==-1){
            loan.setExpireDate(LocalDateTime.now().plusDays(6));
        }else if(user.getScore()==0){
            loan.setExpireDate(LocalDateTime.now().plusDays(10));
        }else if(user.getScore()==1){
            loan.setExpireDate(LocalDateTime.now().plusDays(15));
        }else {
            loan.setExpireDate(LocalDateTime.now().plusDays(20));
        }

        loan.setLoanDate(LocalDateTime.now());
        loan.setBook(book);
        loan.setUser(user);
        loan.setNotes(request.getNotes());

        loanRepository.save(loan);
        book.setLoanable(false);
        bookService.updateBookLoanable(book);

        Loan loanpj=loanRepository.findByBook(book).orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,book)));;
        LoanDTO loanDTO=new LoanDTO();
        loanDTO.setLoanDate(loanpj.getLoanDate());
        loanDTO.setId(loanpj.getId());
        loanDTO.setExpireDate(loanpj.getExpireDate());
        loanDTO.setReturnDate(loanpj.getReturnDate());
        loanDTO.setNotes(loanpj.getNotes());
        loanDTO.setUserId(user.getId());
        loanDTO.setBook(bookMapper.bookToBookDto(book));
        return loanDTO;
    }


    public Long getUserCount() {
        return loanRepository.count();
    }


    public List<LoanDTO> findAll() {
        List<Loan> loans=loanRepository.findAll();
        List<LoanDTO> loanDTOS=new ArrayList<>();
        for(Loan loan:loans){
            loanDTOS.add(convertLoan(loan));}
        return loanDTOS;
    }


    public LoanDTO findLoanId(Long id) {
        Loan loan=findById(id);
        return convertLoan(loan);
    }


    private Loan findById(Long id) {
        return loanRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id)));
    }


    public LoanDTO convertLoan(Loan loan){
        LoanDTO loanDTO=new LoanDTO();
        loanDTO.setId(loan.getId());
        loanDTO.setLoanDate(loan.getLoanDate());
        loanDTO.setNotes(loan.getNotes());
        loanDTO.setReturnDate(loan.getReturnDate());
        loanDTO.setExpireDate(loan.getExpireDate());
        loanDTO.setUserId(loan.getUser().getId());
        loanDTO.setBook(bookMapper.bookToBookDto(loan.getBook()));
        return loanDTO;
    }


    public LoanResponse convertLoanResponse(Loan loan){
        LoanResponse loanDTO=new LoanResponse();
        loanDTO.setId(loan.getId());
        loanDTO.setLoanDate(loan.getLoanDate());
        loanDTO.setReturnDate(loan.getReturnDate());
        loanDTO.setExpireDate(loan.getExpireDate());
        loanDTO.setUserId(loan.getUser().getId());
        loanDTO.setBook(bookMapper.bookToBookDto(loan.getBook()));
        return loanDTO;
    }


    public Page<LoanResponse> getLoansByPages(User user, Pageable pageable) {
        Page<Loan> loans =loanRepository.findAllByUser(user,pageable);
        return loans.map(this::convertLoanResponse);
    }


    public Page<LoanResponse> getAuthUserLoanPage(Pageable pageable) {
        User user = userService.getCurrentlyUser();
        return getLoansByPages(user, pageable);
    }


    public Page<LoanDTO> getLoanPageByUser(Pageable pageable, Long userId) {
        User user =userService.findById(userId);
        Page<Loan> loans=loanRepository.findAllByUser(user,pageable);
        return loans.map(this::convertLoan);
    }


    public Page<LoanDTO> getLoanPageByBook(Pageable pageable, Long bookId) {
        Book book =bookService.findById(bookId);
        Page<Loan> loans=loanRepository.findAllByBook(book,pageable);
        return loans.map(this::convertLoan);
    }


    public LoanDTO findLoanByUserAndId(Long id) {
        User user=userService.getCurrentlyUser();
        Loan loan=loanRepository.findByUserandid(user.getId(),id);
        if(loan==null){
            throw new BadRequestException(String.format(ErrorMessage.LOAN_NOT_FOUND_EXCEPTION,id));
        }
        return convertLoan(loan);
    }


    public LoanDTO updateLoan(Long id,LoanUpdateRequest request) {
        Loan loan=findById(id);
        if(request.getReturnDate()==null){
            loan.setExpireDate(request.getExpireDate());
            loan.setNotes(request.getNotes());
        }else {
            Book book=bookService.findById(loan.getBook().getId());
            book.setLoanable(true);
            bookService.updateBookLoanable(book);
            loan.setReturnDate(request.getReturnDate());
            User user =userService.findById(loan.getUser().getId());
            if(request.getReturnDate().isBefore(loan.getExpireDate())){
                user.setScore(user.getScore()+1);
            }else {user.setScore(user.getScore()-1);}
            userService.updateScore(user);
            if(request.getNotes()!=null){
                loan.setNotes(request.getNotes());
            }
            if(request.getExpireDate()!=null){
                loan.setExpireDate(request.getExpireDate());
            }
        }
        loanRepository.save(loan);
        return convertLoan(loan);
    }

    public Long expiredBooks() {
        LocalDateTime now=LocalDateTime.now();
        return loanRepository.getExpiredBooks(now);
    }

    public Page<Book> expiredBooksByPage(Pageable pageable) {
        LocalDateTime now=LocalDateTime.now();
        return loanRepository.findAllByBookAndPage(pageable,now);
    }

    public Page<UserResponse> findAllUser(Pageable pageable) {
        Page<User> users = loanRepository.findAllByUserPage(pageable);
        return users.map(userService::userToUserResponse);
    }

}