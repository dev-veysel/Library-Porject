package com.library.repository;


import com.library.domain.Book;

import com.library.domain.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    @Query("select count(b.loanable) from Book b where b.loanable=false")
    Long countUnReturnedBooks();
    @Query("select b from Book b where b.loanable=false")
    List<Book> getUnReturnedBooks();

    @Query("select b from Book b where b.loanable=false")
    Page<Book> findAll(Pageable pageable);

}