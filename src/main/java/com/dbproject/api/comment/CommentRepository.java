package com.dbproject.api.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("select c from Comment c" +
            " where c.location.locationId = :locationId"+
            " and c.member.email = :email")
    Comment findByLocationIdAndEmail(@Param("locationId") String locationId,
                                           @Param("email") String email);

    @Query("select c from Comment c" +
            " where c.member.email = :email" +
            " order by c.regTime desc")
    List<Comment> findByMemberEmail(@Param("email") String email);

    /**
     * Whenever you are trying to modify a record in db, you have to mark it as @Modifying, which instruct Spring that it can modify existing records.
     *
     * The modifying queries can only use void or int/Integer as return type, otherwise it will throw an exception.
     */
    @Modifying
    @Query("delete from Comment c" +
            " where c.member.email = :email")
    void deleteByMemberEmail(@Param("email") String email);
}

