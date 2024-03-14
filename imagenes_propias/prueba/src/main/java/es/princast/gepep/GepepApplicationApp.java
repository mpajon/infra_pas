package es.princast.gepep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import es.princast.mntpa.back.config.env.DefaultProfileUtil;

 
 

@SpringBootApplication
@ComponentScan("es.princast")
@EnableScheduling
public class GepepApplicationApp extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GepepApplicationApp.class);
    }

   

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GepepApplicationApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        app.run(args);
    }
}


