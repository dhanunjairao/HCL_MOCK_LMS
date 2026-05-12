package com.example.HCL_MOCK_LMS.service;

import com.example.HCL_MOCK_LMS.dto.IssueDTO;
import com.example.HCL_MOCK_LMS.dto.MemberDTO;
import com.example.HCL_MOCK_LMS.entity.IssueRecord;
import com.example.HCL_MOCK_LMS.entity.Member;
import com.example.HCL_MOCK_LMS.exception.BusinessException;
import com.example.HCL_MOCK_LMS.exception.ResourceNotFoundException;
import com.example.HCL_MOCK_LMS.repository.IssueRecordRepository;
import com.example.HCL_MOCK_LMS.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final IssueRecordRepository issueRecordRepository;

    @Transactional
    public MemberDTO.Response registerMember(MemberDTO.Request request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already registered: " + request.getEmail());
        }

        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        return toResponse(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public List<MemberDTO.Response> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberDTO.Response getMemberById(Long id) {
        return toResponse(findMemberOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<IssueDTO.Response> getBooksIssuedToMember(Long memberId) {
        findMemberOrThrow(memberId); // verify member exists
        return issueRecordRepository.findByMemberMemberId(memberId).stream()
                .map(this::toIssueResponse)
                .collect(Collectors.toList());
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    public Member findMemberOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    private MemberDTO.Response toResponse(Member member) {
        return MemberDTO.Response.builder()
                .memberId(member.getMemberId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

    private IssueDTO.Response toIssueResponse(IssueRecord record) {
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