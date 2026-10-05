package com.sashia.ecommerce.identity.authentication;

import com.sashia.ecommerce.identity.authentication.internal.Role;
import com.sashia.ecommerce.identity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
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
            schema = "identity",
            name = "user_group_roles",
            joinColumns = @JoinColumn(name = "user_group_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    /* **************************** GETTER & SETTERS **********************************/

}
