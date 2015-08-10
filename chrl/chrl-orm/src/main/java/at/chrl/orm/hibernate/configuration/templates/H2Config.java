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
package at.chrl.orm.hibernate.configuration.templates;

import at.chrl.orm.hibernate.configuration.JPAConfig;

/**
 * @author Vinzynth
 * 09.08.2015 - 19:37:04
 *
 */
public class H2Config extends JPAConfig {
	
	@Override
	public void overrideConfig(){
		super.overrideConfig();
		this.JDBC_URL = "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1;MVCC=TRUE";
		this.JDBC_DRIVER = "org.h2.Driver";
		this.JDBC_USER = "sa";
		this.JDBC_PASSWORD = "";
		this.HBM2DDL_AUTO = "create";
	}
}
