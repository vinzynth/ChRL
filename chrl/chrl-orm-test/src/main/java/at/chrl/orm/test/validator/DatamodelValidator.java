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
package at.chrl.orm.test.validator;

import java.util.Collection;
import java.util.List;

import at.chrl.nutils.CollectionUtils;
import at.chrl.orm.hibernate.HibernateService;
import at.chrl.orm.hibernate.configuration.IHibernateConfig;
import at.chrl.orm.test.ORMDatasetGenerator;
import at.chrl.orm.test.configs.H2TestConfig;
import at.chrl.orm.test.configs.MSSQLTestConfig;
import at.chrl.orm.test.configs.MariaDBTestConfig;
import at.chrl.orm.test.configs.MySQLTestConfig;
import at.chrl.orm.test.configs.Neo4jTestConfig;
import at.chrl.orm.test.configs.PostgreSQLTestConfig;

/**
 * @author Vinzynth
 * 09.08.2015 - 17:55:15
 *
 */
public final class DatamodelValidator {
	
	private Collection<IHibernateConfig> configs;
	private List<Class<?>> classes;
	
	/**
	 * 
	 */
	public DatamodelValidator(List<Class<?>> classes) {
		this.classes = classes;
		this.configs = getTestConfigs(classes);
	}
	
	public void testConnection(){
		configs.forEach(this::testConnection);
	}
	
	public void testSessionPersist(){
		configs.forEach(this::testSessionPersist);
	}
	
	public void testPersist(){
		configs.forEach(this::testPersist);
	}
	
	
	private Collection<IHibernateConfig> getTestConfigs(List<Class<?>> annotatedClasses){
		Collection<IHibernateConfig> configs = CollectionUtils.newList();
		
		configs.add(new H2TestConfig().setAnnotatedClasses(annotatedClasses));
		configs.add(new MySQLTestConfig().setAnnotatedClasses(annotatedClasses));
		configs.add(new MariaDBTestConfig().setAnnotatedClasses(annotatedClasses));
		configs.add(new PostgreSQLTestConfig().setAnnotatedClasses(annotatedClasses));
		configs.add(new MSSQLTestConfig().setAnnotatedClasses(annotatedClasses));
//		configs.add(new FirebirdConfig().setAnnotatedClasses(annotatedClasses));
		configs.add(new Neo4jTestConfig().setAnnotatedClasses(annotatedClasses));
//		configs.add(new MongoDBConfig().setAnnotatedClasses(annotatedClasses));
		
		return configs;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Collection<?> generateTestDataset(Collection<Class<?>> classes, int size){
		Collection data = CollectionUtils.newList();
		ORMDatasetGenerator gen = new ORMDatasetGenerator();
		classes.stream().filter(c -> !c.isEnum()).forEach(c -> gen.generate(c, size).forEach(data::add));
		return data;
	}
	
	private void testConnection(final IHibernateConfig config){
		try (ValidatorSession session = new ValidatorSession(){

			@Override
			protected IHibernateConfig getHibernateConfig() {
				return config;
			}
		}){
			System.out.println("Connected: " + config);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("failed: " + e.getMessage());
		}
		
		HibernateService.getInstance().disconnect(config);
	}
	
	private void testPersist(final IHibernateConfig config){
		testSessionPersist(config);
	}
	
	private void testSessionPersist(final IHibernateConfig config){
		Collection<?> testDataset = generateTestDataset(classes, 5);
		try (ValidatorSession session = new ValidatorSession(){

			@Override
			protected IHibernateConfig getHibernateConfig() {
				return config;
			}
		}){
			for (Object object : testDataset) {
				session.saveOrUpdate(object);
			}
			session.flush();
			session.getSession().clear();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("faaaled");
		}
		
		HibernateService.getInstance().disconnect(config);
	}
}
