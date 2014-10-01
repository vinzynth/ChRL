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

import java.util.ArrayList;

/**
 * @author Vinzynth
 * 29.08.2013 - 01:27:18
 *
 */
public class XMLElement extends XMLObject {
	
	protected ArrayList<XMLAttribute> attributes;
	protected ArrayList<XMLElement> elements;
		
	public XMLElement(String name, String[] path){
		super(name, path);
		this.attributes = new ArrayList<XMLAttribute>(15);
		this.elements = new ArrayList<XMLElement>(5);
	}
	
	
	public boolean addElement(XMLElement el){
		return elements.contains(el) ? false : elements.add(el);
	}
	
	public boolean addAttribute(XMLAttribute at){
		return attributes.contains(at) ? false : attributes.add(at);
	}
	
	/**
	 * @return the attributes
	 */
	@Override
	public ArrayList<XMLAttribute> getAttributes() {
		return attributes;
	}
	
	/**
	 * @return the elements
	 */
	@Override
	public ArrayList<XMLElement> getElements() {
		return elements;
	}

	/**
	 * {@inheritDoc}
	 * @see net.InPanic.IPSDParser.XML.XMLObject#print()
	 */
	@Override
	public String print(int tab) {
		String attributeChain = "";
		for (XMLAttribute at : attributes)
			attributeChain = attributeChain.concat(at.print(0));
		String elementString = "";
		for (XMLElement el : elements)
			elementString = elementString.concat(el.print(tab+1));
		String tabs = "";
		for (int i = 0; i < tab; i++)
			tabs = tabs.concat("\t");
		
		String endname = name;
		if(endname.contains(" "))
			endname = endname.substring(0, endname.indexOf(" "));
		
		return tabs.concat("<").concat(name).concat(attributeChain).concat(elements.size() <= 0 ? "/>\n" : ">\n".
				concat(elementString).
				concat(tabs).concat("</").concat(endname).concat(">\n"));
	}

	/**
	 * {@inheritDoc}
	 * @see net.InPanic.IPSDParser.XML.XMLObject#addXMLObject(net.InPanic.IPSDParser.XML.XMLObject)
	 */
	@Override
	public void addXMLObject(XMLObject o) {
		if(o instanceof XMLElement)
			this.addElement((XMLElement)o);
		else if(o instanceof XMLAttribute)
			this.addAttribute((XMLAttribute)o);
	}
}
