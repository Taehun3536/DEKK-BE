package com.dekk.user.infrastructure.jpa;

import com.dekk.user.domain.model.User;
import com.dekk.user.domain.model.enums.Provider;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByProviderAndProviderId(Provider provider, String providerId);

    @EntityGraph(attributePaths = {"profile"})
    Optional<User> findWithProfileById(Long id);
}
