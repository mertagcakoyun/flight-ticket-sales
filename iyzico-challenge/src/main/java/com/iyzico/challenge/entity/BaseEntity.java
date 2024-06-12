package com.iyzico.challenge.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @CreatedDate
  @Column(name = "createdDate", nullable = false, updatable = false)
  protected LocalDateTime createdDate;

  @LastModifiedDate
  @Column(name = "modifiedDate")
  protected LocalDateTime modifiedDate;

  @Version
  private Integer version;
}
