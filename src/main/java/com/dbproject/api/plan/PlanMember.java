package com.dbproject.api.plan;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class PlanMember extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_member_id")
    private Long id;

    @Column(name = "supply")
    private String supply;      //누가 가져가야할지 정해진 준비

    @ManyToOne(fetch = FetchType.LAZY)      //plan 과 매핑
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email")
    private Member member;



}
