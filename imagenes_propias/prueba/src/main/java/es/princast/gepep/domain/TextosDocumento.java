package es.princast.gepep.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A TextosDocumento.
 */
@Entity
@Table(name = "textos_documento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class TextosDocumento extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_textos_documento")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTextos")
    @SequenceGenerator(name = "seqTextos", sequenceName="sec_textos_documento",allocationSize=1)
    private Long idTextosDocumento;

    @Lob
    @Type(type="text")
    @Column(name = "te_exponen", length=4000)
    private String exponen;
    
    @Lob
    @Type(type="text")
    @Column(name = "te_acuerdan", length=4000)
    private String acuerdan;
    
    //sera uno a uno, cada documento tiene dos textos configurables...
    @ManyToOne
    @JoinColumn(name = "cn_documento",referencedColumnName="cn_documento", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})     
    private Documento documento;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TextosDocumento textos = (TextosDocumento) o;
        if (textos.getIdTextosDocumento() == null || getIdTextosDocumento() == null) {
            return false;
        }
        return Objects.equals(getIdTextosDocumento(), textos.getIdTextosDocumento());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdTextosDocumento());
    }
}
