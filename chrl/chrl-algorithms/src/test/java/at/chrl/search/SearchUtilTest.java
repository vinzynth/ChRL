package at.chrl.search;

import at.chrl.algorithms.search.SearchUtil;
import at.chrl.nutils.Rnd;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertTrue;

public class SearchUtilTest {

	@Test
	public void testSearch() throws Exception {
		Set<String> index = new TreeSet<>();

		index.add("test");
		index.add("tes2");
		index.add("ttest");

		String[] result = SearchUtil.search(index, "tt");
		Arrays.stream(result).forEach(System.out::println);
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
		Arrays.stream(result).forEach(System.out::println);
	}

	@Test
	public void testPerformance() throws Exception {
		TreeSet<String> index = new TreeSet<>();

		for (int i = 0; i < 100_000; i++) {
			index.add("dsgaesauiwetgspfhga"+Rnd.nextInt(Integer.MAX_VALUE));
		}

		int scount = 100;
		long t = System.nanoTime();
		System.out.println("Start Search");
		for (int i = 0; i < scount; i++) {
			SearchUtil.search(index, Rnd.nextInt(Integer.MAX_VALUE)+"");
		}
		System.out.println("Finished: " + (System.nanoTime() - t)/1_000/scount +"µs pre search");
	}


	@Test
	public void testIndexSearch() throws Exception {
		double[] arr = new double[]{1,2,4,8,12,25,34,50};

		int size = arr.length;
		assertTrue(0 == SearchUtil.indexSearch(.5, size, arr));
		assertTrue(1 == SearchUtil.indexSearch(1.5,size,arr));
		assertTrue(4 == SearchUtil.indexSearch(9.,size,arr));
		assertTrue(7 == SearchUtil.indexSearch(35,size,arr));
		assertTrue(8 == SearchUtil.indexSearch(51,size,arr));


		arr = new double[]{-14,-12,-8,-5,0,1,2,4,8,12,25,34,50};

		size = arr.length;

		assertTrue(0 == SearchUtil.indexSearch(-15., size, arr));
		assertTrue(1 == SearchUtil.indexSearch(-13.,size,arr));
		assertTrue(4 == SearchUtil.indexSearch(0.,size,arr));
		assertTrue(7 == SearchUtil.indexSearch(3.,size,arr));
		assertTrue(10 == SearchUtil.indexSearch(12,size,arr));
		assertTrue(5 == SearchUtil.indexSearch(.5, size, arr));
		assertTrue(6 == SearchUtil.indexSearch(1.5,size,arr));
		assertTrue(9 == SearchUtil.indexSearch(9.,size,arr));
		assertTrue(size - 1 == SearchUtil.indexSearch(35,size,arr));
		assertTrue(size == SearchUtil.indexSearch(51,size,arr));
	}
}