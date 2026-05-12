package com.library.controller;

import com.library.dto.ApiResponse;
import com.library.dto.IssueDTO;
import com.library.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    // POST /issues/issue — Issue a book to a member
    @PostMapping("/issue")
    public ResponseEntity<ApiResponse<IssueDTO.Response>> issueBook(@Valid @RequestBody IssueDTO.IssueRequest request) {
        IssueDTO.Response record = issueService.issueBook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book issued successfully", record));
    }

    // PUT /issues/return/{issueId} — Return an issued book
    @PutMapping("/return/{issueId}")
    public ResponseEntity<ApiResponse<IssueDTO.Response>> returnBook(@PathVariable Long issueId) {
        IssueDTO.Response record = issueService.returnBook(issueId);
        return ResponseEntity.ok(ApiResponse.success("Book returned successfully", record));
    }

    // GET /issues — View all issue records
    @GetMapping
    public ResponseEntity<ApiResponse<List<IssueDTO.Response>>> getAllIssues() {
        return ResponseEntity.ok(ApiResponse.success("Issue records fetched", issueService.getAllIssues()));
    }

    // GET /issues/{issueId} — Get a specific issue record
    @GetMapping("/{issueId}")
    public ResponseEntity<ApiResponse<IssueDTO.Response>> getIssueById(@PathVariable Long issueId) {
        return ResponseEntity.ok(ApiResponse.success("Issue record fetched", issueService.getIssueById(issueId)));
    }
}