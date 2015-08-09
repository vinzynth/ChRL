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
package at.chrl.orm.hibernate.configuration;

import org.hibernate.ogm.cfg.OgmProperties;

import at.chrl.nutils.configuration.Property;

/**
 * @author Vinzynth
 * 09.08.2015 - 21:57:16
 *
 */
public class OGMConfig extends JPAConfig {

	/**
	 * {@inheritDoc}
	 * @see at.chrl.orm.hibernate.configuration.JPAConfig#toString()
	 */
	@Override
	public String toString() {
		return USERNAME+"@"+HOST+":"+PORT+"/"+DATABASE;
	}
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.orm.hibernate.configuration.IHibernateConfig#overrideConfig()
	 */
	@Override
	public void overrideConfig() {
		this.PROVIDER = "org.hibernate.ogm.jpa.HibernateOgmPersistence";
		this.TRANSACTION_STRATEGY = "org.hibernate.transaction.JTATransactionFactory";
		this.JTA_PLATFORM = "org.hibernate.service.jta.platform.internal.JBossStandAloneJtaPlatform";
	}

	@Property(key = OgmProperties.CREATE_DATABASE, defaultValue = "")
	public String CREATE_DATABASE;
	
	@Property(key = OgmProperties.DATABASE, defaultValue = "")
	public String DATABASE;
	
	@Property(key = OgmProperties.DATASTORE_PROVIDER, defaultValue = "", examples = {
			"org.hibernate.ogm.datastore.cassandra.impl.CassandraDatastoreProvider",
			"org.hibernate.ogm.datastore.couchdb.impl.CouchDBDatastoreProvider",
			"org.hibernate.ogm.datastore.ehcache.impl.EhcacheDatastoreProvider",
			"org.hibernate.ogm.datastore.infinispan.impl.InfinispanDatastoreProvider",
			"org.hibernate.ogm.datastore.map.impl.MapDatastoreProvider",
			"org.hibernate.ogm.datastore.mongodb.impl.MongoDBDatastoreProvider",
			"org.hibernate.ogm.datastore.mongodb.impl.FongoDBDatastoreProvider",
			"org.hibernate.ogm.datastore.neo4j.impl.Neo4jDatastoreProvider"
			})
	public String DATASTORE_PROVIDER;
	
	@Property(key = OgmProperties.ERROR_HANDLER, defaultValue = "")
	public String ERROR_HANDLER;
	
	@Property(key = OgmProperties.GRID_DIALECT, defaultValue = "")
	public String GRID_DIALECT;
	
	@Property(key = OgmProperties.HOST, defaultValue = "")
	public String HOST;
	
	@Property(key = OgmProperties.OPTION_CONFIGURATOR, defaultValue = "")
	public String OPTION_CONFIGURATOR;
	
	@Property(key = OgmProperties.PASSWORD, defaultValue = "")
	public String PASSWORD;
	
	@SuppressWarnings("deprecation")
	@Property(key = OgmProperties.PORT, defaultValue = "")
	public String PORT;
	
	@Property(key = OgmProperties.USERNAME, defaultValue = "")
	public String USERNAME;
}
