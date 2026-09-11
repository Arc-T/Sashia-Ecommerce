package com.sashia.shared.util;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SashiaUser extends User {

    private final Long id;
    private final String vipGroup;

    public SashiaUser(String username, @Nullable String password, Collection<? extends GrantedAuthority> authorities, Long id, String vipGroup) {
        this.id = id;
        this.vipGroup = vipGroup;
        super(username, password, authorities);
    }

    public Long getId() {
        return id;
    }

    public String getVipGroup() {
        return vipGroup;
    }

}
