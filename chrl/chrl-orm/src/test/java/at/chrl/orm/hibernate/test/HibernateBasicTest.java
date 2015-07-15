package at.chrl.orm.hibernate.test;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.PropertiesUtils;
import at.chrl.orm.hibernate.configuration.JPAConfig;

/**
 * @author bravestone
 *
 */
public class HibernateBasicTest {

	private Properties	props;

	private static class DatabaseConfig extends JPAConfig{}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		props = PropertiesUtils.filterEmtpyValues(ConfigUtil.getProperties(DatabaseConfig.class));
	}
	
	
	@Test
	public void testBuildSimpleHibernateConnection() throws Exception {
		Configuration hibernateCfg = new Configuration();
		hibernateCfg.setProperties(props);
		
		
		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(hibernateCfg.getProperties());
        hibernateCfg.buildSessionFactory(ssrb.build());
	}
	
	@Test
	public void testSessionCreation() throws Exception {
		Configuration hibernateCfg = new Configuration();
		hibernateCfg.setProperties(props);
		
		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(hibernateCfg.getProperties());
        SessionFactory sf = hibernateCfg.buildSessionFactory(ssrb.build());
        
        Session s = sf.openSession();
        
        
        s.close();
	}
	
}
