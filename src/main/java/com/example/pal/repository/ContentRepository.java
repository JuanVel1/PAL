package com.example.pal.repository;

import com.example.pal.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByCourseId(@Param("courseId") Long courseId);
    Optional<Content> findById(Long id);
}

