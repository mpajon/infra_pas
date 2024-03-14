package es.princast.gepep.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import es.princast.gepep.service.FileStorageService;
@Configuration
public class FileStorageConfiguration {
	


	
	@Autowired
	private MultipartProperties multipartProperties;


	@Bean
	public MultipartConfigElement multipartConfigElement() {
		return this.multipartProperties.createMultipartConfig();
	}
	
	
	@Bean
	public FileStorageService fileStorageService(MultipartProperties multipartProperties) {
		return new FileStorageService(multipartProperties.getLocation());
	}

	
	 @Bean
	 public MultipartResolver multipartResolver(MultipartProperties multipartProperties) {
		 CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	     return multipartResolver;
		 
	 }
	
	
}
