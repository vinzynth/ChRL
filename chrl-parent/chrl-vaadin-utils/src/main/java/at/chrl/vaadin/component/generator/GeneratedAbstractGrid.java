/**
 * @author Christian Richard Leopold - ChRL
 * Sep 7, 2015 - 2:28:57 PM
 * chrl-vaadin-utils
 * at.chrl.vaadin.component.generator
 */
package at.chrl.vaadin.component.generator;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.vaadin.viritin.FilterableListContainer;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import at.chrl.nutils.CollectionUtils;
import at.chrl.nutils.DatasetGenerator;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Sep 7, 2015 - 2:28:57 PM
 *
 */
@SuppressWarnings("serial")
public class GeneratedAbstractGrid<T> extends HorizontalLayout {

	private static final ComponentGenerator COMP_GEN = new ComponentGeneratorImpl();
	private static final DatasetGenerator DATA_GEN = new DatasetGenerator();
	
	private final Grid grid;
	private final FilterableListContainer<T> listContainer;
	private final Set<ChangeListener<T>> saveListener;
	private final Set<ChangeListener<T>> deleteListener;

	private Button addButton;
	private Button deleteButton;

	private VerticalLayout left;
	private VerticalLayout right;
	private GeneratedAbstractField<T> field;
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	GeneratedAbstractGrid(Class<T> type) {
		this.listContainer = new FilterableListContainer<>(type);
		this.grid = new Grid(type.getSimpleName(), listContainer);
		this.grid.setSelectionMode(SelectionMode.SINGLE);
		this.grid.setEditorEnabled(false);
		this.grid.setSizeFull();
		
		this.saveListener = CollectionUtils.newSet();
		this.deleteListener = CollectionUtils.newSet();
		
		this.setSizeFull();
		this.setSpacing(true);
		
		this.deleteButton = new Button("Delete", FontAwesome.MINUS);
		this.deleteButton.addClickListener(e -> {
			T t = (T) this.grid.getSelectedRow();
			if(Objects.nonNull(t) && Objects.nonNull(this.field)){
				this.listContainer.removeItem(t);
				this.deleteListener.forEach(sl -> sl.saveOnChange(this.field));
				this.right.removeAllComponents();
				this.right.addComponent(this.addButton);	
			}
		});
		
		this.addButton = new Button("New", FontAwesome.PLUS);
		this.addButton.addClickListener(e -> {
			this.createEditor(DATA_GEN.createInstanceOnly(type));
		});
		this.grid.addSelectionListener(e -> {
			T t = (T) this.grid.getSelectedRow();
			if(Objects.nonNull(t)) {
				this.right.addComponent(this.deleteButton);
			}
			this.createEditor(t);
		});
		
		
		
		this.left = new VerticalLayout();
		this.right = new VerticalLayout();
		
		this.left.setSpacing(true);
		this.right.setSpacing(true);
		
		this.left.addComponent(this.grid);
		
		this.right.addComponent(this.addButton);
		
		this.addComponents(this.left, this.right);
	}
	
	private void createEditor(T t){
		this.right.removeAllComponents();
		if(Objects.nonNull(t)){
			this.field = COMP_GEN.generate(t);
			this.field.addSaveListener(c -> {
				this.listContainer.getItemIds().remove(t);
				this.saveListener.forEach(sl -> sl.saveOnChange(this.field));
				if(t instanceof Comparable){
					Set<T> set = new TreeSet<>(this.listContainer.getItemIds());
					set.add(this.field.getValue());
					this.listContainer.removeAllItems();
					this.listContainer.addAll(set);
				}
				else
					this.listContainer.addItem(this.field.getValue());
				this.right.removeAllComponents();
				this.right.addComponent(this.addButton);	
			});
			this.right.addComponent(this.field);
		}
		else
			this.right.addComponent(this.addButton);
	}
	
	public Grid getGrid(){
		return grid;
	}
	
	public FilterableListContainer<T> getContainer(){
		return listContainer;
	}
	
	public boolean addDeleteListener(ChangeListener<T> listener){
		return deleteListener.add(listener);
	}
	
	public boolean addSaveListener(ChangeListener<T> listener){
		return saveListener.add(listener);
	}
	
	@FunctionalInterface
	public static interface ChangeListener<T> {
		
		default void saveOnChange(GeneratedAbstractField<T> f){
			final T backUp = f.getValue();
			try {
				this.run(f);
			} catch (Exception e) {
				f.setValue(backUp);
				e.printStackTrace();
			}
		}
		
		public void run(GeneratedAbstractField<T> f) throws Exception;
	}
}
