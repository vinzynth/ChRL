/**
 * @author Christian Richard Leopold - ChRL
 * Aug 31, 2015 - 6:14:01 PM
 * chrl-vaadin-utils
 * at.chrl.vaadin.component.generator
 */
package at.chrl.vaadin.component.generator;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 31, 2015 - 6:14:01 PM
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class GeneratedAbstractField<T> extends CustomField<T> {

	private final int fieldCount;
	private Class<T> type;
	private List<AccessTuple<?>> accessTuples;
	private List<AbstractField<?>> fields;
	private VerticalLayout layout;

	/**
	 * 
	 */
	public GeneratedAbstractField(Class<T> type, List<Component> components, List<AccessTuple<?>> accessTuples) {
		this.fields = components.stream().filter(c -> c instanceof AbstractField<?>).map(c -> (AbstractField<?>)c).collect(Collectors.toList());
		if(this.fields.size() != accessTuples.size())
			throw new IllegalArgumentException("Fields and AccessTuple count does not match: " + components.size() + " vs. " + accessTuples.size());
		
		this.fieldCount = components.size();
		this.type = type;
		this.accessTuples = accessTuples;
		this.layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setMargin(true);
		components.forEach(layout::addComponent);
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.vaadin.ui.AbstractField#getType()
	 */
	@Override
	public Class<? extends T> getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 * @see com.vaadin.ui.AbstractField#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(T newFieldValue) throws com.vaadin.data.Property.ReadOnlyException, ConversionException {
		for (int i = 0; i < fieldCount; i++) {
			setFieldValue(fields.get(i), accessTuples.get(i));
		}
		super.setValue(newFieldValue);
	}
	
	private void setFieldValue(AbstractField field, AccessTuple tuple){
		field.setValue(tuple.getGetter().apply(this));
	}
	
	private void getFieldValue(AbstractField field, AccessTuple tuple){
		tuple.getSetter().accept(this, field.getValue());
	}
	
	/**
	 * {@inheritDoc}
	 * @see com.vaadin.ui.AbstractField#getValue()
	 */
	@Override
	public T getValue() {
		for (int i = 0; i < fieldCount; i++) {
			getFieldValue(fields.get(i), accessTuples.get(i));
		}
		return super.getValue();
	}

	/**
	 * {@inheritDoc}
	 * @see com.vaadin.ui.CustomField#initContent()
	 */
	@Override
	protected Component initContent() {
		return layout;
	}
}
