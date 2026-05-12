package com.example.HCL_MOCK_LMS.repository;

import com.example.HCL_MOCK_LMS.entity.IssueRecord;
import com.example.HCL_MOCK_LMS.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {

    long countByMemberAndReturnedFalse(Member member);

    List<IssueRecord> findByMemberMemberId(Long memberId);
}