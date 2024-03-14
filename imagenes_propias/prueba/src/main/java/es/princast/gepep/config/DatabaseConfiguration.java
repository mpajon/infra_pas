package es.princast.gepep.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("es.princast.gepep.repository")
@EntityScan("es.princast.gepep.domain")
@EnableTransactionManagement
public class DatabaseConfiguration {


}
