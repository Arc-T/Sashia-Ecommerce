package com.sashia.ecommerce.identity.authentication;

import com.sashia.ecommerce.identity.authentication.internal.Role;
import com.sashia.ecommerce.identity.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "user_groups", schema = "identity")
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ******************************************** TABLE RELATIONS *******************************************

    @OneToMany(mappedBy = "userGroup")
    private Set<User> authUsers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_group_roles",
            joinColumns = @JoinColumn(name = "user_group_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    /* **************************** GETTER & SETTERS **********************************/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<User> getAuthUsers() {
        return authUsers;
    }

    public void setAuthUsers(Set<User> authUsers) {
        this.authUsers = authUsers;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
