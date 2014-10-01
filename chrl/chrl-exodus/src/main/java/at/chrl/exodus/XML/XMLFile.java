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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.LinkedList;

import at.chrl.logging.Logger;

/**
 * @author Vinzynth
 * 29.08.2013 - 14:11:00
 *
 */
public class XMLFile {

	private static final Logger log = new Logger();
	
	final static String encoding ="UTF-8";
	
	private String name;
	private String path;
	
	private LinkedList<XMLObject> entries;
	
	public XMLFile(String name, String path){
		this.name = name;
		this.path = path;
		this.entries = new LinkedList<XMLObject>();
	}
	
	public String getName(){
		return name;
	}
	
	public void addEntries(LinkedList<XMLObject> add){
		entries.addAll(add);
	}
	
	public void writeOut(){
		try {
			File xml = new File(".".concat(File.separator).concat(path));
			xml.getParentFile().mkdirs();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xml), encoding));
			log.info("Write out XML-File: ".concat(name));
			bw.append("<?xml version="+'"'+"1.0" + '"' + "encoding=" + '"' + encoding + '"' + "?>\n");
			bw.append("<!-- Created by InPanicParser ".concat(new Date().toString()).concat(" -->\n"));
			bw.append("<".concat(name.substring(0, name.indexOf("."))).concat(">\n"));
			for (XMLObject x : entries)
				bw.append(x.print(1));
			log.info("XML File succesfully created: ".concat(".".concat(File.separator).concat(path)));
			bw.append("</".concat(name.substring(0, name.indexOf("."))).concat(">"));
			bw.close();
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
	}
}
