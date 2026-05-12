package com.example.HCL_MOCK_LMS.controller;

import com.example.HCL_MOCK_LMS.dto.ApiResponse;
import com.example.HCL_MOCK_LMS.dto.BookDTO;
import com.example.HCL_MOCK_LMS.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // POST /books — Add a new book
    @PostMapping
    public ResponseEntity<ApiResponse<BookDTO.Response>> addBook(@Valid @RequestBody BookDTO.Request request) {
        BookDTO.Response book = bookService.addBook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book added successfully", book));
    }

    // GET /books — View all books
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookDTO.Response>>> getAllBooks() {
        return ResponseEntity.ok(ApiResponse.success("Books fetched successfully", bookService.getAllBooks()));
    }

    // GET /books/available — View available books
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<BookDTO.Response>>> getAvailableBooks() {
        return ResponseEntity.ok(ApiResponse.success("Available books fetched", bookService.getAvailableBooks()));
    }

    // GET /books/search?query=xyz — Search by title or author
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookDTO.Response>>> searchBooks(@RequestParam String query) {
        return ResponseEntity.ok(ApiResponse.success("Search results", bookService.searchBooks(query)));
    }

    // GET /books/{id} — Get a specific book
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDTO.Response>> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Book fetched", bookService.getBookById(id)));
    }
}