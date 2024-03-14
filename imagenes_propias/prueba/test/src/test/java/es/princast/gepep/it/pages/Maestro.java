package es.princast.gepep.it.pages;

import es.princast.fwpa.selenium.pages.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;


public class Maestro extends PageObject {

    //  MENU
    @FindBy(how = How.ID, using = "entity-menu-practicas")
    WebElement menuPracticas;

    @FindBy(how = How.ID, using = "entity-menu-ofertas")
    WebElement menuOfertas;

    @FindBy(how = How.ID, using = "entity-menu-empresas")
    WebElement menuEmpresas;

    @FindBy(how = How.ID, using = "entity-menu-participante")
    WebElement menuParticipante;

    @FindBy(how = How.ID, using = "entity-menu-procesar")
    WebElement menuProcesar;

    @FindBy(how = How.ID, using = "entity-menu-documentacion")
    WebElement menuDocumentacion;

    @FindBy(how = How.ID, using = "entity-menu-informacion")
    WebElement menuInformacion;

    @FindBy(how = How.ID, using = "entity-menu-administracion")
    WebElement menuAdministracion;

    // SUBMENUS
    @FindBy(how = How.ID, using = "menu_tipoPractica")
    WebElement tipoPractica;

    @FindBy(how = How.ID, using = "menu_periodoPractica")
    WebElement periodoPractica;

    @FindBy(how = How.ID, using = "menu_horasPeriodo")
    WebElement horasPeriodo;

    @FindBy(how = How.ID, using = "menu_precios")
    WebElement precios;

    @FindBy(how = How.ID, using = "menu_centros")
    WebElement centros;

    @FindBy(how = How.ID, using = "menu_ensenanzas")
    WebElement ensenanzas;

    @FindBy(how = How.ID, using = "menu_ciclo")
    WebElement ciclo;

    @FindBy(how = How.ID, using = "menu_cursoAcademico")
    WebElement cursoacademico;

    @FindBy(how = How.ID, using = "menu_visor")
    WebElement visor;

    @FindBy(how = How.ID, using = "menu_sector")
    WebElement sector;

    @FindBy(how = How.ID, using = "menu_actividad")
    WebElement actividad;

    @FindBy(how = How.ID, using = "menu_alumnado")
    WebElement alumnado;

    @FindBy(how = How.ID, using = "menu_tutores")
    WebElement tutores;

    @FindBy(how = How.ID, using = "menu_empresa")
    WebElement empresa;

    @FindBy(how = How.ID, using = "menu_convenios")
    WebElement convenios;

    @FindBy(how = How.ID, using = "menu_distribuir")
    WebElement distribuir;

    @FindBy(how = How.ID, using = "menu_matricula")
    WebElement matricula;

    @FindBy(how = How.ID, using = "menu_gastos")
    WebElement gastos;

    @FindBy(how = How.ID, using = "menu_bloqueo")
    WebElement bloqueo;

    @FindBy(how = How.ID, using = "menu_seguimiento")
    WebElement seguimiento;

    @FindBy(how = How.ID, using = "menu_visitas")
    WebElement visitas;

    @FindBy(how = How.ID, using = "menu_operaciones")
    WebElement operaciones;

    @FindBy(how = How.ID, using = "menu_documentos")
    WebElement documentos;

    @FindBy(how = How.ID, using = "menu_textos")
    WebElement textos;

    @FindBy(how = How.ID, using = "menu_clausulas")
    WebElement clausulas;

    @FindBy(how = How.ID, using = "menu_documentacion")
    WebElement documentacion;

    @FindBy(how = How.ID, using = "menu_informacion")
    WebElement informacion;

    /**
     * Constructor
     */
    public Maestro(WebDriver driver) {
        super(driver);
    }

    // MENUS
    public WebElement getMenuPracticas() {
        return this.menuPracticas;
    }

    public WebElement getMenuOfertas() {
        return this.menuOfertas;
    }

    public WebElement getMenuEmpresas() {
        return this.menuEmpresas;
    }

    public WebElement getMenuParticipante() {
        return this.menuParticipante;
    }

    public WebElement getMenuProcesar() {
        return this.menuProcesar;
    }

    public WebElement getMenuDocumentacion() {
        return this.menuDocumentacion;
    }

    public WebElement getMenuInformacion() {
        return this.menuInformacion;
    }

    // SUBMENUS
    public WebElement getTipoPractica() {
        return this.tipoPractica;
    }

    public WebElement getPeriodoPractica() {
        return this.periodoPractica;
    }

    public WebElement getHorasPeriodo() {
        return this.horasPeriodo;
    }

    public WebElement getPrecios() {
        return this.precios;
    }

    public WebElement getCentros() {
        return this.centros;
    }

    public WebElement getEnsenanzas() {
        return this.ensenanzas;
    }

    public WebElement getCiclo() {
        return this.ciclo;
    }

    public WebElement getCursoacademico() {
        return this.cursoacademico;
    }

    public WebElement getVisor() {
        return this.visor;
    }

    public WebElement getSector() {
        return this.sector;
    }

    public WebElement getActividad() {
        return this.actividad;
    }

    public WebElement getAlumnado() {
        return this.alumnado;
    }

    public WebElement getTutores() {
        return this.tutores;
    }

    public WebElement getEmpresa() {
        return this.empresa;
    }

    public WebElement getConvenios() {
        return this.convenios;
    }

    public WebElement getDistribuir() {
        return this.distribuir;
    }

    public WebElement getMatricula() {
        return this.matricula;
    }

    public WebElement getGastos() {
        return this.gastos;
    }

    public WebElement getBloqueo() {
        return this.bloqueo;
    }

    public WebElement getSeguimiento() {
        return this.seguimiento;
    }

    public WebElement getVisitas() {
        return this.visitas;
    }

    public WebElement getOperaciones() {
        return this.operaciones;
    }

    public WebElement getDocumentos() {
        return this.documentos;
    }

    public WebElement getTextos() {
        return this.textos;
    }

    public WebElement getClausulas() {
        return this.clausulas;
    }

    public WebElement getDocumentacion() {
        return this.documentacion;
    }

    public WebElement getInformacion() {
        return this.informacion;
    }

    public void clickLink(WebElement e) {
        e.click();
    }
}
