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
package at.chrl.vaadin.test;

import java.util.Date;

import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import at.chrl.vaadin.component.generator.annotations.ComponentField;
import at.chrl.vaadin.component.generator.annotations.ComponentValidator;
import at.chrl.vaadin.component.generator.annotations.VaadinComponent;

/**
 * @author Vinzynth
 * 29.08.2015 - 02:35:15
 *
 */
@VaadinComponent
public class TestEntity {
	
	@ComponentField(
			value = TextField.class,
			validators = {
					@ComponentValidator(IntegerRangeValidator.class)
	})
	private int id;
	
	@ComponentField(
			value = TextField.class,
			validators = {
					@ComponentValidator(NullValidator.class)
	})
	private String title;
	
	@ComponentField(TextArea.class)
	private String test;
	
	@ComponentField(DateField.class)
	private Date date;


	public TestEntity() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
