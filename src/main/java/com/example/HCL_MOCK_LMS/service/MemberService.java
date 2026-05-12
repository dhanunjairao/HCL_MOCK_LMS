package com.library.service;

import com.library.dto.IssueResponse;
import com.library.dto.MemberRequest;
import com.library.entity.IssueRecord;
import com.library.entity.Member;
import com.library.entity.Role;
import com.library.entity.User;
import com.library.exception.BusinessException;
import com.library.exception.NotFoundException;
import com.library.repository.IssueRecordRepository;
import com.library.repository.MemberRepository;
import com.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final IssueRecordRepository issueRecordRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member create(MemberRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new BusinessException("Username already taken");
        }
        if (memberRepository.existsByEmail(req.email())) {
            throw new BusinessException("Email already registered");
        }

        User user = User.builder()
                .username(req.username())
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(Role.MEMBER)
                .build();
        user = userRepository.save(user);

        Member m = Member.builder()
                .name(req.name())
                .email(req.email())
                .phone(req.phone())
                .joinedOn(LocalDate.now())
                .user(user)
                .build();
        return memberRepository.save(m);
    }

    @Transactional(readOnly = true)
    public List<Member> all() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member get(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<IssueResponse> issuesOf(Long memberId) {
        return issueRecordRepository.findByMemberId(memberId).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IssueResponse> issuesOfUsername(String username) {
        Member m = memberRepository.findByUser_Username(username)
                .orElseThrow(() -> new NotFoundException("Member profile not found"));
        return issuesOf(m.getId());
    }

    private IssueResponse toDto(IssueRecord r) {
        return new IssueResponse(
                r.getId(),
                r.getBook().getId(),
                r.getBook().getTitle(),
                r.getMember().getId(),
                r.getMember().getName(),
                r.getIssuedOn(),
                r.getReturnedOn()
        );
    }
}
