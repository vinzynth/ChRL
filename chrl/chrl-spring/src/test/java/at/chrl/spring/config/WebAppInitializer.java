/**
 * @author bravestone Feb 20, 2015 - 11:26:52 AM
 */
package at.chrl.spring.config;

import javax.servlet.ServletContext;

/**
 * @author Leopold Christian
 * 
 *
 */
public class WebAppInitializer extends AbstractWebAppInitializer {

	/**
	 * {@inheritDoc}
	 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getRootConfigClasses()
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] {ApplicationConfig.class};
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.config.AbstractWebAppInitializer#registerOtherVaadinServlet(javax.servlet.ServletContext)
	 */
	@Override
	protected void registerOtherVaadinServlet(ServletContext servletContext) {
		// TODO Auto-generated method stub
		
	}

}
