package com.coding.crab.common;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * @author : yong
 * @fileName : BaseEntity
 * @date : 2024-02-11
 * @description : 엔티티 클래스에서 공통으로 사용되는 칼럼을 관리하기 위한 abstract 클래스입니다. 다른 엔티티 클래스들은 BaseEntity 클래스를 상속받게 됩니다.
 */
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO 1. 등록자 ID(@CreatedBy), 수정자 ID(@LastModifiedBy) 칼럼 추가
    // TODO 2. Entity 분리
    // 참조 : https://velog.io/@kdohyeon/Spring-EnableJpaAuditing

    @Column(name = "CREATE_AT", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createAt;

    @Column(name = "UPDATE_AT", nullable = false)
    @LastModifiedDate
    private LocalDateTime updateAt;
}
