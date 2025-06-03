package com.dbproject.api.comment.repository;

import com.dbproject.api.comment.Comment;
import com.dbproject.api.comment.dto.CommentDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>{

    @Query("select c from Comment c" +
            " join fetch c.member"+
            " where c.location.locationId = :locationId" +
            " order by c.regTime desc")
    List<Comment> findByLocationId(@Param("locationId") String locationId);

    @Query("select new com.dbproject.api.comment.dto.CommentDto(c.id,c.content,m.email,m.name,c.regTime,mi.imgUrl)" +
            " from Comment c" +
            " join Member m on c.member.email = m.email" +
            " left outer join MemberImg mi on mi.member.email = c.member.email"+
            " where c.location.locationId = :locationId" +
            " order by c.regTime desc")
    List<CommentDto> findByLocationIdWithJoin(@Param("locationId") String locationId, Pageable pageable);


    @Query("select c from Comment c" +
            " where c.location.locationId = :locationId"+
            " and c.member.email = :email")
    Optional<Comment> findDuplicateComment(@Param("locationId") String locationId, @Param("email") String email);

    @Query("select c from Comment c" +
            " where c.location.locationId = :locationId"+
            " and c.member.email = :email")
    Comment findByLocationIdAndEmail(@Param("locationId") String locationId, @Param("email") String email);

    @Query("select c from Comment c" +
            " where c.member.email = :email" +
            " order by c.regTime desc")
    List<Comment> findByMemberEmail(@Param("email") String email);

    @Modifying
    @Query("delete from Comment c" +
            " where c.member.email = :email")
    void deleteByMemberEmail(@Param("email") String email);
}

