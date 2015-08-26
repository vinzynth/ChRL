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

import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Spatial;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

import at.chrl.nutils.CollectionUtils;
import at.chrl.spring.generics.repositories.GenericIndexedRepository;
import at.chrl.spring.generics.repositories.GenericRepository;
import at.chrl.spring.generics.repositories.IndexSearcher;
import at.chrl.spring.generics.repositories.SpatialIndexSearcher;
import at.chrl.spring.hibernate.config.RepositoryHolder;
import at.chrl.spring.hibernate.config.SpringJpaConfig;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Aug 21, 2015 - 4:58:25 PM
 *
 */
public class RepositoryHolderImplementation implements RepositoryHolder, ApplicationContextAware {

	private Map<Class<?>, GenericRepository<?>> repositories;
	private SpringJpaConfig jpaConfig;
	private Collection<GenericRepository<?>> autoWiredRepositories;
	private Map<Class<?>, Map<Class<?>, IndexSearcher<?>>> indexSearchers;
	
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
	private <T> GenericRepository<T> registerRepositoryBean(DefaultListableBeanFactory registry, Class<T> genericType){
		boolean indexed = Objects.nonNull(genericType.getAnnotation(Indexed.class));
		
		AnnotatedGenericBeanDefinition bean = new AnnotatedGenericBeanDefinition(indexed ? GenericIndexedRepository.class : GenericRepository.class);
		bean.setAutowireCandidate(true);
		bean.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
		ConstructorArgumentValues conVal = new ConstructorArgumentValues();
		conVal.addGenericArgumentValue(genericType);
		bean.setConstructorArgumentValues(conVal);
		String beanName = (indexed ? "Indexed" : "") + "GenericRepository_"+genericType.getSimpleName();
		registry.registerBeanDefinition(beanName, bean);
		
		GenericRepository<T> repo = (GenericRepository<T>) registry.getBean(beanName);
		
		if(indexed){
			Collection<IndexSearcher<T>> searchers = registerIndexSearcher(registry, genericType, repo);
			indexSearchers.putIfAbsent(genericType, CollectionUtils.<Class<?>, IndexSearcher<?>>newMap());
			searchers.forEach(s -> {
				indexSearchers.get(genericType).put(s.getClass(), s);				
			});
		}
		return repo;
	}
	
	@SuppressWarnings("unchecked")
	private <T> Collection<IndexSearcher<T>> registerIndexSearcher(DefaultListableBeanFactory registry, Class<T> genericType, GenericRepository<T> repo){
		
		Collection<IndexSearcher<T>> searchers = CollectionUtils.newList();
		
		boolean spatial = Objects.nonNull(genericType.getAnnotation(Spatial.class));
		if(spatial) {
			AnnotatedGenericBeanDefinition bean = new AnnotatedGenericBeanDefinition(SpatialIndexSearcher.class);
			bean.setAutowireCandidate(true);
			bean.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
			ConstructorArgumentValues conVal = new ConstructorArgumentValues();
			conVal.addGenericArgumentValue(repo);
			bean.setConstructorArgumentValues(conVal);
			String beanName = "SpatialIndexSearcher_"+genericType.getSimpleName();
			registry.registerBeanDefinition(beanName, bean);
			searchers.add((IndexSearcher<T>) registry.getBean(beanName));
		}
		
		return searchers;
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
		this.indexSearchers = CollectionUtils.newMap();
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
	 * @see at.chrl.spring.hibernate.config.RepositoryHolder#getIndexSearcher(java.lang.Class, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <S extends IndexSearcher<T>, T> S getIndexSearcher(Class<S> searcherClass, Class<T> type) {
		return (S) indexSearchers.getOrDefault(type, Collections.emptyMap()).getOrDefault(searcherClass, null);
	}

}
