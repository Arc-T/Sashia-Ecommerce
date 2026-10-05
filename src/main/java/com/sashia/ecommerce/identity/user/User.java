package com.sashia.ecommerce.identity.user;

import com.sashia.ecommerce.identity.authentication.UserGroup;
import com.sashia.ecommerce.identity.user.dto.GenderType;
import com.sashia.ecommerce.identity.user.internal.UserLog;
import com.sashia.ecommerce.identity.user.vip.VipGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "identity")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private String phone;

    private String email;

    private String password;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @SoftDelete
    private LocalDateTime deletedAt;

    // ************************************** FOREIGN-KEY RELATIONS *******************************************

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserGroup userGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private VipGroup vipGroup;

    // ******************************************** TABLE RELATIONS *******************************************

    @OneToMany(fetch = FetchType.LAZY)
    private Set<UserLog> userLogs;

    /* **************************** GETTER & SETTERS **********************************/

}