package com.dekk.admin.domain.model;

import com.dekk.admin.domain.exception.AdminBusinessException;
import com.dekk.admin.domain.exception.AdminErrorCode;
import com.dekk.common.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(
        name = "admins",
        uniqueConstraints = {@UniqueConstraint(name = "uk_admin_email", columnNames = "email")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE admins SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Filter(name = "deletedFilter")
public class Admin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private AdminRole adminRole;

    private Admin(String email, String password, AdminRole adminRole) {
        this.email = email;
        this.password = password;
        this.adminRole = adminRole;
    }

    public static Admin create(String email, String encodedPassword, AdminRole adminRole) {
        validate(email, encodedPassword, adminRole);
        return new Admin(email, encodedPassword, adminRole);
    }

    private static void validate(String email, String encodedPassword, AdminRole adminRole) {
        if (email == null || email.isBlank()) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_ADMIN_DATA);
        }
        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_ADMIN_DATA);
        }
        if (adminRole == null) {
            throw new AdminBusinessException(AdminErrorCode.INVALID_ADMIN_DATA);
        }
    }
}
