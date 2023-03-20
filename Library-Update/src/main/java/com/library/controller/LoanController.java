package com.library.controller;

import com.library.domain.User;
import com.library.dto.LoanDTO;
import com.library.dto.LoanResponse;
import com.library.dto.request.LoanRequest;
import com.library.dto.request.LoanUpdateRequest;
import com.library.service.LoanService;
import com.library.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final UserService userService;

    public LoanController(LoanService loanService, UserService userService) {
        this.loanService = loanService;
        this.userService = userService;
    }


    @PostMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<LoanDTO> createLoan(@RequestParam("userId") Long userId,
                                              @RequestParam("bookId") Long bookId,
                                              @Valid @RequestBody LoanRequest request) {
        User user = userService.findById(userId);
        LoanDTO loanDTO = loanService.createLoan(request, user, bookId);
        return new ResponseEntity<>(loanDTO, HttpStatus.CREATED);
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<List<LoanDTO>> getAll() {
        List<LoanDTO> loans = loanService.findAll();
        return ResponseEntity.ok(loans);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<LoanDTO> getAllPage(@PathVariable("id") Long id) {
        LoanDTO loans = loanService.findLoanId(id);
        return ResponseEntity.ok(loans);
    }


    @GetMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Page<LoanResponse>> getAuthUserLoanPage(@RequestParam(required = false,value="page",defaultValue ="0") int page,
                                                              @RequestParam(required = false,value="size",defaultValue ="20") int size,
                                                              @RequestParam(required = false,value="sort",defaultValue ="loanDate") String prop,
                                                              @RequestParam(required = false,value="type",defaultValue ="DESC") Sort.Direction type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));
        Page<LoanResponse> loanDTOS = loanService.getAuthUserLoanPage(pageable);
        return ResponseEntity.ok(loanDTOS);
    }


    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Page<LoanDTO>> getLoanPageByUser(@PathVariable("userId")Long userId,@RequestParam(required = false,value="page",defaultValue ="0") int page,
                                                                  @RequestParam(required = false,value="size",defaultValue ="20") int size,
                                                                  @RequestParam(required = false,value="sort",defaultValue ="loanDate") String prop,
                                                                  @RequestParam(required = false,value="type",defaultValue ="DESC") Sort.Direction type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));
        Page<LoanDTO> loanDTOS = loanService.getLoanPageByUser(pageable,userId);
        return ResponseEntity.ok(loanDTOS);
    }


    @GetMapping("/book/{bookId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Page<LoanDTO>> getLoanPageByBook(@PathVariable("bookId")Long bookId,@RequestParam(required = false,value="page",defaultValue ="0") int page,
                                                           @RequestParam(required = false,value="size",defaultValue ="20") int size,
                                                           @RequestParam(required = false,value="sort",defaultValue ="loanDate") String prop,
                                                           @RequestParam(required = false,value="type",defaultValue ="DESC") Sort.Direction type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));
        Page<LoanDTO> loanDTOS = loanService.getLoanPageByBook(pageable,bookId);
        return ResponseEntity.ok(loanDTOS);
    }


    @GetMapping("/loan/{id}")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<LoanDTO> findLoanByUserAndId(@PathVariable("id") Long id){
        LoanDTO dto=loanService.findLoanByUserAndId(id);
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<LoanDTO> updateLoan(@PathVariable("id") Long id , @Valid @RequestBody LoanUpdateRequest request){
        LoanDTO loanDTO=loanService.updateLoan(id,request);
        return ResponseEntity.ok(loanDTO);
    }
}