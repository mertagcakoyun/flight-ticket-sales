package com.iyzico.challenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * BaseEntity is the base class for other entity classes and contains fundamental fields.
 * It includes basic fields such as creation date, modification date, and version.
 * This class is used to provide common properties for other entity classes.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "createdDate")
    @JsonIgnore
    protected LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modifiedDate")
    @JsonIgnore
    protected LocalDateTime modifiedDate;

    @Version
    @JsonIgnore
    private Integer version;
}
