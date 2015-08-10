/**
 * This file is part of ChRL Util Collection.
 * 
 * ChRL Util Collection is free software: you can redistribute it and/or
 * modify  it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * ChRL Util Collection is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ChRL Util Collection.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.orm.test.configs;

import java.util.List;

import at.chrl.orm.hibernate.configuration.IHibernateConfig;
import at.chrl.orm.hibernate.configuration.templates.MSSQLConfig;


/**
 * @author Vinzynth
 * 09.08.2015 - 18:08:54
 *
 */
public class MSSQLTestConfig extends MSSQLConfig {
	@Override
	public void overrideConfig(){
		super.overrideConfig();
		this.JDBC_URL = "jdbc:sqlserver://localhost:1433";
		this.JDBC_USER = "sa";
		this.JDBC_PASSWORD = "toor";
		this.HBM2DDL_AUTO = "create";
		this.PERSISTENCE_UNIT_NAME = "mssql";
	}
	

	private List<Class<?>> annotatedClasses;
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.orm.hibernate.configuration.IHibernateConfig#getAnnotatedClasses()
	 */
	@Override
	public List<Class<?>> getAnnotatedClasses() {
		return annotatedClasses;
	}

	/**
	 * @param annotatedClasses the annotatedClasses to set
	 */
	public IHibernateConfig setAnnotatedClasses(List<Class<?>> annotatedClasses) {
		this.annotatedClasses = annotatedClasses;
		return this;
	}
}