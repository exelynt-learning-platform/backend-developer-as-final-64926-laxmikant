package com.booking.resourcebooking.controller;

import com.booking.resourcebooking.dto.ReservationRequest;
import com.booking.resourcebooking.dto.ReservationResponse;
import com.booking.resourcebooking.entity.ReservationStatus;
import com.booking.resourcebooking.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ReservationResponse createReservation(
            @Valid @RequestBody ReservationRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        return reservationService.createReservation(request, username);
    }

    @GetMapping
    public Page<ReservationResponse> getReservations(
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String sort,
            Authentication authentication) {

        String username = authentication.getName();

        Pageable pageable;
        if (sort != null && !sort.isBlank()) {
            String[] sortParts = sort.split(",");
            String property = sortParts[0].trim();
            Sort.Direction direction = (sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1].trim()))
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            pageable = PageRequest.of(page, size, Sort.by(direction, property));
        } else {
            pageable = PageRequest.of(page, size);
        }

        return reservationService.searchReservations(
                username,
                status,
                minPrice,
                maxPrice,
                pageable
        );
    }

    @GetMapping("/{id}")
    public ReservationResponse getReservationById(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        return reservationService.getReservationById(id, username);
    }

    @PutMapping("/{id}")
    public ReservationResponse updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        return reservationService.updateReservation(id, request, username);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        reservationService.deleteReservation(id, username);
    }
}