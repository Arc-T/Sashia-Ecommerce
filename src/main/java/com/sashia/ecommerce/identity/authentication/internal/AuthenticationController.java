package com.sashia.ecommerce.identity.authentication.internal;

import com.sashia.ecommerce.identity.authentication.AuthenticationService;
import com.sashia.ecommerce.identity.authentication.dto.AuthenticationResponse;
import com.sashia.ecommerce.identity.authentication.dto.LoginRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/authentication")
class AuthenticationController {

    private final AuthenticationService authenticationService;

    AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest credentials) {
        return ResponseEntity.ok(authenticationService.authenticateAdmin(credentials));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie refreshCookie = new Cookie("token", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        refreshCookie.setAttribute("SameSite", "Strict");
        response.addCookie(refreshCookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('SHOP_OWNER')")
    ResponseEntity<Boolean> validate() {
        return ResponseEntity.ok(true);
    }

//    @PostMapping("/otp")
//     ResponseEntity<Boolean> login(@RequestBody AuthenticationDTO authentication) {
//        return ResponseEntity.ok(authenticationServiceContract.authenticateUser(authentication));
//    }
//
//    @PostMapping("/email")
//     ResponseEntity<Boolean> login(@RequestBody AuthenticationDTO authentication) {
//        return ResponseEntity.ok(authenticationServiceContract.authenticateUser(authentication));
//    }
//
//    @PostMapping("/password")
//     ResponseEntity<Boolean> login(@RequestBody AuthenticationDTO authentication) {
//        return ResponseEntity.ok(authenticationServiceContract.authenticateUser(authentication));
//    }

}
