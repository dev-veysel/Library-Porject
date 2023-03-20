package com.library.controller;

import com.library.dto.RegisterDTO;
import com.library.dto.request.RegisterRequest;
import com.library.dto.request.SigninRequest;
import com.library.dto.response.SigninResponse;
import com.library.security.jwt.JwtUtils;
import com.library.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping()
public class UserJwtController {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserJwtController(JwtUtils jwtUtils, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterDTO> register(@Valid @RequestBody RegisterRequest registerRequest){
        RegisterDTO registerDTO=userService.register(registerRequest);
        return ResponseEntity.ok(registerDTO);
    }


    @PostMapping("/signin")
    public ResponseEntity<SigninResponse > signin(@Valid @RequestBody SigninRequest signinRequest){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword());
        Authentication authentication=
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();
        String jwtToken=jwtUtils.generateJwtToken(userDetails);
        SigninResponse signinResponse=new SigninResponse(jwtToken);
        return ResponseEntity.ok(signinResponse);
    }
}