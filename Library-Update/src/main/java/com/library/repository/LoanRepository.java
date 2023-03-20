package com.library.repository;

import com.library.domain.Book;
import com.library.domain.Loan;
import com.library.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {

    @EntityGraph(attributePaths = {"book","user"})
    Page<Loan> findAllByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"book","user"})
    Optional<Loan> findByUser(User user);

    boolean existsByUser(User user);

    @EntityGraph(attributePaths = {"book","user"})
    Optional<Loan> findByBook(Book book);

    @EntityGraph(attributePaths = {"book","user"})
    Set<Loan> findAllByUser(User user);

    @EntityGraph(attributePaths = {"book"})
    Page<Loan> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"book","user"})
    Page<Loan> findAllByBook(Book book, Pageable pageable);

    @Query("select l from Loan l where l.user.id=:userId and l.id=:id")
    Loan findByUserandid(@Param("userId") Long userId,@Param("id") Long id);

    @Query("select count(l.book) from Loan l where l.expireDate<:now ")
    Long getExpiredBooks(@Param("now") LocalDateTime now);

    @Query("select l.book from Loan l where l.expireDate<:now ")
    Page<Book> findAllByBookAndPage(Pageable pageable,@Param("now") LocalDateTime now);

    @Query("select l.user From Loan l")
    Page<User> findAllByUserPage(Pageable pageable);
}