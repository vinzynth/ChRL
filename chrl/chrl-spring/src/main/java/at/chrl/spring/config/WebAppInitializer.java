/**
 * @author bravestone Feb 20, 2015 - 11:26:52 AM
 */
package at.chrl.spring.config;

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
		return new Class<?>[] {};
	}

}
