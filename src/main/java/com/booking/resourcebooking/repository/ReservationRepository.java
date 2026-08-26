package com.booking.resourcebooking.repository;

import com.booking.resourcebooking.entity.Reservation;
import com.booking.resourcebooking.entity.ReservationStatus;
import com.booking.resourcebooking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser(User user);

    boolean existsByResourceId(Long resourceId);

    @Query("""
            SELECT r FROM Reservation r
            WHERE (:status IS NULL OR r.status = :status)
            AND (:minPrice IS NULL OR r.price >= :minPrice)
            AND (:maxPrice IS NULL OR r.price <= :maxPrice)
            """)
    Page<Reservation> searchAllReservations(
            @Param("status") ReservationStatus status,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );

    @Query("""
            SELECT r FROM Reservation r
            WHERE r.user = :user
            AND (:status IS NULL OR r.status = :status)
            AND (:minPrice IS NULL OR r.price >= :minPrice)
            AND (:maxPrice IS NULL OR r.price <= :maxPrice)
            """)
    Page<Reservation> searchUserReservations(
            @Param("user") User user,
            @Param("status") ReservationStatus status,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );
}