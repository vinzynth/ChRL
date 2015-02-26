package at.chrl.spring.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import at.chrl.nutils.JVMInfoUtil;
import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.PropertiesUtils;

@RestController
public class BasicController {

	@RequestMapping("exception")
	public boolean throwException() {
		throw new RuntimeException("Test Exception thrown by request at " + new Date().toString());
	}

	@RequestMapping("jvm")
	public String getJVMInfo() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JVMInfoUtil.printAllInfos(new PrintStream(baos));
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

@ControllerAdvice
@EnableWebMvc
class DefaultExceptionHandler {

	public DefaultExceptionHandler() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Handle exceptions thrown by handlers.
	 */
	@ExceptionHandler(value = Exception.class)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		e.printStackTrace();
		// If the exception is annotated with @ResponseStatus rethrow it and let
		// the framework handle it
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
			throw e;

		// Otherwise setup and send the user to a default error-view.
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", e);
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("error");
		return mav;
	}
}