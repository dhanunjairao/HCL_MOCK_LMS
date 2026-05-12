package com.example.HCL_MOCK_LMS.controller;

import com.example.HCL_MOCK_LMS.dto.ApiResponse;
import com.example.HCL_MOCK_LMS.dto.IssueDTO;
import com.example.HCL_MOCK_LMS.dto.MemberDTO;
import com.example.HCL_MOCK_LMS.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // POST /members — Register a member
    @PostMapping
    public ResponseEntity<ApiResponse<MemberDTO.Response>> registerMember(@Valid @RequestBody MemberDTO.Request request) {
        MemberDTO.Response member = memberService.registerMember(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Member registered successfully", member));
    }

    // GET /members — View all members
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberDTO.Response>>> getAllMembers() {
        return ResponseEntity.ok(ApiResponse.success("Members fetched", memberService.getAllMembers()));
    }

    // GET /members/{id} — View member details
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberDTO.Response>> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Member fetched", memberService.getMemberById(id)));
    }

    // GET /members/{id}/issues — View books issued to a member
    @GetMapping("/{id}/issues")
    public ResponseEntity<ApiResponse<List<IssueDTO.Response>>> getMemberIssues(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Issues fetched", memberService.getBooksIssuedToMember(id)));
    }
}