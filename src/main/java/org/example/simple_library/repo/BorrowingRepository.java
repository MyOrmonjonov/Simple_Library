package org.example.simple_library.repo;

import org.example.simple_library.entity.Book;
import org.example.simple_library.entity.Borrowing;
import org.example.simple_library.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowingRepository extends JpaRepository<Borrowing, Integer> {
    Optional<Borrowing> findByBookAndUserAndReturnDateIsNull(Book book, Users user);

    List<Borrowing> findByUserAndReturnDateIsNull(Users user);
}