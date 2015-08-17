/**
 * @author bravestone Feb 26, 2015 - 3:13:51 PM bravestone-dataProvider
 *         com.bravestone.diana.rss
 */
package at.chrl.spring.generics.rss;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Item;

/**
 * 
 * @author Christian Richard Leopold - ChRL <br>
 *         Feb 26, 2015 - 4:50:15 PM
 *
 */
public class EmptyRSSView extends AbstractRssFeedView {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.web.servlet.view.feed.AbstractRssFeedView#buildFeedItems(java.util.Map,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return Collections.emptyList();
	}


	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Channel feed, HttpServletRequest request) {
		feed.setTitle("Empty Feed");
		feed.setDescription("Nothing to find here.");
		feed.setLink("");
		super.buildFeedMetadata(model, feed, request);
	}
}
