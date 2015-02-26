/**
 * @author bravestone
 * Feb 20, 2015 - 4:34:35 PM
 * bravestone-spring
 * com.bravestone.vaadin
 */
package at.chrl.vaadin;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.server.DefaultUIProvider;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 * @author bravestone
 *
 */
@SuppressWarnings("serial")
public class SpringUIProvider extends DefaultUIProvider {

	private ApplicationContext context;

	@Override
	public UI createInstance(UICreateEvent event) {
		ensureInitialized();
		if (context == null) {
			throw new IllegalStateException("Couldn't load spring context.");
		}
		return context.getBean(event.getUIClass());
	}

	private void ensureInitialized() {
		if (context == null) {
			init();
		}
	}

	private void init() {
		VaadinServlet vaadinServlet = VaadinServlet.getCurrent();
		if (vaadinServlet == null) {
			System.err.println("Couldn't get current instance of VaadinServlet");
			return;
		}
		context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(vaadinServlet
						.getServletContext());
	}

}