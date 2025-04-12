package org.example.simple_library.repo;

import org.example.simple_library.entity.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookGenreRepository extends JpaRepository<BookGenre, Integer> {
}