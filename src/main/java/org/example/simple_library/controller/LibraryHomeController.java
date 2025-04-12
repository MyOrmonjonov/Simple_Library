package org.example.simple_library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simple_library.entity.Book;
import org.example.simple_library.entity.Borrowing;
import org.example.simple_library.entity.Users;
import org.example.simple_library.repo.BookRepository;
import org.example.simple_library.repo.BorrowingRepository;
import org.example.simple_library.repo.UsersRepository;
import org.example.simple_library.servic.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/home/books")
public class LibraryHomeController {


    private final BookService bookService;
    private final BookRepository bookRepository;
    private final UsersRepository usersRepository;
    private final BorrowingRepository borrowingRepository;

    public LibraryHomeController(BookService bookService, BookRepository bookRepository, UsersRepository usersRepository, BorrowingRepository borrowingRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.usersRepository = usersRepository;
        this.borrowingRepository = borrowingRepository;
    }

   @GetMapping
    public List<Book> getBooks(){
        return bookRepository.findAll();
   }


    @PostMapping("/{bookId}/borrow")
    public ResponseEntity<?> borrowBook(
            @PathVariable Integer bookId,
            @RequestHeader("token") String token) {

        try {

            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid token structure");
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payloadMap = mapper.readValue(payloadJson, Map.class);

            String username = (String) payloadMap.get("sub"); // yoki "username", token qanday tuzilganiga qarab
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Username not found in token");
            }


            Optional<Users> userOptional = usersRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }
            Users user = userOptional.get();


            Optional<Book> bookOptional = bookRepository.findById(bookId);
            if (bookOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Book not found");
            }
            Book book = bookOptional.get();


            if (book.getBorrowedBy() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Book is already borrowed");
            }


            Optional<Borrowing> existingBorrowing = borrowingRepository
                    .findByBookAndUserAndReturnDateIsNull(book, user);
            if (existingBorrowing.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("You have already borrowed this book");
            }


            Borrowing borrowing = new Borrowing();
            borrowing.setBook(book);
            borrowing.setUser(user);
            borrowing.setBorrowDate(LocalDate.now());
            borrowing.setReturnDate(null);


            book.setBorrowedBy(user);
            bookRepository.save(book);

            borrowingRepository.save(borrowing);

            return ResponseEntity.ok("Book borrowed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error borrowing book: " + e.getMessage());
        }
    }

    @GetMapping("/my-borrowed")
    public ResponseEntity<?> getMyBorrowedBooks(@RequestHeader("token") String token) {
        try {
            // Tokenni decode qilib username olish
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createResponse(false, "Invalid token structure"));
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payloadMap = mapper.readValue(payloadJson, Map.class);

            String username = (String) payloadMap.get("sub"); // "sub" yoki "username" keyni tekshirib ko'ring
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createResponse(false, "Username not found in token"));
            }

            // Find user by username
            Optional<Users> userOptional = usersRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createResponse(false, "User not found"));
            }
            Users user = userOptional.get();

            // Get all active borrowings for the user
            List<Borrowing> borrowings = borrowingRepository.findByUserAndReturnDateIsNull(user);

            return ResponseEntity.ok(borrowings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(false, "Error fetching borrowed books: " + e.getMessage()));
        }
    }

    @PostMapping("/return/{borrowingId}")
    public ResponseEntity<?> returnBook(
            @PathVariable Integer borrowingId,
            @RequestHeader("token") String token) {

        try {
            // Tokenni decode qilib username olish
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createResponse(false, "Invalid token structure"));
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payloadMap = mapper.readValue(payloadJson, Map.class);

            String username = (String) payloadMap.get("sub"); // "sub" yoki "username" keyni tekshirib ko'ring
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createResponse(false, "Username not found in token"));
            }

            // Find user by username
            Optional<Users> userOptional = usersRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createResponse(false, "User not found"));
            }
            Users user = userOptional.get();

            // Find borrowing by ID
            Optional<Borrowing> borrowingOptional = borrowingRepository.findById(borrowingId);
            if (borrowingOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createResponse(false, "Borrowing not found"));
            }

            Borrowing borrowing = borrowingOptional.get();

            // Check if the borrowing belongs to the user
            if (!borrowing.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(createResponse(false, "You can only return your own borrowed books"));
            }

            // Check if the book is already returned
            if (borrowing.getReturnDate() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createResponse(false, "This book has already been returned"));
            }

            // Update the borrowing with return date
            borrowing.setReturnDate(LocalDate.now());

            // Update the book's borrowedBy field to null
            borrowing.getBook().setBorrowedBy(null);

            // Save the updated borrowing
            borrowingRepository.save(borrowing);

            return ResponseEntity.ok(createResponse(true, "Book returned successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(false, "Error returning book: " + e.getMessage()));
        }
    }

    private Map<String, Object> createResponse(boolean success, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        return response;
    }


}
