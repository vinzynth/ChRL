/**
 * @author Christian Richard Leopold - ChRL
 * Sep 7, 2015 - 2:28:57 PM
 * chrl-vaadin-utils
 * at.chrl.vaadin.component.generator
 */
package at.chrl.vaadin.component.generator;

import java.util.Objects;
import java.util.Set;

import org.vaadin.viritin.FilterableListContainer;

import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import at.chrl.nutils.CollectionUtils;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Sep 7, 2015 - 2:28:57 PM
 *
 */
@SuppressWarnings("serial")
public class GeneratedAbstractGrid<T> extends HorizontalLayout {

	private static final ComponentGenerator COMP_GEN = new ComponentGeneratorImpl();
	
	private final Grid grid;
	private final FilterableListContainer<T> listContainer;
	private final Set<SaveListener<T>> saveListener;

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	GeneratedAbstractGrid(Class<T> type) {
		this.listContainer = new FilterableListContainer<>(type);
		this.grid = new Grid(type.getSimpleName(), listContainer);
		this.grid.setEditorEnabled(false);
		this.grid.setSizeFull();
		
		this.saveListener = CollectionUtils.newSet();
		
		this.setSizeFull();
		this.setSpacing(true);
		
		VerticalLayout left = new VerticalLayout();
		VerticalLayout right = new VerticalLayout();
		
		left.addComponent(this.grid);
		
		this.grid.addSelectionListener(e -> {
			T t = (T) e.getSelected().stream().findFirst().orElse(null);
			
			right.removeAllComponents();
			if(Objects.nonNull(t)){
				GeneratedAbstractField<T> field = COMP_GEN.generate(t);
				field.addSaveListener(c -> {
					this.listContainer.getItemIds().remove(t);
					this.saveListener.forEach(sl -> sl.saveOnSave(field));
					this.listContainer.addItem(field.getValue());
				});
			}
		});
		
		this.addComponent(left);
		this.addComponent(right);
	}
	
	public Grid getGrid(){
		return grid;
	}
	
	public FilterableListContainer<T> getContainer(){
		return listContainer;
	}
	
	public boolean addSaveListener(SaveListener<T> listener){
		return saveListener.add(listener);
	}
	
	@FunctionalInterface
	public static interface SaveListener<T> {
		
		default void saveOnSave(GeneratedAbstractField<T> f){
			final T backUp = f.getValue();
			try {
				this.onSave(f);
			} catch (Exception e) {
				f.setValue(backUp);
				e.printStackTrace();
			}
		}
		
		public void onSave(GeneratedAbstractField<T> f) throws Exception;
	}
}
