/**
 * @author bravestone Feb 20, 2015 - 6:08:05 PM bravestone-spring
 *         com.bravestone.vaadin.ui
 */
package at.chrl.vaadin.ui;

import at.chrl.vaadin.ui.BasicUI.Views;

import com.vaadin.navigator.View;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;

/**
 * @author bravestone
 *
 */
@SuppressWarnings("serial")
public abstract class BasicUIView extends VerticalLayout implements View {

	/**
	 * 
	 */
	public BasicUIView() {

		MenuBar mb = new MenuBar();
		for (Views views : BasicUI.Views.values()) {
			mb.addItem(views.toString(), null, new Command() {

				@Override
				public void menuSelected(MenuItem selectedItem) {
					getUI().getNavigator().navigateTo(views.name());
				}
			});
		}

		addComponent(mb);
	}

}
