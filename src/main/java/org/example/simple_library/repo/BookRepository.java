package org.example.simple_library.repo;

import org.example.simple_library.dto.BookDto;
import org.example.simple_library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpEntity;

public interface BookRepository extends JpaRepository<Book, Integer> {

}