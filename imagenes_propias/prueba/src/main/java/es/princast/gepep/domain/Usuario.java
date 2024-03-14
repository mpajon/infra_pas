package es.princast.gepep.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
public class Usuario extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "cn_usuario")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUsuario")
    @SequenceGenerator(name = "seqUsuario", sequenceName="sec_usuario", allocationSize=1)
    private Long idUsuario;

    @NotNull
    @Column(name = "ca_login", nullable = false)
    private String login;
    
    @Column(name = "ca_pwd")
    private String pwd;

    @Column(name = "dc_nombre")
    private String nombre;

    @Column(name = "dl_apellidos")
    private String apellidos;

    @Column(name = "dl_direccion")
    private String direccion;

    @Column(name = "dl_email")
    private String email;

    @Column(name = "dc_telefono")
    private String telefono;

    @Column(name = "ti_tipo_fiscal")
    private String tipoFiscal;

    @Column(name = "if_dni")
    private String dni;

    @Column(name = "fl_activo")
    private Boolean activo;

    @Column(name = "fe_falta")
    private LocalDate falta;

    @Column(name = "fe_fbaja")
    private LocalDate fechaBaja;

    @Column(name = "ca_curso_aca")
    private String anioAcademico;

    @Column(name = "cn_usuario_base")
    private Integer idUsuarioBase;

    @OneToOne
    @JoinColumn(name="ca_centro", referencedColumnName="ca_centro",unique = false)    
    private Centro centro;
    
    @OneToOne
    @JoinColumn(name="cn_perfil", referencedColumnName="cn_perfil",unique = false)  
    private Perfil perfil;
 
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

       Usuario user = (Usuario) o;
        return !(user.getIdUsuario() == null || getIdUsuario() == null) && Objects.equals(getIdUsuario(), user.getIdUsuario());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdUsuario());
    }

  
}
