package at.chrl.vaadin.ui;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.chrl.vaadin.ui.views.ConfigView;
import at.chrl.vaadin.ui.views.MainView;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Title("Backend UI")
@Component
@PreserveOnRefresh
@Scope("request")
public class BasicUI extends UI {
	
	Navigator nav;

	enum Views{
		MAIN("Main", MainView.class),
		CONFIG("Active Configuration", ConfigView.class);
		
		private String string;
		private Class<? extends View> viewClass;

		private Views(String string, Class<? extends View> viewClass) {
			this.string = string;
			this.viewClass = viewClass;
		}
		
		/**
		 * {@inheritDoc}
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return string;
		}
	}

	@Override
	protected void init(VaadinRequest request) {
		nav = new Navigator(this, this);
		
		for (Views view : Views.values()) {
			nav.addView(view.name(), view.viewClass);
		}
		
		nav.navigateTo(Views.MAIN.name());
		
	}

}