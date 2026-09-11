package com.sashia.shared.config;

import com.sashia.ecommerce.identity.authentication.UserGroup;
import com.sashia.ecommerce.identity.user.User;
import com.sashia.ecommerce.identity.user.UserRepository;
import com.sashia.shared.util.SashiaUser;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component(value = "userDetailsService")
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    DefaultUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    @Cacheable(cacheNames = "user-auth", unless = "#result == null")
    public UserDetails loadUserByUsername(@NonNull String username) {
        User user = userRepository.findByPhoneWithAuthorities(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new SashiaUser(
                username,
                user.getPassword(),
                getAuthorities(user.getUserGroup()),
                user.getId(),
                user.getVipGroup().name()
        );
    }

    @NullMarked
    private Collection<? extends GrantedAuthority> getAuthorities(UserGroup userGroup) {
        return userGroup.getRoles()
                .stream()
                .flatMap(role -> role.permissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .toList();
    }

}