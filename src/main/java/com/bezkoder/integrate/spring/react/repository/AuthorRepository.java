package com.bezkoder.integrate.spring.react.repository;

import com.bezkoder.integrate.spring.react.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
