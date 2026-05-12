package com.example.HCL_MOCK_LMS.repository;

import com.example.HCL_MOCK_LMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}