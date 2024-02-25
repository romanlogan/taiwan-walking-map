package com.dbproject.api.comment.repository;

import com.dbproject.api.comment.Comment;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    List<Comment> findByLocationId(@Param("locationId") String locationId);

    Optional<Comment> findDuplicateComment(@Param("locationId") String locationId, @Param("email") String email);

    Comment findByLocationIdAndEmail(@Param("locationId") String locationId, @Param("email") String email);

    List<Comment> findByMemberEmail(@Param("email") String email);

    void deleteByMemberEmail(@Param("email") String email);
}
