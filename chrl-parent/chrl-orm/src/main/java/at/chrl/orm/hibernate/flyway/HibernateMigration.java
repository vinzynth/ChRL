/**
 * @author Christian Richard Leopold - ChRL
 * Sep 14, 2015 - 2:27:15 PM
 * chrl-orm
 * at.chrl.orm.hibernate.flyway
 */
package at.chrl.orm.hibernate.flyway;

import java.sql.Connection;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import at.chrl.orm.hibernate.HibernateService;
import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.orm.hibernate.configuration.IHibernateConfig;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Sep 14, 2015 - 2:27:15 PM
 *
 */
public interface HibernateMigration extends JdbcMigration{

	public void migrate(SessionTemplate session) throws Exception;
	
	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.migration.jdbc.JdbcMigration#migrate(java.sql.Connection)
	 */
	@Override
	default void migrate(Connection connection) throws Exception {
		try (SessionTemplate session = new SessionTemplate() {
			
			@Override
			protected IHibernateConfig getHibernateConfig() {
				return HibernateService.getInstance().getCurrentFlywayConfig();
			}
			
		}){
			this.migrate(session);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error Migration database: " + this.getClass().getName(), e);
		}
	}
	
}
