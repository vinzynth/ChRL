/**
 * @author Christian Richard Leopold - ChRL
 * Sep 8, 2015 - 11:08:16 AM
 * chrl-vaadin-utils
 * at.chrl.vaadin.component.generator
 */
package at.chrl.vaadin.component.generator;

import java.util.Collection;
import java.util.stream.Collectors;

import at.chrl.vaadin.component.components.VersionField;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Sep 8, 2015 - 11:08:16 AM
 *
 */
public final class ComponentGeneratorUtils {

	public static Collection<VersionField> getVersionFields(GeneratedAbstractField<?> gaf){
		return gaf.getFields().stream()
		.filter(f -> {
			return f instanceof VersionField;
		})
		.map(f -> {
			return (VersionField) f;
		})
		.collect(Collectors.toList());
	}
	
}
