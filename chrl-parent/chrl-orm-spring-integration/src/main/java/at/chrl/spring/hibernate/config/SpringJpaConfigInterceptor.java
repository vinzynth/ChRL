/**
 * @author Christian Richard Leopold - ChRL
 * Aug 26, 2015 - 10:30:33 AM
 * chrl-orm-spring-integration
 * at.chrl.spring.hibernate.config
 */
package at.chrl.spring.hibernate.config;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 26, 2015 - 10:30:33 AM
 *
 */
public interface SpringJpaConfigInterceptor {

	public void modify(SpringGeneratedJpaConfig config);
	
}
