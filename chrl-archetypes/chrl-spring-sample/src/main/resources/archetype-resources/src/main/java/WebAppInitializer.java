package ${package};

import at.chrl.spring.config.AbstractWebAppInitializer;

public class WebAppInitializer extends AbstractWebAppInitializer {

	/**
	 * {@inheritDoc}
	 * @see org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer#getRootConfigClasses()
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{SpringConfig.class};
	}
	
}
