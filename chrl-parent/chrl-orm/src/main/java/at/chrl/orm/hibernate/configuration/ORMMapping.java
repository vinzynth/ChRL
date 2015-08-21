/**
 * @author Christian Richard Leopold - ChRL
 * Aug 21, 2015 - 2:41:01 PM
 * chrl-orm
 * at.chrl.orm.hibernate.configuration
 */
package at.chrl.orm.hibernate.configuration;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 21, 2015 - 2:41:01 PM
 *
 */
public class ORMMapping implements IHibernateConfig {

	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ORMMapping: " + this.getClass().getSimpleName();
	}
}
