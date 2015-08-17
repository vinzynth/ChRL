/*
 * This file is part of aion-lightning <aion-lightning.com>.
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
package at.chrl.nutils;

import java.util.regex.Pattern;

import static at.chrl.nutils.Constants.log;

/**
 * A collection of utility methods to retrieve and parse the values of the Java
 * system properties.
 */
public final class SystemPropertyUtil {

	/**
	 * Returns {@code true} if and only if the system property with the
	 * specified {@code key} exists.
	 */
	public static boolean contains(String key) {
		return get(key) != null;
	}

	/**
	 * Returns the value of the Java system property with the specified
	 * {@code key}, while falling back to {@code null} if the property access
	 * fails.
	 *
	 * @return the property value or {@code null}
	 */
	public static String get(String key) {
		return get(key, null);
	}

	/**
	 * Returns the value of the Java system property with the specified
	 * {@code key}, while falling back to the specified default value if the
	 * property access fails.
	 *
	 * @return the property value. {@code def} if there's no such property or if
	 *         an access to the specified property is not allowed.
	 */
	public static String get(String key, String def) {
		if (key == null) {
			throw new NullPointerException("key");
		}
		if (key.isEmpty()) {
			throw new IllegalArgumentException("key must not be empty.");
		}

		String value = null;
		try {
			value = System.getProperty(key);
		} catch (Exception e) {
			log.error("Unable to retrieve a system property '" + key + "'; default values will be used.", e);
		}

		if (value == null) {
			return def;
		}

		return value;
	}

	/**
	 * Returns the value of the Java system property with the specified
	 * {@code key}, while falling back to the specified default value if the
	 * property access fails.
	 *
	 * @return the property value. {@code def} if there's no such property or if
	 *         an access to the specified property is not allowed.
	 */
	public static boolean getBoolean(String key, boolean def) {
		String value = get(key);
		if (value == null) {
			return def;
		}

		value = value.trim().toLowerCase();
		if (value.isEmpty()) {
			return true;
		}

		if ("true".equals(value) || "yes".equals(value) || "1".equals(value)) {
			return true;
		}

		if ("false".equals(value) || "no".equals(value) || "0".equals(value)) {
			return false;
		}

		log("Unable to parse the boolean system property '" + key + "':" + value + " - " + "using the default value: " + def);

		return def;
	}

	private static final Pattern INTEGER_PATTERN = Pattern.compile("-?[0-9]+");

	/**
	 * Returns the value of the Java system property with the specified
	 * {@code key}, while falling back to the specified default value if the
	 * property access fails.
	 *
	 * @return the property value. {@code def} if there's no such property or if
	 *         an access to the specified property is not allowed.
	 */
	public static int getInt(String key, int def) {
		String value = get(key);
		if (value == null) {
			return def;
		}

		value = value.trim().toLowerCase();
		if (INTEGER_PATTERN.matcher(value).matches()) {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
				// Ignore
			}
		}

		log("Unable to parse the integer system property '" + key + "':" + value + " - " + "using the default value: " + def);

		return def;
	}

	/**
	 * Returns the value of the Java system property with the specified
	 * {@code key}, while falling back to the specified default value if the
	 * property access fails.
	 *
	 * @return the property value. {@code def} if there's no such property or if
	 *         an access to the specified property is not allowed.
	 */
	public static long getLong(String key, long def) {
		String value = get(key);
		if (value == null) {
			return def;
		}

		value = value.trim().toLowerCase();
		if (INTEGER_PATTERN.matcher(value).matches()) {
			try {
				return Long.parseLong(value);
			} catch (Exception e) {
				// Ignore
			}
		}

		log("Unable to parse the long integer system property '" + key + "':" + value + " - " + "using the default value: " + def);

		return def;
	}

	private static void log(String msg) {
		log.warn(msg);
	}

	private SystemPropertyUtil() {
		// Unused
	}
}
