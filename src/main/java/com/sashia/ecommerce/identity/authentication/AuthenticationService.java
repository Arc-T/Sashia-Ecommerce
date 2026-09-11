package com.sashia.ecommerce.identity.authentication;

import com.sashia.ecommerce.identity.authentication.dto.AuthenticationDTO;
import com.sashia.ecommerce.identity.authentication.dto.AuthenticationResponse;
import com.sashia.ecommerce.identity.authentication.dto.LoginRequest;

public interface AuthenticationService {

    AuthenticationResponse authenticateAdmin(LoginRequest request);

    AuthenticationDTO authenticateCustomer(AuthenticationDTO authentication);

}
