///**
// * @author bravestone Feb 12, 2015 - 6:08:35 PM bravestone-dataProvider
// *         com.bravestone.diana.repositories.generics.controller
// */
//package at.chrl.spring.generics.repositories.controller;
//
//import java.util.Collection;
//import java.util.function.Supplier;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import at.chrl.spring.generics.repositories.GenericRepository;
//import at.chrl.spring.generics.rss.EmptyRSSView;
//import at.chrl.spring.generics.rss.GenericRSSView;
//import at.chrl.spring.generics.rss.IRSSController;
//
///**
// * @author bravestone
// * @param <T>
// * @param <R>
// */
//public abstract class RepositoryController<R extends GenericRepository<T>, T> {
//
//	@SuppressWarnings("unchecked")
//	public RepositoryController() {
//		if (this instanceof IRSSController)
//			rssSupplier = () -> ((IRSSController<GenericRSSView<T>, T>) this).getRss(repo.getLast(100));
//		else
//			rssSupplier = () -> new ModelAndView(new EmptyRSSView());
//	}
//
//	private final Supplier<ModelAndView> rssSupplier;
//
//	@Autowired(required = true)
//	protected R repo;
//
//	@RequestMapping("get")
//	public T getById(@RequestParam(value = "id") int id) {
//		return repo.getById(id);
//	}
//
//	@RequestMapping("getall")
//	public Collection<T> getAll(@RequestParam(value = "maxResults",
//			defaultValue = "100") int maxResults) {
//		return repo.getAll(maxResults);
//	}
//
//	@RequestMapping("hql")
//	public Collection<T> getByHql(@RequestParam(value = "query") String query, @RequestParam(
//			value = "maxResults", defaultValue = "0") int maxResults) {
//		return repo.executeHQLQuery(query, maxResults);
//	}
//
//	@RequestMapping("sql")
//	public Collection<T> getBySql(@RequestParam(value = "query") String query, @RequestParam(
//			value = "maxResults", defaultValue = "0") int maxResults) {
//		return repo.executeSQLQuery(query, maxResults);
//	}
//
//	@RequestMapping("named")
//	public Collection<T> getByNamedQuery(@RequestParam(value = "query") String query, @RequestParam(
//			value = "maxResults", defaultValue = "0") int maxResults) {
//		return repo.executeNamedQuery(query, maxResults);
//	}
//
//	@RequestMapping("count")
//	public long getCount() {
//		return repo.count();
//	}
//
//	@RequestMapping("rss")
//	public ModelAndView getRssFeed() {
//		return rssSupplier.get();
//	}
//}
