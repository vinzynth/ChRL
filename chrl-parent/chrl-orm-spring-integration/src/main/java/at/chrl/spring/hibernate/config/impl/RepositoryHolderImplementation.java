/**
 * @author Christian Richard Leopold - ChRL
 * Aug 21, 2015 - 4:58:25 PM
 * chrl-orm-spring-integration
 * at.chrl.spring.hibernate.config.impl
 */
package at.chrl.spring.hibernate.config.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

import at.chrl.nutils.CollectionUtils;
import at.chrl.spring.generics.repositories.GenericIndexedRepository;
import at.chrl.spring.generics.repositories.GenericRepository;
import at.chrl.spring.hibernate.config.RepositoryHolder;
import at.chrl.spring.hibernate.config.SpringJpaConfig;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 21, 2015 - 4:58:25 PM
 *
 */
public class RepositoryHolderImplementation implements RepositoryHolder, ApplicationContextAware, BeanPostProcessor {

	private Map<Class<?>, GenericRepository<?>> repositories;
	private SpringJpaConfig jpaConfig;
	private Collection<GenericRepository<?>> autoWiredRepositories;
	
	/**
	 * 
	 */
	public RepositoryHolderImplementation(SpringJpaConfig jpaConfig, Collection<GenericRepository<?>> autoWiredRepositories) {
		this.jpaConfig = jpaConfig;
		if(Objects.isNull(autoWiredRepositories))
			this.autoWiredRepositories = Collections.emptyList();
		else
			this.autoWiredRepositories = autoWiredRepositories;

	}
	
	@SuppressWarnings("unchecked")
	private <T> GenericIndexedRepository<T> registerRepositoryBean(DefaultListableBeanFactory registry, Class<T> genericType){
		AnnotatedGenericBeanDefinition bean = new AnnotatedGenericBeanDefinition(GenericIndexedRepository.class);
		bean.setAutowireCandidate(true);
		bean.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		ConstructorArgumentValues conVal = new ConstructorArgumentValues();
		conVal.addGenericArgumentValue(genericType);
		bean.setConstructorArgumentValues(conVal);
		String beanName = "GenericIndexedRepository_"+genericType.getSimpleName();
		registry.registerBeanDefinition(beanName, bean);
		return (GenericIndexedRepository<T>) registry.getBean(beanName);
	}
	
	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.hibernate.config.RepositoryHolder#getRepository(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <R extends GenericRepository<T>, T> R getRepository(Class<T> cls) {
		if(!repositories.containsKey(cls))
			throw new RuntimeException("No Repository for class " + cls.getSimpleName() + " available.");	
		return (R) repositories.get(cls);
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.spring.hibernate.config.RepositoryHolder#getIndexedRepository(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <R extends GenericIndexedRepository<T>, T> R getIndexedRepository(Class<T> cls) {
		GenericRepository<?> genericRepository = repositories.get(cls);
		if(genericRepository instanceof GenericIndexedRepository)
			return (R) genericRepository;
		throw new RuntimeException("No GenericIndexedRepository for class " + cls.getSimpleName() + " available.");
	}
	
	/**
	 * {@inheritDoc}
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AbstractApplicationContext appCon = (AbstractApplicationContext) applicationContext;
		this.repositories = CollectionUtils.newMap();
		if(Objects.nonNull(autoWiredRepositories))
			autoWiredRepositories.forEach(r -> {
				this.repositories.putIfAbsent(r.getType(), r);
			});
		
		Predicate<Class<?>> conKey = this.repositories::containsKey;

		DefaultListableBeanFactory registry = (DefaultListableBeanFactory) appCon.getBeanFactory();
		jpaConfig.getMappedClasses().getAnnotatedClasses().stream()
			.filter(conKey.negate())
			.forEach(c -> {
				repositories.put(c, registerRepositoryBean(registry, c));
			});
	}

	/**
	 * {@inheritDoc}
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println(beanName);
		System.out.println(bean);
		return null;
	}

}
