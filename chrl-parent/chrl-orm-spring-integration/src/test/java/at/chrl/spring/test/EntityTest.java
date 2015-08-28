/**
 * This file is part of ChRL Util Collection.
 * 
 * ChRL Util Collection is free software: you can redistribute it and/or
 * modify  it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * ChRL Util Collection is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ChRL Util Collection.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package at.chrl.spring.test;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import at.chrl.nutils.DatasetGenerator;
import at.chrl.orm.test.ORMDatasetGenerator;
import at.chrl.spring.generics.repositories.GenericRepository;
import at.chrl.spring.hibernate.config.RepositoryHolder;

/**
 * @author Vinzynth
 * 27.08.2015 - 18:58:19
 *
 */
@ComponentScan("at.chrl.spring")
public class EntityTest {

//	@Test
	public void test() throws InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(EntityTest.class);
		RepositoryHolder bean = context.getBean(RepositoryHolder.class);
		DatasetGenerator gen = new ORMDatasetGenerator();
		
		GenericRepository<TestEntity> indexedRepository = bean.getRepository(TestEntity.class);
		
		List<TestEntity> collect = gen.generate(TestEntity.class, 200_000).collect(Collectors.toList());
		
		long n = System.nanoTime();
		indexedRepository.asyncSave(collect);
		System.out.println((System.nanoTime() - n)/(1_000*1_000) + " ms");
		
		Thread.sleep(5000);
	}

}
