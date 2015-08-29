/**
 * This file is part of ChRL Util Collection.
 * 
 * ChRL Util Collection is free software: you can redistribute it and/or
 * modify  it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * ChRL Util Collection is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ChRL Util Collection.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.vaadin.component.generator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.vaadin.data.Validator;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import at.chrl.nutils.DatasetGenerator;
import at.chrl.nutils.Memoizer;
import at.chrl.vaadin.component.generator.annotations.ComponentCollection;
import at.chrl.vaadin.component.generator.annotations.ComponentField;
import at.chrl.vaadin.component.generator.annotations.ComponentValidator;
import at.chrl.vaadin.component.generator.annotations.VaadinComponent;

/**
 * @author Vinzynth
 * 29.08.2015 - 02:47:06
 * 
 * <p>
 * TODO:	<br> Implment IO -> Generate {@link AbstractField} instaead of component
 * 			<br> Implement {@link ComponentCollection}
 * 			<br> Implement a save button // listeners
 * 			<br> warnings and error handling
 */
public class ComponentGeneratorImpl implements ComponentGenerator{

	private final Predicate<Class<?>> IS_VAADIN_COMPONENT = Memoizer.memoizePredicate(c -> c.isAnnotationPresent(VaadinComponent.class));
	private final Function<Class<?>, Supplier<? extends Component>> COMPONENT_SUPPLIER = Memoizer.memoize(this::getComponent);
	private final Function<Field, Supplier<? extends Component>> FIELD_SUPPLIER = Memoizer.memoize(this::getSupplierForField);
	
	private final DatasetGenerator COMPONENT_GENERATOR = new DatasetGenerator();
	
	private final <T> Supplier<? extends Component> getComponent(Class<T> cls){
		List<Supplier<? extends Component>> fieldSupplier = Arrays.stream(cls.getDeclaredFields())
			.filter(f -> f.isAnnotationPresent(ComponentField.class))
			.map(FIELD_SUPPLIER::apply)
			.collect(Collectors.toList());
		
		return () -> {
			VerticalLayout layout = new VerticalLayout();
			layout.setSpacing(true);
			layout.setMargin(true);
			
			fieldSupplier.forEach(s -> layout.addComponent(s.get()));
			
			return layout;
		};
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private final <T> Supplier<Component> getSupplierForField(Field field){
		String error = "";
		ComponentField annotation = field.getAnnotation(ComponentField.class);
		try {
			Class<T> fieldType = (Class<T>) field.getType();
			Class<? extends AbstractField<T>> componentType = (Class<? extends AbstractField<T>>) annotation.value();
			
			List<String> calls = Arrays.asList(annotation.callsAfterCreation());
			Method[] declaredMethods = componentType.getDeclaredMethods();
			
			List<Method> callingMethods = Arrays.stream(declaredMethods).filter(m -> calls.contains(m.getName())).collect(Collectors.toList());
			
			// TODO: warn if some methods wheren't found
			
			Validator[] validators = Arrays.stream(annotation.validators())
					.map(ComponentValidator::value)
					.map(COMPONENT_GENERATOR::createInstanceOnly)
					.toArray(Validator[]::new);
			
			Supplier<Component> returnMe = () -> {
				AbstractField<T> component = COMPONENT_GENERATOR.createInstanceOnly(componentType);
				
				try {
					for (Method method : callingMethods) {
						method.invoke(component);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(!annotation.caption().isEmpty())
					component.setCaption(annotation.caption());
				if(!annotation.description().isEmpty())
					component.setDescription(annotation.description());
				
				component.setReadOnly(annotation.readOnly());
				
				Arrays.asList(validators).forEach(component::addValidator);
				
				return component;
			};
			
			returnMe.get(); // Test Supplier
			
			return returnMe;
		} catch (ClassCastException e) {
			String err = "Annotation type Exception for Field " + field.getName() + " in Class" + field.getDeclaringClass().getName() + " | AbstractField type and Field type dont match";
			System.err.println(e.getMessage() + " | " + err);
			e.printStackTrace();
			error = err + System.lineSeparator() + e.toString();
			
			// Fallback Component
			try {
				Class<? extends Component> comp = annotation.value();
				Supplier<Component> returnMe = () -> COMPONENT_GENERATOR.createInstanceOnly(comp);
				
				returnMe.get();
				
				return returnMe;
			} catch (Exception e2) {
				System.err.println("Annotated Class type can not be instantiated as component: " + e.getMessage());
				e2.printStackTrace();
				error = e2.toString();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			error = e.toString();
		}
		final String fError = error;
		return () -> new Label(fError);
	}

	/**
	 * {@inheritDoc}
	 * @see com.bravestone.diango.gui.ComponentGenerator#generate(java.lang.Object)
	 */
	@Override
	public Component generate(Class<?> cls) {
		if(!IS_VAADIN_COMPONENT.test(cls))
			return null;
		
		return COMPONENT_SUPPLIER.apply(cls).get();
	}
}
