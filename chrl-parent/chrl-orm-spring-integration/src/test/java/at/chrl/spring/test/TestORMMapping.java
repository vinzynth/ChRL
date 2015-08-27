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

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.chrl.orm.hibernate.configuration.ORMMapping;
import at.chrl.spring.hibernate.config.SpringGeneratedJpaConfig;
import at.chrl.spring.hibernate.config.SpringJpaConfigInterceptor;

/**
 * @author Vinzynth
 * 27.08.2015 - 18:56:25
 *
 */
@Configuration
public class TestORMMapping{

	@Bean
	public ORMMapping getTestORMMapping(){
		return new ORMMapping(){
			
			@Override
			public List<Class<?>> getAnnotatedClasses(){
				return Collections.singletonList(TestEntity.class);
			}
		};
	}
	
	@Bean
	public SpringJpaConfigInterceptor interceptor(){
		return new SpringJpaConfigInterceptor() {
			
			@Override
			public void modify(SpringGeneratedJpaConfig config) {
				config.JDBC_DRIVER = "org.postgresql.Driver";
				config.JDBC_URL = "jdbc:postgresql://localhost:5432/test";
				config.JDBC_USER = "postgres";
				config.JDBC_PASSWORD = "postgres";
				config.SHOW_SQL = "true";
				config.GENERATE_STATISTICS = "true";
			}
		};
	}
	
}
