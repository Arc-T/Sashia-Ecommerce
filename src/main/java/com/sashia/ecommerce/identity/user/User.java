package com.sashia.ecommerce.identity.user;

import com.sashia.ecommerce.identity.authentication.UserGroup;
import com.sashia.ecommerce.identity.user.dto.GenderType;
import com.sashia.ecommerce.identity.user.internal.UserLog;
import com.sashia.ecommerce.identity.user.vip.VipGroup;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

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

    // ******************************************** SECURITY *******************************************

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /* **************************** GETTER & SETTERS **********************************/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public VipGroup getVipGroup() {
        return vipGroup;
    }

    public void setVipGroup(VipGroup vipGroup) {
        this.vipGroup = vipGroup;
    }

    public Set<UserLog> getUserLogs() {
        return userLogs;
    }

    public void setUserLogs(Set<UserLog> userLogs) {
        this.userLogs = userLogs;
    }
}