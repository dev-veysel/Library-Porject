package com.library.repository;

import com.library.domain.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories,Long> {

    @Query("select c.sequence from Categories c")
    List<Integer> getAllSequences();
}