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
package at.chrl.spring.generics.repositories.utils;

import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * @author Vinzynth
 * 27.08.2015 - 20:26:38
 *
 */
public final class SpringUtils {

	@SuppressWarnings("unchecked")
	public static <T> T generateBean(ApplicationContext context, Class<T> cls, String name, Object... args){
		System.out.println("Register Bean:" + name);
		AbstractApplicationContext appCon = (AbstractApplicationContext) context;
		DefaultListableBeanFactory registry = (DefaultListableBeanFactory) appCon.getBeanFactory();
		AnnotatedGenericBeanDefinition bean = new AnnotatedGenericBeanDefinition(cls);
		bean.setAutowireCandidate(true);
		bean.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		ConstructorArgumentValues conVal = new ConstructorArgumentValues();
		for (Object object : args) {
			conVal.addGenericArgumentValue(object);
		}
		bean.setConstructorArgumentValues(conVal);
		String beanName = name+"_"+cls.getSimpleName();
		registry.registerBeanDefinition(beanName, bean);
		return (T) registry.getBean(beanName);
	}
}
