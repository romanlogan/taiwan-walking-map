package com.dbproject.api.comment.repository;

import com.dbproject.api.comment.Comment;
import com.dbproject.api.comment.dto.UserComment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CommentMapper extends CommentRepository {

    void save(UserComment userComment);

    List<Comment> findByLocationId(@Param("locationId") String locationId);

    Optional<Comment> findDuplicateComment(@Param("locationId") String locationId, @Param("email") String email);

    Comment findByLocationIdAndEmail(@Param("locationId") String locationId, @Param("email") String email);

    List<Comment> findByMemberEmail(@Param("email") String email);

    void deleteByMemberEmail(@Param("email") String email);
}
