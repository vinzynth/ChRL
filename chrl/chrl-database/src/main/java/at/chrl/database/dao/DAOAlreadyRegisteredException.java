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
 * This exception is thrown if DAO is already registered in
 * {@link at.chrl.database.dao.DAOManager}
 * 
 * @author SoulKeeper
 */
public class DAOAlreadyRegisteredException extends DAOException {

	/**
	 * SerialID
	 */
	private static final long serialVersionUID = -4966845154050833016L;

	public DAOAlreadyRegisteredException() {
	}

	/**
	 * @param message
	 */
	public DAOAlreadyRegisteredException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DAOAlreadyRegisteredException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public DAOAlreadyRegisteredException(Throwable cause) {
		super(cause);
	}
}
