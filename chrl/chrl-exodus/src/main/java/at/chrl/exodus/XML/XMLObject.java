/**
 * This file is part of InPanic-Core.
 *
 * InPanic-Core is a free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * InPanic-Core is distibuted in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * InPanic-Core. If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.exodus.XML;

import java.util.ArrayList;

/**
 * @author Vinzynth 29.08.2013 - 01:26:35
 *
 */
public abstract class XMLObject {

	protected String name;
	protected String[] path;

	public XMLObject(String name, String[] path) {
		this.name = name;
		this.path = path;
	}

	public String[] getPath() {
		return this.path;
	}

	public String getPath(int depth) {
		String returnMe = path[0];
		for (int i = 1; i <= depth; i++)
			returnMe = returnMe.concat(".").concat(path[i]);
		return returnMe;
	}

	public String getFullPath() {
		String returnMe = path[0];
		for (int i = 1; i < path.length - 1; i++)
			returnMe = returnMe.concat(".").concat(path[i]);
		return returnMe;
	}

	public abstract void addXMLObject(XMLObject o);

	public String getName() {
		return name;
	}

	public ArrayList<XMLObject> getXMLObjects() {
		ArrayList<XMLObject> returnMe = new ArrayList<XMLObject>();

		returnMe.addAll(getAttributes());
		returnMe.addAll(getElements());

		return returnMe;
	}

	public ArrayList<XMLAttribute> getAttributes() {
		return new ArrayList<>();
	}

	public ArrayList<XMLElement> getElements() {
		return new ArrayList<>();
	}

	public String getContainerString() {
		return "";
	}

	public abstract String print(int tab);

	/**
	 * @return
	 */
	public String getParent() {
		return "";
	}
}
