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
package at.chrl.orm.hibernate.datatypes;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

import org.hibernate.envers.Audited;
import org.hibernate.type.CustomType;

import at.chrl.nutils.CollectionUtils;
import at.chrl.nutils.interfaces.INestedCollection;

/**
 * {@link Audited} is not supported, since some datastores may have troubles 
 * mapping custom hibernate types({@link CustomType}).
 * 
 * 
 * @author Vinzynth
 * 02.08.2015 - 01:51:44
 *
 */
@Entity
public class MultiMapEntry implements INestedCollection<Collection<Long>, Long>, ObjectMapable<Long>{

	@Id
	@GeneratedValue
	private Long id;
	
	@Version
	private Date version;
	
	private boolean isSet;
	
	@ElementCollection
	private List<Long> datalist;
	
	@ElementCollection
	private Set<Long> dataset;
	
	public MultiMapEntry() {
		this.datalist = CollectionUtils.newList();
		this.dataset = CollectionUtils.newSet();
	}
	
	public MultiMapEntry(boolean isSet) {
		this();
		this.isSet = isSet;
	}
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.nutils.interfaces.INestedCollection#getNestedCollection()
	 */
	@Override
	public Collection<Long> getNestedCollection() {
		return isSet ? dataset : datalist;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.orm.hibernate.datatypes.ObjectMapable#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @return the isSet
	 */
	public boolean isSet() {
		return isSet;
	}

	/**
	 * @param isSet the isSet to set
	 */
	public void setSet(boolean isSet) {
		this.isSet = isSet;
	}

	/**
	 * @return the version
	 */
	public Date getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Date version) {
		this.version = version;
	}

}
