/**
 * @author bravestone Feb 26, 2015 - 1:01:48 PM bravestone-dataProvider
 *         com.bravestone.diana.rss
 */
package at.chrl.spring.generics.rss;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.sun.syndication.feed.rss.Item;

/**
 * @author Leopold Christian
 *
 */
@Component
public abstract class GenericRSSView<T> extends AbstractRssFeedView {

	public static String MODEL_DATA_KEY = "model.data.key";

	protected GenericRSSView() {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.web.servlet.view.feed.AbstractRssFeedView#buildFeedItems(java.util.Map,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.convertAll((Collection<T>) model.getOrDefault(MODEL_DATA_KEY, Collections.emptyList()));
	}

	public abstract Item convert(T toConvert);

	public final List<Item> convertAll(Collection<T> toConvert) {
		return toConvert.stream().filter(Objects::nonNull).map(t -> this.convertFailSafe(t)).filter(Objects::nonNull).collect(Collectors.toList());
	}

	private final Item convertFailSafe(T toConvert) {
		try {
			return convert(toConvert);
		} catch (Exception e) {
			System.err.println("Error during RSS Item conversion: " + e.getMessage());
		}
		return null;
	}
}
