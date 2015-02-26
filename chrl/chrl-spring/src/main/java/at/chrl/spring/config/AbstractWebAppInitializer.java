package at.chrl.spring.config;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.context.ContextCleanupListener;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import at.chrl.nutils.ArrayUtils;
import at.chrl.vaadin.SpringUIProvider;
import at.chrl.vaadin.ui.BasicUI;

import com.vaadin.server.VaadinServlet;

/**
 * 
 * @author Leopold Christian
 * 
 * @see {@link WebAppInitializer} as example implementation
 *
 */
public abstract class AbstractWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	protected abstract Class<?>[] getConfigClasses();

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return (Class<?>[]) ArrayUtils.add(getConfigClasses(), ApplicationConfig.class);
	};

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebMvcConfig.class };
	}

	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);

		return new Filter[] { characterEncodingFilter };
	}

	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setInitParameter("defaultHtmlEscape", "true");
		registration.setInitParameter("spring.profiles.active", "default");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.web.servlet.support.AbstractDispatcherServletInitializer#onStartup(javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();

		rootContext.register(getRootConfigClasses());
		
		registerSpringContextLoaderListener(servletContext, rootContext);
		
		registerVaadinServlet(servletContext);
		registerDispatcherServlet(servletContext);
	}

	private void registerVaadinServlet(ServletContext servletContext) {

		VaadinServlet vaadinServlet = new VaadinServlet();
		ServletRegistration.Dynamic vaadinServletRegistration = servletContext.addServlet("vaadinServlet", vaadinServlet);
		vaadinServletRegistration.setInitParameter("ui", BasicUI.class.getName());
		vaadinServletRegistration.setInitParameter("UIProvider", SpringUIProvider.class.getName());
		vaadinServletRegistration.setLoadOnStartup(1);
		vaadinServletRegistration.addMapping("/ui/*");
		vaadinServletRegistration.addMapping("/VAADIN/*");

	}

	private void registerSpringContextLoaderListener(ServletContext servletContext, WebApplicationContext rootContext) {
		servletContext.addListener(new ContextLoaderListener(rootContext));
		servletContext.addListener(new ContextCleanupListener());
		servletContext.addListener(new RequestContextListener());
	}
}