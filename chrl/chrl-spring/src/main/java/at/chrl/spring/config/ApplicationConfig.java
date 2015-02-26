/**
 * @author bravestone
 * Feb 20, 2015 - 11:38:57 AM
 * bravestone-spring
 * com.bravestone.spring.config
 */
package at.chrl.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import at.chrl.Application;

/**
 * @author bravestone
 *
 */
@Configuration
@ComponentScan(basePackageClasses = Application.class)
public class ApplicationConfig {

}
