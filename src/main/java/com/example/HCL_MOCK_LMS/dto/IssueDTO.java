package com.example.HCL_MOCK_LMS.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class IssueDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class IssueRequest {
        @NotNull(message = "Book ID is required")
        private Long bookId;

        @NotNull(message = "Member ID is required")
        private Long memberId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long issueId;
        private Long bookId;
        private String bookTitle;
        private String bookAuthor;
        private Long memberId;
        private String memberName;
        private LocalDate issueDate;
        private LocalDate returnDate;
        private boolean returned;
    }
}