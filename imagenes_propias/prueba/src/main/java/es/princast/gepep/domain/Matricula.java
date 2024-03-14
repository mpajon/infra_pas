package es.princast.gepep.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A PracticaOferta.
 */
@Entity
@Table(name = "matricula")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@Getter
@Setter
/** Mapping para queries Nativas: (usadas para evitar que jpa nos inicialice todas las relaciones y afecte al rendimiento) **/

@SqlResultSetMapping(name="MatriculaNativeQueryMapping",
classes = {
        @ConstructorResult(targetClass = Matricula.class,
                columns = {
                		// Columnas Datos de matricula //
						@ColumnResult(name = "idMatricula", type = String.class),
						@ColumnResult(name = "anio", type = Integer.class),
						@ColumnResult(name = "xMatricula", type = Integer.class),
						@ColumnResult(name = "desOferta", type = String.class),
						@ColumnResult(name = "fechaMatricula", type = LocalDate.class),
						@ColumnResult(name = "esDual", type = Boolean.class),
						@ColumnResult(name = "desCiclo", type = String.class),
						@ColumnResult(name = "codCiclo", type = String.class),
						@ColumnResult(name = "curso", type = Integer.class),
						@ColumnResult(name = "turno", type = String.class),
						@ColumnResult(name = "numAlumnos", type = Integer.class),
                		
                		// Columnas Datos de OfertaCentro //
                		@ColumnResult(name="idOfertaCentro", type=String.class),
                		
                		// Columnas Datos de Unidad //
                		@ColumnResult(name="idUnidad", type=String.class),
                		@ColumnResult(name="nombreUnidad", type=String.class),
                		
                		// Columnas Datos de Alumno //
                		@ColumnResult(name="idAlumno", type=String.class),
                		@ColumnResult(name="nifAlumno", type=String.class),
                		@ColumnResult(name="nombreAlumno", type=String.class),
                		@ColumnResult(name="apellido1", type=String.class),
                		@ColumnResult(name="apellido2", type=String.class),
                		
                		// Columnas Datos de seguimiento //
                		@ColumnResult(name="idSeguimientoFinal", type=Long.class),
                		
                		}
        )}
)

public class Matricula extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "ca_matricula",nullable =false) 
    @Id
  /*  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqMatricula")
    @SequenceGenerator(name = "seqMatricula", sequenceName="sec_matricula", allocationSize=1)*/
    private String idMatricula;   
    
    @ManyToOne
    @OrderBy("dl_apellido1, dl_apellido2, dc_nombre asc")
    @JoinColumn(name = "ca_alumno",referencedColumnName="ca_alumno", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
   // @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler","alumno", "matricula"}, allowSetters =true)
    private Alumno alumno;
    

    @ManyToOne
    @JoinColumn(name = "ca_ofercen", referencedColumnName="ca_ofercen",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler","ofertaCentro", "matricula"}, allowSetters =true)
    private OfertasCentro ofertaCentro;
 
    
    @Transient
    @Builder.Default
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "matricula"})  
    //@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler","gastosAlumno", "matricula"}, allowSetters =true)
    private List<GastoAlumno> gastosAlumno = new ArrayList<>();

   
    @Transient
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "matricula"})  
    private Distribucion distribucion;
    
    @Transient
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "matricula"})  
    private SeguimientoFinal seguimientoFinal;
    
    
    @Column(name = "nu_anio")
    private Integer anio;
    
    @Column(name = "cn_x_matricula")   
    private Integer xMatricula;   
    
    @Column(name = "dl_desoferta")
    private String desOferta;
    
    @Column(name = "fe_fmatricula")
    private LocalDate fechaMatricula;
    
    @Column(name = "fl_esdual")
    private Boolean esDual;
	
    @Column(name = "dl_desciclo")
    private String desCiclo;
    
    @Column(name = "dc_codciclo")
    private String codCiclo;
    
    @Column(name = "cn_curso")   
    private Integer curso;   
    
    @ManyToOne
    @JoinColumn(name = "ca_unidad",referencedColumnName="ca_unidad", nullable = false)    
    @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler","unidad", "matricula"}, allowSetters =true)
    private Unidad unidad;   
    
    @Column(name = "ca_turno")   
    private String turno;   
  
    @Column(name = "cn_alumnos")   
    private Integer numAlumnos;   
    
    
	
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Matricula matricula = (Matricula) o;
        if (matricula.getIdMatricula() == null || getIdMatricula() == null) {
            return false;
        }
        return Objects.equals(getIdMatricula(), matricula.getIdMatricula());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdMatricula());
    }

	

    /** 
     * Constructor para MatriculaNativeQueryMapping
     */
    public Matricula(
    		
    		// Parametros Datos de matricula //
    		String idMatricula,
    		Integer anio,
			Integer xMatricula,
			String desOferta,
			LocalDate fechaMatricula,
			Boolean esDual,
			String desCiclo,
			String codCiclo,
			Integer curso,
			String turno,
			Integer numAlumnos,
    		
    		// Parametros Datos de OfertaCentro //
			String idOfertaCentro,
			
			// Parametros Datos de Unidad //
			String idUnidad,
			String nombreUnidad,
    		
    		// Parametros Datos de alumno //
    		String idAlumno,
    		String nifAlumno,
    		String nombreAlumno,
    		String apellido1,
    		String apellido2,
    		
    		// Parametros Datos de seguimiento //
    		Long idSeguimientoFinal) {
       
    	 // Seteo Datos de Matricula //
        this.idMatricula = idMatricula;
 		this.anio = anio;
 		this.xMatricula = xMatricula;
 		this.desOferta = desOferta;
 		this.fechaMatricula = fechaMatricula;
 		this.esDual = esDual;
 		this.desCiclo = desCiclo;
 		this.codCiclo = codCiclo;
 		this.curso = curso;
 		this.turno = turno;
 		this.numAlumnos = numAlumnos;
    	
    	
    	// Seteo Datos de OfertaCentro //
    	OfertasCentro ofertaCentro = null;
    	if(idOfertaCentro != null) {
    		ofertaCentro = new OfertasCentro(idOfertaCentro);
    	}
    	this.ofertaCentro = ofertaCentro;
    	
    	// Seteo Datos de Unidad //
    	Unidad unidad = null;
    	if(idUnidad != null) {
    		unidad = new Unidad(idUnidad);
    		unidad.setNombre(nombreUnidad);
    	}
    	this.unidad = unidad;
    		
    	// Seteo Datos de alumno //
        Alumno alumno =  new Alumno();
        alumno.setIdAlumno(idAlumno);
        alumno.setNif(nifAlumno);
        alumno.setNombre(nombreAlumno);
        alumno.setApellido1(apellido1);
        alumno.setApellido2(apellido2);
        this.alumno = alumno;
        
        // Seteo Datos de seguimiento //
       SeguimientoFinal seguimientoFinal = null;
       if(idSeguimientoFinal != null) {
    	   seguimientoFinal = new SeguimientoFinal(idSeguimientoFinal);
       }
       this.seguimientoFinal = seguimientoFinal;
       
       

    }

	
	
  
}
