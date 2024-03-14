package es.princast.gepep.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @CreatedBy    
    @Column(name = "ca_usucrea", columnDefinition="VARCHAR(50) DEFAULT 'SYSTEM'", nullable = false,  updatable = false ) 
    @JsonIgnore
    private String createdBy = "SYSTEM";

    @CreatedDate
    @Column(name = "fe_fcreacion", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP" ,nullable = false, updatable = false)
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @LastModifiedBy
    @Column(name = "ca_usuactual", length = 50)
    @JsonIgnore
    private String lastModifiedBy ="SYSTEM UP";

    @LastModifiedDate
    @Column(name = "fe_factualiza")
    @JsonIgnore
    private Instant lastModifiedDate = Instant.now();
    
    
   
    
}
