/**
 * @author Christian Richard Leopold - ChRL
 * Jul 29, 2015 - 1:00:57 PM
 * chrl-orm
 * at.chrl.orm.hibernate.test
 */
package at.chrl.orm.hibernate.test;

import org.junit.Test;

import at.chrl.orm.test.ORMDatasetGenerator;

/**
 * @author Christian Richard Leopold - ChRL <br>
 * Jul 29, 2015 - 1:00:57 PM
 *
 */
public class DatasetGeneratorTest {

	@Test
	public void test() {
		new ORMDatasetGenerator().generate(TestClass.class, 5).forEach(t -> {
			System.out.println("----");
			System.out.println(t);
			
			System.out.println("-");
			t.getAdditional().forEach(System.out::println);
			System.out.println("-");
			t.getAdditional2().forEach(System.out::println);
			System.out.println("-");
			t.getAdditional3().forEach(System.out::println);
			System.out.println("-");
			t.getAdditional4().forEach(System.out::println);
			
			System.out.println("-");
			System.out.println(t.getEmbedMe1());
			System.out.println(t.getEmbedMe2());
			
			System.out.println("-");
			t.getEnumMap().entrySet().forEach(System.out::println);
			System.out.println("-");
			t.getIntMap().entrySet().forEach(System.out::println);
			System.out.println("-");
			t.getStringMap().entrySet().forEach(System.out::println);
			
		});
	}
}
