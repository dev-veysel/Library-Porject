package com.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 70,nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean builtIn=false;

    @Column(length = 17,nullable = false)
    private String isbn;

    private Integer pageCount;

    private Integer publishDate;

    @Column(nullable = false)
    private Boolean loanable=true;

    @Column(length = 6,nullable = false)
    private String shelfCode;

    @Column(nullable = false)
    private Boolean active=true;

    @Column(nullable = false)
    private Boolean feature=false;

    private LocalDateTime createDate=LocalDateTime.now();

    @ManyToOne
    @JoinColumn
    private Author author;

    @ManyToOne
    @JoinColumn
    private Publisher publisher;

    @ManyToOne
    @JoinColumn
    private Categories categorie;

    @OneToOne
    @JoinColumn(name = "imageId")
    private ImageFile image;

    @OneToMany(mappedBy = "book")
    private Set<Loan> loans;
}