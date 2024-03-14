package es.princast.gepep.domain;

 import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

 import lombok.*;
 import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Familia.
 */
@Entity
@Table(name = "familia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Familia extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

 
    @Column(name = "cn_familia")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secFamilia")
    @SequenceGenerator(name = "secFamilia", sequenceName ="sec_familia", allocationSize=1)
    private Long idFamilia;
    
     @NotNull
    @Column(name = "dc_nombre", nullable = false)
    private String nombre;
     
     @Override
     public boolean equals(Object o) {
         if (this == o) {
             return true;
         }
         if (o == null || getClass() != o.getClass()) {
             return false;
         }
         Familia familia  = (Familia) o;
         if (familia.getIdFamilia() == null || getIdFamilia() == null) {
             return false;
         }
         return Objects.equals(getIdFamilia(), familia.getIdFamilia());
     }

     @Override
     public int hashCode() {
         return Objects.hashCode(getIdFamilia());
     }
  
 

}
