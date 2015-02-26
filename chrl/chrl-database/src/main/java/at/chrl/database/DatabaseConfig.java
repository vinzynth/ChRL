/**
 * This file is part of aion-lightning <aion-lightning.org>.
 * 
 * aion-lightning is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * aion-lightning is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * aion-lightning. If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.database;

import java.io.File;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.Property;

/**
 * This class holds all configuration of database
 * 
 * @author SoulKeeper
 */
public class DatabaseConfig {

	static {
		ConfigUtil.loadAndExport(DatabaseConfig.class);
	}

	/**
	 * Default database url.
	 */
	@Property(key = "chrl.database.url",
			defaultValue = "jdbc:mysql://localhost:3306/aion_uni")
	public static String DATABASE_URL;

	/**
	 * Name of database Driver
	 */
	@Property(key = "chrl.database.driver",
			defaultValue = "com.mysql.jdbc.Driver")
	public static Class<?> DATABASE_DRIVER;

	/**
	 * Default database user
	 */
	@Property(key = "chrl.database.user", defaultValue = "root")
	public static String DATABASE_USER;

	/**
	 * Default database password
	 */
	@Property(key = "chrl.database.password", defaultValue = "root")
	public static String DATABASE_PASSWORD;

	/**
	 * Amount of partitions used by BoneCP
	 */
	@Property(key = "chrl.database.bonecp.partition.count", defaultValue = "2")
	public static int DATABASE_BONECP_PARTITION_COUNT;

	/**
	 * Minimum amount of connections that are always active in bonecp partition
	 */
	@Property(key = "chrl.database.bonecp.partition.connections.min",
			defaultValue = "2")
	public static int DATABASE_BONECP_PARTITION_CONNECTIONS_MIN;

	/**
	 * Maximum amount of connections that are allowed to use in bonecp partition
	 */
	@Property(key = "chrl.database.bonecp.partition.connections.max",
			defaultValue = "5")
	public static int DATABASE_BONECP_PARTITION_CONNECTIONS_MAX;

	/**
	 * Location of database script context descriptor
	 */
	@Property(key = "chrl.database.scriptcontext.descriptor",
			defaultValue = "./data/scripts/system/database/database.xml")
	public static File DATABASE_SCRIPTCONTEXT_DESCRIPTOR;

}
