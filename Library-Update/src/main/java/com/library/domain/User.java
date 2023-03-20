package com.library.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30,nullable = false)
    private String firstName;

    @Column(length = 30,nullable = false)
    private String lastName;

    private Integer score=0;

    @Column(length = 100,nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    private LocalDate birthDate;

    @Column(length = 80,nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createDate=LocalDateTime.now();

    private String resetPasswordCode;

    @Column(nullable = false)
    private Boolean builtIn=false;

    @ManyToMany
    @JoinTable(name="t_user_role",joinColumns = @JoinColumn(name = "userId"),inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<Role>();

    @OneToMany(mappedBy = "user")
    private Set<Loan> loans;
}