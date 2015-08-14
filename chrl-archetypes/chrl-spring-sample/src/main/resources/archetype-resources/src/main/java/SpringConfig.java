package ${package};

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.chrl.orm.hibernate.configuration.JPAConfig;

@Configuration
public class SpringConfig {

	@Bean
	public JPAConfig getJPAConfig(){
		return new ORMConfig();
	}
}
