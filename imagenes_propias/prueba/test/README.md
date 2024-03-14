Instrucciones para pruebas automáticas
======================================

1. Pre-requisitos:

   - Para Firefox:
       - Crear un perfil *selenium* (o copiar el que figura en la carpeta *src/test/resources/selenium*) en la carpeta de los perfiles.
       - A partir de Firefox 47 es necesario un driver externo de firefox. Es el fichero *src/test/resources/geckodriver.exe*. En caso de que de algún problema de incompatibilidad de versiones se puede bajar del siguiente enlace: [geckodriver](https://github.com/mozilla/geckodriver/releases/). Referenciar el ejecutable desde la variable *webdriver.gecko.driver*.
   - Para Chrome:
       - Es necesario descargar un chromedriver para la versión de Chrome usada. Se puede descargar desde el siguiente enlace: [chromedriver](https://chromedriver.chromium.org/downloads). Referenciar el ejecutable desde la variable *webdriver.chrome.driver*.
   - Para EDGE:
       - Es necesario descargar un edgedriver para la versión de EDGE usada. Se puede descargar desde el siguiente enlace: [edgedriver](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/). Referenciar el ejecutable desde la variable *webdriver.edge.driver*. 
   
2. Lanzar Prueba:

    - Para Firefox:
        - Ejecutar:
           ```bash
           $ mvn clean verify -Dbrowser=firefox -Dremote.driver=false -Dwebdriver.gecko.driver=C:\\desarrollo\\java\\proyectos\\mntpa_j2ee_gescap_back\\test\\src\\test\\resources\\geckodriver.exe
           ```

           Estos son los parámetros que se aceptan con el fin de personalizar la ejecución:
        
           | Parametro                | Valor por defecto                                     | Comentario                                          |
           | ------------------------ | ----------------------------------------------------- | ----------------------------------------------------|
           | firefox.profile.name     | selenium                                              | Nombre del perfil del firefox.                      |
           | firefox.bin.path         | C:\\Program Files\\Mozilla Firefox\\firefox.exe       | Ruta al binario del firefox.                        |
           | test.local.resources.dir | C:\\selenium\\docs\\                                  | Path para los documentos a adjuntar en el trámite.|
           | webdriver.gecko.driver   |                                                       | Path al driver .exe de Firefox.                    |
        
        - *Probado con un Firefox 86.0.1 (64 bits)*

    - Para Chrome:
        - Ejecutar:
            ```bash
           $ mvn clean verify -Dbrowser=chrome -Dremote.driver=false -Dwebdriver.chrome.driver=C:\\desarrollo\\java\\proyectos\\mntpa_edu_gepep_back\\test\\src\\test\\resources\\chromedriver.exe
           ```
           Estos son los parámetros que se aceptan con el fin de personalizar la ejecución:
        
           | Parametro                | Valor por defecto                                     | Comentario                                         |
           | ------------------------ | ----------------------------------------------------- | -------------------------------------------------- |
           | test.local.resources.dir | C:\\selenium\\docs\\                                  | Parth para los documentos a adjuntar en el trámite.|
           | webdriver.chrome.driver  |                                                       | Parth al driver .exe de Chrome.                    |
        
        *Probado con un Chrome 89.0.1 (64 bits)*
   
    - Para Edge:
         - Ejecutar:
             ```bash
            $ mvn clean verify -Dbrowser=edge -Dremote.driver=false -Dwebdriver.edge.driver=C:\\desarrollo\\java\\proyectos\\mntpa_edu_gepep_back\\test\\src\\test\\resources\\msedgedriver.exe
            ```
           Estos son los parámetros que se aceptan con el fin de personalizar la ejecución:

           | Parametro                | Valor por defecto                                     | Comentario                                         |
           | ------------------------ | ----------------------------------------------------- | -------------------------------------------------- |
           | test.local.resources.dir | C:\\selenium\\docs\\                                  | Parth para los documentos a adjuntar en el trámite.|
           | webdriver.chrome.driver  |                                                       | Parth al driver .exe de Edge.                      |

       *Probado con un Edge 94.0.992.31 (64 bits)*