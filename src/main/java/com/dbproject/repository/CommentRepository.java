package com.dbproject.repository;

import com.dbproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c"+
            " where c.location.locationId = :locationId")
    List<Comment> findByLocationId(@Param("locationId") String locationId);
}
