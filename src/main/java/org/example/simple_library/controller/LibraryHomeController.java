package org.example.simple_library.controller;

import org.example.simple_library.repo.BookRepository;
import org.example.simple_library.servic.BookService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class LibraryHomeController {


    private final BookService bookService;
    private final BookRepository bookRepository;

    public LibraryHomeController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }


}
