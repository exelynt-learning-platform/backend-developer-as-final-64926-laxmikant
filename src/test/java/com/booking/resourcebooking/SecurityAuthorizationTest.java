package com.booking.resourcebooking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Test Room",
                                    "type": "MEETING_ROOM",
                                    "capacity": 10
                                }
                                """)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void userCannotCreateResource() throws Exception {
        mockMvc.perform(
                post("/resources")
                        .with(user("regularUser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Test Room",
                                    "type": "MEETING_ROOM",
                                    "capacity": 10
                                }
                                """)
        ).andExpect(status().isForbidden());
    }

    @Test
    void adminCanCreateResource() throws Exception {
        mockMvc.perform(
                post("/resources")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Test Room",
                                    "type": "MEETING_ROOM",
                                    "capacity": 10
                                }
                                """)
        ).andExpect(status().isOk());
    }

    @Test
    void unauthenticatedUserCannotGetReservations() throws Exception {
        mockMvc.perform(get("/reservations"))
                .andExpect(status().isUnauthorized());
    }
}