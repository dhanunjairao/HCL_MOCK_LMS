package com.library.service;

import com.library.dto.IssueDTO;
import com.library.entity.Book;
import com.library.entity.IssueRecord;
import com.library.entity.Member;
import com.library.exception.BusinessException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.IssueRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {

    private static final int MAX_BOOKS_PER_MEMBER = 3;

    private final IssueRecordRepository issueRecordRepository;
    private final BookService bookService;
    private final MemberService memberService;
    private final BookRepository bookRepository;

    @Transactional
    public IssueDTO.Response issueBook(IssueDTO.IssueRequest request) {
        Book book = bookService.findBookOrThrow(request.getBookId());
        Member member = memberService.findMemberOrThrow(request.getMemberId());

        // Business Rule 1: Book must be available
        if (!book.isAvailable()) {
            throw new BusinessException("Book '" + book.getTitle() + "' is not available for issuing");
        }

        // Business Rule 2: Member cannot have more than 3 active issues
        long activeIssues = issueRecordRepository.countByMemberAndReturnedFalse(member);
        if (activeIssues >= MAX_BOOKS_PER_MEMBER) {
            throw new BusinessException(
                    "Member '" + member.getName() + "' already has " + MAX_BOOKS_PER_MEMBER
                    + " books issued. Please return a book before issuing a new one.");
        }

        // Mark book as unavailable
        book.setAvailable(false);
        bookRepository.save(book);

        // Create issue record
        IssueRecord record = IssueRecord.builder()
                .book(book)
                .member(member)
                .issueDate(LocalDate.now())
                .returned(false)
                .build();

        return toResponse(issueRecordRepository.save(record));
    }

    @Transactional
    public IssueDTO.Response returnBook(Long issueId) {
        IssueRecord record = issueRecordRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue record not found with id: " + issueId));

        if (record.isReturned()) {
            throw new BusinessException("This book has already been returned (Issue ID: " + issueId + ")");
        }

        // Mark book as available again
        Book book = record.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        // Update issue record
        record.setReturned(true);
        record.setReturnDate(LocalDate.now());

        return toResponse(issueRecordRepository.save(record));
    }

    public List<IssueDTO.Response> getAllIssues() {
        return issueRecordRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public IssueDTO.Response getIssueById(Long issueId) {
        IssueRecord record = issueRecordRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue record not found with id: " + issueId));
        return toResponse(record);
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private IssueDTO.Response toResponse(IssueRecord record) {
        return IssueDTO.Response.builder()
                .issueId(record.getIssueId())
                .bookId(record.getBook().getBookId())
                .bookTitle(record.getBook().getTitle())
                .bookAuthor(record.getBook().getAuthor())
                .memberId(record.getMember().getMemberId())
                .memberName(record.getMember().getName())
                .issueDate(record.getIssueDate())
                .returnDate(record.getReturnDate())
                .returned(record.isReturned())
                .build();
    }
}