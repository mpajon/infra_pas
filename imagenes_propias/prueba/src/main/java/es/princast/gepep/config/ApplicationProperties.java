package es.princast.gepep.config;

import es.princast.gepep.domain.Convenio;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.annotation.Validated;


@Configuration
@ConfigurationProperties(prefix="application")
@Validated
@Data
public class ApplicationProperties {

	  	@Bean
	  	@ConfigurationProperties(prefix="spring.messages")
	    public MessageSource messageSource() {
	        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	        return messageSource;
	  }
	  
	    @Bean
	    @ConfigurationProperties(prefix="spring.config")
	    public MessageSource configSource() {
	        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	        return messageSource;
	    }

		@Bean
		@ConfigurationProperties(prefix="spring.reports")
		public MessageSource reportsSource() {
			ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();			
			return messageSource;
		}



}
