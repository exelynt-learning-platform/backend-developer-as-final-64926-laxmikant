package com.booking.resourcebooking.repository;

import com.booking.resourcebooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //when someone login we have to find that cardentials in db
    Optional<User> findByUsername(String username);
}