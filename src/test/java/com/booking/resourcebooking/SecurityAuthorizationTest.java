package com.booking.resourcebooking;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedUserCannotCreateResource() throws Exception {

        mockMvc.perform(
                post("/resources")
                        .contentType("application/json")
                        .content("""
                                {
                                    "name": "Test Room",
                                    "type": "ROOM",
                                    "capacity": 10
                                }
                                """)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void adminCanCreateResource() throws Exception {

        mockMvc.perform(
                post("/resources")
                        .with(user("admin").roles("ADMIN"))
                        .contentType("application/json")
                        .content("""
                            {
                                "name": "Test Room",
                                "type": "MEETING_ROOM",
                                "capacity": 10
                            }
                            """)
        ).andExpect(status().isOk());
    }
}