package es.princast.gepep.domain.dto;


import java.time.LocalDate;

public interface DistribucionDTO {
    Long getIdDistribucion();
    Long getIdAnexo();
    String getCentro();
    Integer getHEntradaMan();
    Integer getMinEntradaMan();
    Integer getHSalidaMan();
    Integer getMinSalidaMan();
    Integer getHEntradaTard();
    Integer getMinEntradaTard();
    Integer getHSalidaTard();
    Integer getMinSalidaTard();
    String getHorarioFlexible();
    LocalDate getFechaBaja();
    LocalDate getFechaInicio();
    LocalDate getFechaFin();
    String getRealizacion();
    String getTutorEmpresa();

    String getCursoAcademico();
    String getNombrePeriodo();
    String getNombreTipoPractica();

    String getNifAlumno();
    String getNombreAlumno();
    String getApellido1Alumno();
    String getApellido2Alumno();
    LocalDate getFechaNacimientoAlumno();
    String getPaisAlumno();
    String getProvinciaAlumno();
    String getMunicipioAlumno();

    String getNombreUnidad();
    String getTurno();

    String getCodigoCentro();
    String getNombreCentro();

    String getNombreOferta();
    String getCodigoOferta();

    String getNombreCiclo();
    String getCodigoCiclo();
    String getFamilia();

    String getNombreEnsenanza();

    String getNombreTipoPracticaConvenio();
    Integer getNumeroConvenio();
    String getCodigoConvenio();

    String getNombreArea();
    String getCifEmpresa();
    String getNombreEmpresa();
    String getNombreActividad();

    Integer getCodAnexo();
    Integer getHorasPeriodoPractica();
    Integer getHoras();

    String getNifTutor();
    String getNombreTutor();
    String getApellido1Tutor();
    String getApellido2Tutor();

    String getNifTutorAdicional();
    String getNombreTutorAdicional();
    String getApellido1TutorAdicional();
    String getApellido2TutorAdicional();
}
