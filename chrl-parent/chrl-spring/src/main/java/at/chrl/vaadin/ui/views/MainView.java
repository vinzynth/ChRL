/**
 * @author bravestone Feb 20, 2015 - 5:37:59 PM bravestone-spring
 *         com.bravestone.vaadin.ui.views
 */
package at.chrl.vaadin.ui.views;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import at.chrl.nutils.JVMInfoUtil;
import at.chrl.vaadin.ui.BasicUIView;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * @author bravestone
 *
 */
@SuppressWarnings("serial")
public class MainView extends BasicUIView {

	private Label[] lbls;

	/**
	 * 
	 */
	public MainView() {
		super();
		Label header = new Label("JVM Information");

		addComponent(header);
		setComponentAlignment(header, Alignment.TOP_CENTER);

		lbls = new Label[] {};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		for (Label component : lbls)
			removeComponent(component);

		lbls = loadLabls();

		addComponents(lbls);
		Notification.show("Welcome", "to Backend UI", Type.TRAY_NOTIFICATION);
	}

	public Label[] loadLabls() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		JVMInfoUtil.getInstance().printAllInfos(new PrintStream(baos));
		String[] infos = baos.toString().split(System.lineSeparator());
		return Arrays.stream(infos).map(Label::new).toArray(Label[]::new);
	}
}
