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
package at.chrl.spring.hibernate.config;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;

import at.chrl.orm.hibernate.ExtendedSessionTemplate;
import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.orm.hibernate.configuration.JPAConfig;

/**
 * @author Vinzynth
 * 30.07.2015 - 00:22:44
 *
 */
public interface SessionTemplateFactory {

	public SessionTemplate createTemplate();
	public SessionTemplate createTemplate(JPAConfig jpaConfig);
	public ExtendedSessionTemplate createTemplate(Session session);
	public default ExtendedSessionTemplate createTemplate(EntityManager entityManager){
		return createTemplate(entityManager.unwrap(HibernateEntityManager.class).getSession());
	}
}
