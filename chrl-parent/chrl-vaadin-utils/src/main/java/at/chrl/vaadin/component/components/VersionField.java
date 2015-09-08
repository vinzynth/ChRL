/**
 * @author Christian Richard Leopold - ChRL
 * Sep 8, 2015 - 10:40:10 AM
 * chrl-vaadin-utils
 * at.chrl.vaadin.component.components
 */
package at.chrl.vaadin.component.components;

import java.util.Date;

import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

import at.chrl.vaadin.component.generator.GeneratedAbstractGrid;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Sep 8, 2015 - 10:40:10 AM
 *
 */
@SuppressWarnings("serial")
public class VersionField extends CustomField<Date> {
	
	private DateField dateField;
	private Button history;
	
	private HistoryListener listener;
	private HorizontalLayout lay;
	
	/**
	 * 
	 */
	public VersionField() {
		this.listener = () -> new GeneratedAbstractGrid<Date>(Date.class, true);
		this.dateField = new DateField();
		this.dateField.setDateFormat("dd.MM.yyyy - HH:mm:ss");
		this.history = new Button("History", FontAwesome.CLOCK_O);
		this.history.addClickListener(e -> {
			GeneratedAbstractGrid<?> grid = this.listener.getGrid();
			grid.setSizeFull();
			Window window = new Window("Version History", grid);
			window.setSizeFull();
			getUI().addWindow(window);
		});
		this.lay = new HorizontalLayout();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.vaadin.ui.CustomField#initContent()
	 */
	@Override
	protected Component initContent() {
		lay.addComponents(this.dateField, this.history);
		return lay;
	}

	/**
	 * {@inheritDoc}
	 * @see com.vaadin.ui.AbstractField#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean readOnly) {
		this.history.setVisible(!readOnly);
		this.dateField.setReadOnly(readOnly);
	}
	
	/**
	 * getter for listener
	 * @return listener
	 * 		VersionField.listener
	 */
	public HistoryListener getHistoryListener() {
		return listener;
	}

	/**
	 * setter for listener
	 * @param listener
	 *		new listener to set
	 */
	public void setHistoryListener(HistoryListener listener) {
		this.listener = listener;
	}

	/**
	 * {@inheritDoc}
	 * @see com.vaadin.ui.AbstractComponent#setCaption(java.lang.String)
	 */
	@Override
	public void setCaption(String caption) {
		lay.setCaption(caption);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.vaadin.ui.AbstractComponent#getCaption()
	 */
	@Override
	public String getCaption() {
		return lay.getCaption();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.vaadin.ui.AbstractField#getValue()
	 */
	@Override
	public Date getValue() {
		return dateField.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.vaadin.ui.AbstractField#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Date newFieldValue) throws com.vaadin.data.Property.ReadOnlyException, ConversionException {
		dateField.setValue(newFieldValue);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.vaadin.ui.AbstractField#getType()
	 */
	@Override
	public Class<? extends Date> getType() {
		return Date.class;
	}
	
	@FunctionalInterface
	public static interface HistoryListener{
		GeneratedAbstractGrid<?> getGrid();
	}
}
