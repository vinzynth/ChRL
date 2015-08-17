/**
 * @author Christian Richard Leopold - ChRL
 * Jul 27, 2015 - 3:45:11 PM
 * chrl-orm
 * at.chrl.orm.hibernate.flyway
 */
package at.chrl.orm.hibernate.flyway;

import java.io.PrintStream;
import java.sql.Connection;

import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.callback.FlywayCallback;

import at.chrl.nutils.ArrayUtils;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Jul 27, 2015 - 3:45:11 PM
 *
 */
public class HibernateCallback implements FlywayCallback {

	private PrintStream out;
	@SuppressWarnings("unused")
	private PrintStream err;

	/**
	 * @param out
	 * @param err 
	 */
	public HibernateCallback(PrintStream out, PrintStream err) {
		this.out = out;
		this.err = err;
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#beforeClean(java.sql.Connection)
	 */
	@Override
	public void beforeClean(Connection connection) {
		out.println("[Hibernate Service] Flyway start clean");
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#afterClean(java.sql.Connection)
	 */
	@Override
	public void afterClean(Connection connection) {
		out.println("[Hibernate Service] Flyway finished clean");
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#beforeMigrate(java.sql.Connection)
	 */
	@Override
	public void beforeMigrate(Connection connection) {
		out.println("[Hibernate Service] Flyway start migrate");
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#afterMigrate(java.sql.Connection)
	 */
	@Override
	public void afterMigrate(Connection connection) {
		out.println("[Hibernate Service] Flyway finished migrate");
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#beforeEachMigrate(java.sql.Connection, org.flywaydb.core.api.MigrationInfo)
	 */
	@Override
	public void beforeEachMigrate(Connection connection, MigrationInfo info) {
		out.println("[Hibernate Service] Flyway migrate: " + info.getVersion().toString());
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#afterEachMigrate(java.sql.Connection, org.flywaydb.core.api.MigrationInfo)
	 */
	@Override
	public void afterEachMigrate(Connection connection, MigrationInfo info) {
		out.println("[Hibernate Service] Flyway finished migrate: ");
		printFlywayInfo(info);
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#beforeValidate(java.sql.Connection)
	 */
	@Override
	public void beforeValidate(Connection connection) {
		out.println("[Hibernate Service] Flyway start validate");
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#afterValidate(java.sql.Connection)
	 */
	@Override
	public void afterValidate(Connection connection) {
		out.println("[Hibernate Service] Flyway finished validate");
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#beforeBaseline(java.sql.Connection)
	 */
	@Override
	public void beforeBaseline(Connection connection) {
//		out.println("[Hibernate Service] Flyway start baseline");
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#afterBaseline(java.sql.Connection)
	 */
	@Override
	public void afterBaseline(Connection connection) {
		out.println("[Hibernate Service] Flyway finished initial migration version");
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#beforeInit(java.sql.Connection)
	 */
	@Override
	public void beforeInit(Connection connection) {}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#afterInit(java.sql.Connection)
	 */
	@Override
	public void afterInit(Connection connection) {}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#beforeRepair(java.sql.Connection)
	 */
	@Override
	public void beforeRepair(Connection connection) {
		out.println("[Hibernate Service] Flyway start repair");
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#afterRepair(java.sql.Connection)
	 */
	@Override
	public void afterRepair(Connection connection) {
		out.println("[Hibernate Service] Flyway finished repair");
	}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#beforeInfo(java.sql.Connection)
	 */
	@Override
	public void beforeInfo(Connection connection) {}

	/**
	 * {@inheritDoc}
	 * @see org.flywaydb.core.api.callback.FlywayCallback#afterInfo(java.sql.Connection)
	 */
	@Override
	public void afterInfo(Connection connection) {}
	
	private void printFlywayInfo(MigrationInfo... infos){
		MigrationInfo[] all = ArrayUtils.nullToEmpty(infos);
		if(all.length > 0){
			for (MigrationInfo migrationInfo : all) {
				out.println("[Hibernate Service] Flyway Information:");
				out.println("Version:     " + migrationInfo.getVersion().toString());
				out.println("Description: " + migrationInfo.getDescription());
				out.println("Date:        " + migrationInfo.getInstalledOn());
				out.println("State:       " + migrationInfo.getState().name());
				out.println("Type:        " + migrationInfo.getType().name());
				out.println("[Hibernate Service] -------------------");
			}
		}
	}
}
