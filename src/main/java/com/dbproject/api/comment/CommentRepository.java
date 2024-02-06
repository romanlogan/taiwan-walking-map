package com.dbproject.api.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c"+
            " where c.location.locationId = :locationId" +
            " order by c.regTime desc")
    List<Comment> findByLocationId(@Param("locationId") String locationId);


    @Query("select c from Comment c" +
            " where c.location.locationId = :locationId"+
            " and c.member.email = :email")
    Optional<Comment> findDuplicateComment(@Param("locationId") String locationId,
                                           @Param("email") String email);
}
