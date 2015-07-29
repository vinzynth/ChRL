/**
 * @author bravestone Feb 20, 2015 - 6:32:22 PM bravestone-spring
 *         com.bravestone.vaadin.ui.views
 */
package at.chrl.vaadin.ui.views;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.PropertiesUtils;
import at.chrl.vaadin.ui.BasicUIView;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Table;

/**
 * @author bravestone
 *
 */
@SuppressWarnings("serial")
public class ConfigView extends BasicUIView {

	private Table tbl;

	/**
	 * 
	 */
	public ConfigView() {
		super();
		tbl = new Table("Active Configuration:");

		tbl.addContainerProperty("Id", String.class, "");
		tbl.addContainerProperty("Value", String.class, "");

		tbl.setSizeFull();
		tbl.setHeight(20, Unit.CM);

		addComponent(tbl);

		loadProperties();

		tbl.setSortEnabled(true);
	}

	private void loadProperties() {
		try {
			for (Properties properties : getActiveProperties()) {
				properties.entrySet().forEach(e -> {
//					System.out.println(e.getKey() + " | " + e.getValue());
					tbl.addItem(new Object[] { e.getKey(), e.getValue() }, e.getKey());
				});
			}
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static Properties[] getActiveProperties() throws IOException {
		if (Objects.nonNull(ConfigUtil.getConfigDirectory()))
			return PropertiesUtils.loadAllFromDirectory(ConfigUtil.getConfigDirectory(), false);
		return PropertiesUtils.loadAllFromDirectory(new File("."), false);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		tbl.removeAllItems();
		loadProperties();
	}

}
