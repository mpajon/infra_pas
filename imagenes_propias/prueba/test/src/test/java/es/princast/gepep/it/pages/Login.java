package es.princast.gepep.it.pages;

import es.princast.fwpa.selenium.pages.PageObject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Login extends PageObject {

    // LOGIN
    @FindBy(how = How.ID, using = "username")
    WebElement user;

    @FindBy(how = How.ID, using = "password")
    WebElement password;

    @FindBy(how = How.ID, using = "kc-login")
    WebElement botonLogin;

    //LOGOUT
    @FindBy(how = How.ID, using = "menu_account")
    WebElement menuAccount;

    @FindBy(how = How.ID, using = "logout")
    WebElement logout;

    /**
     * Constructor
     */
    public Login(WebDriver driver) {
        super(driver);
    }

    public void setUser(String valor) {
        this.user.sendKeys(valor);
    }

    public void setPassword(String valor) {
        this.password.sendKeys(valor);
    }

    /**
     * Redireccion al KL, esperando a que muestre el formulario
     */
    public void esperarPorKL() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 30, 2000);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        } catch (TimeoutException te) {
            log.error("No se carga la pantalla de login");
        }
    }

    /**
     * Loguear y redireccionar el control a GEPEP
     */
    public Maestro clickBotonLogin(){
        this.botonLogin.click();

        return PageFactory.initElements(driver, Maestro.class);
    }

    /**
     * Logout
     */
    public void clickLinkLogout() {
        this.menuAccount.click();
        this.logout.click();
    }
}
