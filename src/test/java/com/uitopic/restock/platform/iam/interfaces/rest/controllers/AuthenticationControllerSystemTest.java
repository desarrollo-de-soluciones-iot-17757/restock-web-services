package com.uitopic.restock.platform.iam.interfaces.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uitopic.restock.platform.iam.infrastructure.persistence.mongodb.repositories.UserPersistenceRepository;
import com.uitopic.restock.platform.iam.interfaces.rest.resources.SignInResource;
import com.uitopic.restock.platform.iam.interfaces.rest.resources.SignUpResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthenticationControllerSystemTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserPersistenceRepository userPersistenceRepository;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @BeforeEach
        void setUp() {
                userPersistenceRepository.deleteAll();
        }

        @Test
        void signUp_validPayload_returns201WithUserData() throws Exception {
                SignUpResource resource = new SignUpResource("Valid Business", "signup-valid@example.com",
                                "password123", "RESTAURANTADMIN");

                mockMvc.perform(post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(resource)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.email").value("signup-valid@example.com"))
                                .andExpect(jsonPath("$.role").value("RESTAURANTADMIN"));
        }

        @Test
        void signUp_duplicateEmail_returns409WithMessage() throws Exception {
                SignUpResource resource = new SignUpResource("Dup Business", "dup@example.com", "password123",
                                "RESTAURANTADMIN");

                // First register
                mockMvc.perform(post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(resource)))
                                .andExpect(status().isCreated());

                // Try registering again
                mockMvc.perform(post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(resource)))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message").value("Email already registered: dup@example.com"));
        }

        @Test
        void signUp_missingRequiredFields_returns400() throws Exception {
                SignUpResource resource = new SignUpResource("", "", "", "");

                mockMvc.perform(post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(resource)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void signUp_invalidRole_returns400() throws Exception {
                SignUpResource resource = new SignUpResource("Business", "invalid-role@example.com", "password123",
                                "UNKNOWN_ROLE");

                mockMvc.perform(post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(resource)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Unknown role: UNKNOWN_ROLE"));
        }

        @Test
        void signIn_validCredentials_returns200WithNonBlankToken() throws Exception {
                SignUpResource signUp = new SignUpResource("SignIn Business", "signin-valid@example.com", "password123",
                                "RETAILADMIN");
                mockMvc.perform(post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUp)))
                                .andExpect(status().isCreated());

                SignInResource signIn = new SignInResource("signin-valid@example.com", "password123");

                mockMvc.perform(post("/api/v1/auth/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signIn)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.email").value("signin-valid@example.com"))
                                .andExpect(jsonPath("$.role").value("RETAILADMIN"))
                                .andExpect(jsonPath("$.token").value(not(emptyOrNullString())));
        }

        @Test
        void signIn_wrongPassword_returns401() throws Exception {
                SignUpResource signUp = new SignUpResource("Wrong Pwd Business", "wrong-pwd@example.com", "password123",
                                "RESTAURANTADMIN");
                mockMvc.perform(post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUp)))
                                .andExpect(status().isCreated());

                SignInResource signIn = new SignInResource("wrong-pwd@example.com", "wrongpassword");

                mockMvc.perform(post("/api/v1/auth/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signIn)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void signIn_unknownEmail_returns401() throws Exception {
                SignInResource signIn = new SignInResource("unknown-signin@example.com", "password123");

                mockMvc.perform(post("/api/v1/auth/sign-in")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signIn)))
                                .andExpect(status().isUnauthorized());
        }
}
