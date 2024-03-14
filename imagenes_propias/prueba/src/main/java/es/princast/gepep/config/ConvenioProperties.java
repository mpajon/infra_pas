package es.princast.gepep.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Configuration
@ConfigurationProperties(prefix = "application.conveniob")
@Validated
@Data
@Getter
@Setter
public class ConvenioProperties {

     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
     private LocalDateTime fechaconveniob;

}
