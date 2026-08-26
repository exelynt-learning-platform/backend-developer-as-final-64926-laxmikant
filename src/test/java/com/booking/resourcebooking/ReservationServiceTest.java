package com.booking.resourcebooking;

import com.booking.resourcebooking.entity.Reservation;
import com.booking.resourcebooking.entity.Role;
import com.booking.resourcebooking.entity.User;
import com.booking.resourcebooking.exception.ResourceNotFoundException;
import com.booking.resourcebooking.repository.ReservationRepository;
import com.booking.resourcebooking.repository.ResourceRepository;
import com.booking.resourcebooking.repository.UserRepository;
import com.booking.resourcebooking.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private UserRepository userRepository;

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reservationService = new ReservationService(
                reservationRepository,
                resourceRepository,
                userRepository
        );
    }

    @Test
    void userCannotAccessAnotherUsersReservation() {

        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("Laxmikant");
        currentUser.setRole(Role.USER);

        User reservationOwner = new User();
        reservationOwner.setId(2L);
        reservationOwner.setUsername("otherUser");
        reservationOwner.setRole(Role.USER);

        Reservation reservation = new Reservation();
        reservation.setId(100L);
        reservation.setUser(reservationOwner);

        when(reservationRepository.findById(100L))
                .thenReturn(Optional.of(reservation));

        when(userRepository.findByUsername("Laxmikant"))
                .thenReturn(Optional.of(currentUser));

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservationService.getReservationById(
                        100L,
                        "Laxmikant"
                )
        );
    }
}