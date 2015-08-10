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

import org.hibernate.ogm.datastore.neo4j.Neo4jProperties;
import org.hibernate.ogm.datastore.neo4j.impl.Neo4jDatastoreProvider;

import at.chrl.nutils.configuration.Property;
import at.chrl.orm.hibernate.configuration.OGMConfig;


/**
 * 
 * Hibernate OGM supports neo4j only in embedded mode yet.
 * <p>
 * Since neo40j uses a older lucene version, using a neo4j datastore is not
 * compatible with hibernate search
 * <p>
 * 
 * @author Vinzynth
 * 09.08.2015 - 18:14:38
 *
 */
public class Neo4jConfig extends OGMConfig {

	@Override
	public void overrideConfig(){
		super.overrideConfig();
		this.DATASTORE_PROVIDER = Neo4jDatastoreProvider.class.getName();
		this.JTA_PLATFORM = "";
		this.HIBERNATE_SEARCH_DIRECTORY_PROVIDER = "";
		this.HIBERNATE_SEARCH_LOCKING_STRATEGY = "";
		this.HIBERNATE_SEARCH_FILESYSTEM_ACCESS_TYPE = "";
		this.HIBERNATE_SEARCH_INDEX_BASE = "";
		this.HIBERNATE_SEARCH_EXCLUSIVE_INDEX_USER = "";
		this.HIBERNATE_SEARCH_AUTOREGISTER_LISTENERS = "false";
	}
	
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.orm.hibernate.configuration.OGMConfig#toString()
	 */
	@Override
	public String toString() {
		return "Neo4j@" + DATABASE_PATH;
	}
	
	@Property(key = Neo4jProperties.CONFIGURATION_RESOURCE_NAME, defaultValue = "")
	public String CONFIGURATION_RESOURCE_NAME;
	
	@Property(key = Neo4jProperties.DATABASE_PATH, defaultValue = "")
	public String DATABASE_PATH;
	
	@Property(key = Neo4jProperties.SEQUENCE_QUERY_CACHE_MAX_SIZE, defaultValue = "")
	public String SEQUENCE_QUERY_CACHE_MAX_SIZE;
}
