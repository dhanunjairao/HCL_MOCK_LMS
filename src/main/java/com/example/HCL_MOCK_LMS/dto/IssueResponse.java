package com.example.HCL_MOCK_LMS.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueResponse {
    private Long id;
    private Long bookId;
    private Long memberId;
    private LocalDate issuedOn;
    private LocalDate returnedOn;
    private Double fine;
}