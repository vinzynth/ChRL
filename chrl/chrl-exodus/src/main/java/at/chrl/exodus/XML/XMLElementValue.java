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

/**
 * @author Vinzynth 29.08.2013 - 13:54:43
 *
 */
public class XMLElementValue extends XMLElement {

	String value;

	/**
	 * @param name
	 */
	public XMLElementValue(String name, String[] path, String value) {
		super(name, path);
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see net.InPanic.IPSDParser.XML.XMLObject#print()
	 */
	@Override
	public String print(int tab) {
		String attributeChain = "";
		for (XMLAttribute at : attributes)
			attributeChain = attributeChain.concat(at.print(0));
		String tabs = "";
		for (int i = 0; i < tab; i++)
			tabs = tabs.concat("\t");
		return tabs.concat("<").concat(name).concat(attributeChain).concat(">").concat(value).concat("</").concat(name).concat(">\n");
	}
}
