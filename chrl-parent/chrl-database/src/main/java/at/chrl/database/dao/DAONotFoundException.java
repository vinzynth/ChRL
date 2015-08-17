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
package at.chrl.database.dao;

/**
 * This class represents exception that is thrown if DAO implementation was not
 * foud
 * 
 * @author SoulKeeper
 */
public class DAONotFoundException extends DAOException {

	/**
	 * SerialID
	 */
	private static final long serialVersionUID = 4241980426435305296L;

	public DAONotFoundException() {
	}

	/**
	 * @param message
	 */
	public DAONotFoundException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DAONotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public DAONotFoundException(Throwable cause) {
		super(cause);
	}
}
