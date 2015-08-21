/**
 * @author bravestone Feb 20, 2015 - 11:38:57 AM bravestone-spring
 *         com.bravestone.spring.config
 */
package at.chrl.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import at.chrl.Application;
import at.chrl.nutils.cron.CronService;

/**
 * @author bravestone
 *
 */
@Configuration
@ComponentScan(basePackageClasses = Application.class)
public class ApplicationConfig {

	@Bean(destroyMethod = "shutdown")
	public CronService getCronService(){
		return CronService.getInstance();
	}
	
}
