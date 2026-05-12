package com.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

// ── Book DTOs ────────────────────────────────────────────────────────────────

public class BookDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Author is required")
        private String author;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long bookId;
        private String title;
        private String author;
        private boolean available;
    }
}