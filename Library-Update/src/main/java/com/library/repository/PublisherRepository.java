package com.library.repository;

import com.library.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface PublisherRepository extends JpaRepository<Publisher,Long> {

}