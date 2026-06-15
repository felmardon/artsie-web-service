package com.felmardon.artsie.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * An admin user who can manage portfolio content through the CMS.
 * For a personal portfolio this will typically be a single user,
 * but the schema supports multiple admins if needed.
 */
@Entity
@Table(name = "admin_users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class AdminUser extends BaseEntity {

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String passwordHash;

    @Email
    @Size(max = 150)
    @Column(length = 150)
    private String email;

    @Column(nullable = false)
    private boolean enabled = true;

    public AdminUser() {}

    // ---- Getters and Setters ----

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
