/**
 * This file is part of InPanic-Core.
 *
 * InPanic-Core is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * InPanic-Core is distibuted in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with InPanic-Core. If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.exodus.XML;

/**
 * @author Vinzynth
 * 29.08.2013 - 01:26:56
 *
 */
public class XMLAttribute extends XMLObject {

	String value;
	
	public XMLAttribute(String name, String[] path,String value){
		super(name, path);
		this.value = value;
	}
	
	/**
	 * {@inheritDoc}
	 * @see net.InPanic.IPSDParser.XML.XMLObject#print(int)
	 */
	@Override
	public String print(int tab) {
		return " ".concat(name).concat("=").concat('"'+value+'"');
	}

	/**
	 * {@inheritDoc}
	 * @see net.InPanic.IPSDParser.XML.XMLObject#addXMLObject(net.InPanic.IPSDParser.XML.XMLObject)
	 */
	@Override
	public void addXMLObject(XMLObject o) {
		//ignore
	}
}