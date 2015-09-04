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

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import at.chrl.orm.hibernate.ExtendedSessionTemplate;
import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.orm.hibernate.configuration.IHibernateConfig;
import at.chrl.orm.hibernate.configuration.JPAConfig;

/**
 * @author Vinzynth
 * 27.08.2015 - 21:48:42
 *
 */
public class SessionTemplateFactoryImpl implements SessionTemplateFactory{

	@Autowired(required = true)
	private SpringGeneratedJpaConfig jpaConfig;

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.hibernate.config.SessionTemplateFactory#createTemplate()
	 */
	@Override
	public SessionTemplate createTemplate() {
		return new SessionTemplate(){

			@Override
			protected IHibernateConfig getHibernateConfig() {
				return jpaConfig;
			}
		};
	}
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.hibernate.config.SessionTemplateFactory#createTemplate(at.chrl.orm.hibernate.configuration.JPAConfig)
	 */
	@Override
	public SessionTemplate createTemplate(JPAConfig jpaConfig) {
		return new SessionTemplate(){

			@Override
			protected IHibernateConfig getHibernateConfig() {
				return jpaConfig;
			}
		};
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.hibernate.config.SessionTemplateFactory#createTemplate(org.hibernate.Session)
	 */
	@Override
	public ExtendedSessionTemplate createTemplate(Session session) {
		return new ExtendedSessionTemplate(session) {
			
			@Override
			protected IHibernateConfig getHibernateConfig() {
				return jpaConfig;
			}
		};
	}
}
