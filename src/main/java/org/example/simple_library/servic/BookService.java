package org.example.simple_library.servic;

import org.example.simple_library.entity.Book;
import org.example.simple_library.entity.Users;
import org.example.simple_library.repo.BookRepository;
import org.example.simple_library.repo.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UsersRepository usersRepository;

    public BookService(BookRepository bookRepository, UsersRepository usersRepository) {
        this.bookRepository = bookRepository;
        this.usersRepository = usersRepository;
    }


    public List<Book> getAllBooks() {
        return  bookRepository.findAll();
    }

    public Optional<Book> getBookById(Integer id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }

    public Book assignBookToUser(Integer bookId, Integer userId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Optional<Users> userOptional = usersRepository.findById(userId);

        if (bookOptional.isPresent() && userOptional.isPresent()) {
            Book book = bookOptional.get();
            Users user = userOptional.get();

            book.setBorrowedBy(user);
            return bookRepository.save(book);
        }

        return null;
    }
    public Book returnBook(Integer bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setBorrowedBy(null);
            return bookRepository.save(book);
        }

        return null;
    }
}
