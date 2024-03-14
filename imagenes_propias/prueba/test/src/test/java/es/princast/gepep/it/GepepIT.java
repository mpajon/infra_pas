package es.princast.gepep.it;

import es.princast.fwpa.selenium.SeleniumBase;
import es.princast.fwpa.selenium.core.SeleniumTest;
import es.princast.gepep.it.pages.Login;
import es.princast.gepep.it.pages.Maestro;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SeleniumTest(startPage = "https://integ62.asturias.es/gepep")
public class GepepIT extends SeleniumBase {
    protected Log log = LogFactory.getLog(GepepIT.class);

    private final String USER = "E52617953W";
    private final String PWD = "temporal1";
    private final int WAIT = 3000;

    // Login
    private Login login;

    // Maestro de los menus
    private Maestro maestro;

    /**
     * Login para cada test
     */
    @Before
    public void login()  {
        this.login = new Login(driver);

        // esperar a que redireccione
        login.esperarPorKL();

        // user, pass
        login.setUser(this.USER);
        login.setPassword(this.PWD);

        // login
        this.maestro = login.clickBotonLogin();

        log.info("Login OK");
    }

    /**
     * Logount tras cada test
     */
    @After
    public void logout() {
        this.login.clickLinkLogout();
        log.info("Logout OK");

        this.login = null;
        this.maestro = null;
    }

    private void dormir() {
        try {
            Thread.sleep(this.WAIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMaestrosPracticas() {
        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuPracticas());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getTipoPractica());
        log.info("Maestros de tipos de practica OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuPracticas());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getPeriodoPractica());
        log.info("Maestros de periodos practicas OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuPracticas());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getHorasPeriodo());
        log.info("Maestros de horas periodo OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuPracticas());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getPrecios());
        log.info("Maestros de precios OK");
        // Esperar a cargar la tabla
        this.dormir();
    }

    @Test
    public void testMaestrosOfertaFormativa() {
        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuOfertas());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getCentros());
        log.info("Maestros de centros OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuOfertas());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getEnsenanzas());
        log.info("Maestros de enseñanzas OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuOfertas());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getCiclo());
        log.info("Maestros de ciclos OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuOfertas());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getCursoacademico());
        log.info("Maestros de cursos OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuOfertas());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getVisor());
        log.info("Maestros de visor OK");
        // Esperar a cargar la tabla
        this.dormir();
    }

    @Test
    public void testMaestrosEmpresas() {
        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuEmpresas());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getSector());
        log.info("Maestros de sectores OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuEmpresas());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getActividad());
        log.info("Maestros de actividades OK");
        // Esperar a cargar la tabla
        this.dormir();
    }

    @Test
    public void testMaestrosParticipantes() {
        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuParticipante());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getAlumnado());
        log.info("Maestros de alumnos OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuParticipante());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getTutores());
        log.info("Maestros de tutores OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuParticipante());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getEmpresa());
        log.info("Maestros de empresas OK");
        // Esperar a cargar la tabla
        this.dormir();
    }

    @Test
    public void testMaestrosProcesarPracticas() {
        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuProcesar());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getConvenios());
        log.info("Maestros de convenios OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuProcesar());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getDistribuir());
        log.info("Maestros de distribucion OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuProcesar());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getMatricula());
        log.info("Maestros de matriculas OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuProcesar());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getGastos());
        log.info("Maestros de gastos OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuProcesar());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getBloqueo());
        log.info("Maestros de bloqueos OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuProcesar());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getSeguimiento());
        log.info("Maestros de seguimientos OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuProcesar());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getVisitas());
        log.info("Maestros de visitas OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuProcesar());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getOperaciones());
        log.info("Maestros de operaciones OK");
        // Esperar a cargar la tabla
        this.dormir();
    }

    @Test
    public void testMaestrosDocumentacion() {
        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuDocumentacion());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getDocumentos());
        log.info("Maestros de documentacion OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuDocumentacion());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getTextos());
        log.info("Maestros de textos OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuDocumentacion());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getClausulas());
        log.info("Maestros de clausulas OK");
        // Esperar a cargar la tabla
        this.dormir();

        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuDocumentacion());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getDocumentacion());
        log.info("Maestros de documentacion OK");
        // Esperar a cargar la tabla
        this.dormir();

    }

    @Test
    public void testMaestrosInformacion() {
        // Click en el menu padre
        this.maestro.clickLink(this.maestro.getMenuInformacion());
        // Click en el submenu
        this.maestro.clickLink(this.maestro.getInformacion());
        log.info("Maestros de informacion OK");
        // Esperar a cargar la tabla
        this.dormir();
    }
}
