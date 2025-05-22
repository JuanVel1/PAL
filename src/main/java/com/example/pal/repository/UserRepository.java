package com.example.pal.repository;

import com.example.pal.model.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.pal.model.User;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @EntityGraph(attributePaths = "roles")
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles")
    List<User> findAllWithRoles();

    List<User> findUsersByRoles(@NotNull(message = "El usuario debe tener al menos un rol") @Size(min = 1, message = "El usuario debe tener al menos un rol") Set<Role> roles);

}
