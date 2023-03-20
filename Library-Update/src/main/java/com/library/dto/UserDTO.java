package com.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.library.domain.Loan;
import com.library.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private Integer score;

    private String address;

    private String phone;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDate birthDate;

    private String email;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss")
    private LocalDateTime createDate;

    private Set<String> role;

    public void setRoles(Set<Role> roles) {
        Set<String> roleStr = new HashSet<>();
        roles.forEach(r->{
            roleStr.add(r.getType().getName()); // Customer , Administrator
        });
        this.role=roleStr;
    }
}