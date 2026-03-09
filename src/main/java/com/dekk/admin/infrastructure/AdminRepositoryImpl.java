package com.dekk.admin.infrastructure;

import com.dekk.admin.domain.model.Admin;
import com.dekk.admin.domain.repository.AdminRepository;
import com.dekk.admin.infrastructure.jpa.AdminJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {

    private final AdminJpaRepository adminJpaRepository;

    @Override
    public Admin save(Admin admin) {
        return adminJpaRepository.save(admin);
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return adminJpaRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return adminJpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return adminJpaRepository.findById(id);
    }
}
