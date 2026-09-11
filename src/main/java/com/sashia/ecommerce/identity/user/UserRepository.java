package com.sashia.ecommerce.identity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT u FROM User u
                   JOIN FETCH u.vipGroup
                   JOIN FETCH u.userGroup ug
                        JOIN FETCH ug.roles r
                             JOIN FETCH r.permissions
            WHERE u.phone = :phone
            """)
    Optional<User> findByPhoneWithAuthorities(String phone);

}
