package com.library.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_categorie")
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 80,nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean builtIn=false;

    @Column(nullable = false)
    private Integer sequence;

    @OneToMany(mappedBy = "categorie")
    private List<Book> books;
}