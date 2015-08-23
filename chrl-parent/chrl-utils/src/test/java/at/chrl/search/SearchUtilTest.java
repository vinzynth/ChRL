package at.chrl.search;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import at.chrl.algorithms.search.SearchUtil;
import at.chrl.nutils.Rnd;

public class SearchUtilTest {

	@Test
	public void testSearch() throws Exception {
		Set<String> index = new TreeSet<>();

		index.add("test");
		index.add("tes2");
		index.add("ttest");

		String[] result = SearchUtil.search(index, "tt");
		for (int i = 0; i < result.length; i++) {
			if(result[i].equals("ttest"))
				return;
		}
		fail("Result not found in returned set");
	}

	@Test
	public void testSearch2() throws Exception {
		Set<String> index = new TreeSet<>();

		index.add("Toggle Comment");
		index.add("Toggle Block Comment");
		index.add("Set Syntax: Tcl");
		index.add("Set Syntax: HTML(Tcl)");
		index.add("File: New File Relative to Current View");
		index.add("Set Syntax: C");
		index.add("Import a really long string!");
		index.add("Import a really long string!1");
		index.add("Import a really long string!2");
		index.add("Import a really long string!3");
		index.add("Import a really long tring!setc");
		index.add("Import a really long string!4");
		index.add("Import a really long string!5");
		index.add("s");

		String[] result = SearchUtil.search(index, "setc");
		for (int i = 0; i < result.length; i++) {
			if(result[i].equals("Set Syntax: C"))
				return;
		}
		fail("Result not found in returned set");
	}

	@Test
	public void testPerformance() throws Exception {
		TreeSet<String> index = new TreeSet<>();

		for (int i = 0; i < 10_000; i++) {
			index.add(Rnd.nextString() + Rnd.nextInt(Integer.MAX_VALUE));
		}

		int scount = 100;
		for (int i = 0; i < scount; i++) {
			String[] search = SearchUtil.search(index, Rnd.nextInt(Integer.MAX_VALUE) + "");
			assertTrue(Arrays.stream(search).filter(index::contains).count() == search.length);
		}
	}

	@Test
	public void testIndexSearch() throws Exception {
		double[] arr = new double[] { 1, 2, 4, 8, 12, 25, 34, 50 };

		int size = arr.length;
		assertTrue(0 == SearchUtil.indexSearch(.5, size, arr));
		assertTrue(1 == SearchUtil.indexSearch(1.5, size, arr));
		assertTrue(4 == SearchUtil.indexSearch(9., size, arr));
		assertTrue(7 == SearchUtil.indexSearch(35, size, arr));
		assertTrue(8 == SearchUtil.indexSearch(51, size, arr));

		arr = new double[] { -14, -12, -8, -5, 0, 1, 2, 4, 8, 12, 25, 34, 50 };

		size = arr.length;

		assertTrue(0 == SearchUtil.indexSearch(-15., size, arr));
		assertTrue(1 == SearchUtil.indexSearch(-13., size, arr));
		assertTrue(4 == SearchUtil.indexSearch(0., size, arr));
		assertTrue(7 == SearchUtil.indexSearch(3., size, arr));
		assertTrue(10 == SearchUtil.indexSearch(12, size, arr));
		assertTrue(5 == SearchUtil.indexSearch(.5, size, arr));
		assertTrue(6 == SearchUtil.indexSearch(1.5, size, arr));
		assertTrue(9 == SearchUtil.indexSearch(9., size, arr));
		assertTrue(size - 1 == SearchUtil.indexSearch(35, size, arr));
		assertTrue(size == SearchUtil.indexSearch(51, size, arr));
	}
}