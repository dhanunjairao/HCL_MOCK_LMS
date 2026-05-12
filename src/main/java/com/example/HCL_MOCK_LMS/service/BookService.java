package com.example.HCL_MOCK_LMS.service;

import com.example.HCL_MOCK_LMS.dto.BookDTO;
import com.example.HCL_MOCK_LMS.entity.Book;
import com.example.HCL_MOCK_LMS.exception.ResourceNotFoundException;
import com.example.HCL_MOCK_LMS.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookDTO.Response addBook(BookDTO.Request request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .available(true)
                .build();
        return toResponse(bookRepository.save(book));
    }

    public List<BookDTO.Response> getAllBooks() {
        return bookRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<BookDTO.Response> getAvailableBooks() {
        return bookRepository.findByAvailableTrue()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<BookDTO.Response> searchBooks(String query) {
        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public BookDTO.Response getBookById(Long id) {
        return toResponse(findBookOrThrow(id));
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    public Book findBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    private BookDTO.Response toResponse(Book book) {
        return BookDTO.Response.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .available(book.isAvailable())
                .build();
    }
}