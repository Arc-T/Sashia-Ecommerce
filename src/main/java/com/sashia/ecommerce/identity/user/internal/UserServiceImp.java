package com.sashia.ecommerce.identity.user.internal;

import com.sashia.ecommerce.identity.user.UserRepository;
import com.sashia.ecommerce.identity.user.UserService;
import com.sashia.ecommerce.identity.user.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserDTO> get(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public long create(UserDTO user) {
        return 0;
    }

    @Override
    public long update(Long id, UserDTO user) {
        return 0;
    }

    @Override
    public Optional<UserDTO> getUserProfileInfo(Long id) {
        return Optional.empty();
    }

}