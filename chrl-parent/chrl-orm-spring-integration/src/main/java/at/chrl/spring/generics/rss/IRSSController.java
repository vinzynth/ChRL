/**
 * @author bravestone Feb 26, 2015 - 1:32:32 PM bravestone-dataProvider
 *         com.bravestone.diana.rss
 */
package at.chrl.spring.generics.rss;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Leopold Christian
 *
 */
public interface IRSSController<V extends GenericRSSView<T>, T> {

	public default ModelAndView getRss(Collection<T> objects) {
		ModelAndView mav = new ModelAndView(getView());
		mav.addObject(GenericRSSView.MODEL_DATA_KEY, objects);
		return mav;
	}

	/**
	 * @return the view
	 */
	@SuppressWarnings("unchecked")
	public default V getView() {
		try {
			return (V) ((Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	};

}
