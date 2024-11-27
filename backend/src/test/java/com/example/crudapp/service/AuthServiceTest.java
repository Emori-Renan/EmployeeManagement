package com.example.crudapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.example.crudapp.model.UserLogin;
import com.example.crudapp.util.JwtUtil;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    AuthServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticate() {
        String username = "testuser";
        String password = "password";
        String role = "ROLE_USER";

        UserLogin mockUser = new UserLogin();
        mockUser.setUsername(username);
        mockUser.setRole(role);

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        when(mockAuth.getPrincipal()).thenReturn(mockUser);
        when(jwtUtil.generateToken(username, role)).thenReturn("mockToken");

        String token = authService.authenticate(username, password);

        assertNotNull(token);
        assertEquals("mockToken", token);
    }
}
