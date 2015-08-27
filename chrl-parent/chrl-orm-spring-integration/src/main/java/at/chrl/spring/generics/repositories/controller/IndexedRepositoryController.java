///**
// * @author bravestone Feb 12, 2015 - 6:18:20 PM bravestone-dataProvider
// *         com.bravestone.diana.repositories.generics.controller
// */
//package at.chrl.spring.generics.repositories.controller;
//
//import java.util.Collection;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import at.chrl.spring.generics.repositories.GenericIndexedRepository;
//
///**
// * @author bravestone
// *
// */
//public abstract class IndexedRepositoryController<R extends GenericIndexedRepository<T>, T> extends RepositoryController<GenericIndexedRepository<T>, T> {
//
//	@RequestMapping("rebuildIndex")
//	public boolean rebuildIndex() {
//		repo.updateIndex();
//		return true;
//	}
//
//	@RequestMapping("search")
//	public Collection<T> searchIndex(@RequestParam(value = "keyword") String keyword, @RequestParam(
//			value = "field", defaultValue = "title") String field) {
//		return repo.searchIndex(keyword, field);
//	}
//}
