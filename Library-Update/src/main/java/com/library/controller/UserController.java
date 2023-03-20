package com.library.controller;

import com.library.dto.LoanDTO;
import com.library.dto.UserDTO;
import com.library.dto.request.UserRequest;
import com.library.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping()
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER') or hasRole('STAFF')")
    public ResponseEntity<UserDTO> getAuthenticatedUser(){
    UserDTO user=userService.getAuthenticatedUser();
    return ResponseEntity.ok(user);
    }


    @GetMapping("/user/loans")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF') or hasRole('MEMBER')")
    public ResponseEntity<Page<LoanDTO>> getLoansByUser(@RequestParam(required = false ,value="page",defaultValue ="0") int page,
                                                        @RequestParam(required = false ,value="size",defaultValue ="20") int size,
                                                        @RequestParam(required = false ,value="sort",defaultValue ="id") String prop,
                                                        @RequestParam(required = false ,value="type",defaultValue ="DESC") Sort.Direction type){
        Pageable pageable= PageRequest.of(page, size, Sort.by(type, prop));
        Page<LoanDTO> loans=userService.getLoansByUser(pageable);
        return ResponseEntity.ok(loans);
    }


    @GetMapping("users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Page<UserDTO>> getAllWithPage(@RequestParam(required = false ,value="page",defaultValue ="0") int page,
                                                        @RequestParam(required = false ,value="size",defaultValue ="20") int size,
                                                        @RequestParam(required = false ,value="sort",defaultValue ="createDate") String prop,
                                                        @RequestParam(required = false ,value="type",defaultValue ="DESC") Sort.Direction type){
        Pageable pageable= PageRequest.of(page, size, Sort.by(type, prop));
        Page<UserDTO> users=userService.getAllWithUser(pageable);
        return ResponseEntity.ok(users);
    }


    @GetMapping("users/{Id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("Id") Long id ){
        UserDTO userDTO=userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }


    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<UserDTO> saveUserByStaff(@Valid @RequestBody UserRequest request){
        UserDTO user=userService.saveUserByStaff(request);
        return ResponseEntity.ok(user);
    }


    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<UserDTO> UpdateUser(@PathVariable("id") Long id,@RequestBody UserRequest request){
        UserDTO userDTO=userService.updateUserById(id,request);
        return ResponseEntity.ok(userDTO);
    }


    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("id") Long id){
        UserDTO userDTO=userService.deleteUser(id);
        return ResponseEntity.ok(userDTO);
    }
}