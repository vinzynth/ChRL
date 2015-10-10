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
package at.chrl.config.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import at.chrl.nutils.JVMInfoUtil;
import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.PropertiesUtils;

/**
 * @author Vinzynth
 * 05.10.2015 - 00:59:16
 *
 */
@RestController
public class JVMController {

	
	@RequestMapping("exception")
	public boolean throwException() {
		throw new RuntimeException("Test Exception thrown by request at " + new Date().toString());
	}

	@RequestMapping("jvm")
	public String getJVMInfo() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JVMInfoUtil.getInstance().printAllInfos(new PrintStream(baos));
		return baos.toString().replace(System.lineSeparator(), "<br>");
	}

	@RequestMapping("config")
	public String getConfigs() throws IOException {
		return prettyPrinted(getActiveProperties());
	}

	@RequestMapping("properties")
	public Properties[] getProperties() throws IOException {
		return getActiveProperties();
	}

	@RequestMapping("property")
	public Object getProperty(@RequestParam(value = "id") String id) throws IOException {
		for (Properties properties : getActiveProperties()) {
			Object object = properties.get(id);
			if (Objects.nonNull(object))
				return object;
		}
		return null;
	}

	private static String prettyPrinted(Object json) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
		return writer.writeValueAsString(json).replace(System.lineSeparator(), "<br>");
	}

	private static Properties[] getActiveProperties() throws IOException {
		if (Objects.nonNull(ConfigUtil.getConfigDirectory()))
			return PropertiesUtils.loadAllFromDirectory(ConfigUtil.getConfigDirectory(), false);
		return PropertiesUtils.loadAllFromDirectory(new File("."), false);
	}
	
}
