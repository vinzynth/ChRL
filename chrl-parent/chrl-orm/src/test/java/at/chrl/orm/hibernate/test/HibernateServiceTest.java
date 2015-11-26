package at.chrl.orm.hibernate.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.chrl.nutils.Rnd;
import at.chrl.orm.hibernate.HibernateService;
import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.orm.hibernate.configuration.IHibernateConfig;
import at.chrl.orm.hibernate.configuration.templates.H2Config;
import at.chrl.orm.hibernate.datatypes.MultiMapEntry;

public class HibernateServiceTest {
	
	private static final List<Class<?>> clazzes;
	
	static{
		clazzes = new ArrayList<Class<?>>();
		clazzes.add(TestClass.class);
		clazzes.add(Test2Class.class);
		clazzes.add(TestEnum.class);
		clazzes.add(MultiMapEntry.class);
	}

	private static class HibernateJPATestConfig extends H2Config{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public List<Class<?>> getAnnotatedClasses() {
			return clazzes;
		}
		
		/**
		 * {@inheritDoc}
		 * @see at.chrl.orm.hibernate.configuration.templates.H2Config#overrideConfig()
		 */
		@Override
		public void overrideConfig() {
			super.overrideConfig();
			this.SHOW_SQL = "false";
		}
		
		private static class Singleton{
			private static final HibernateJPATestConfig instance = new HibernateJPATestConfig();
		}
		
		public static HibernateJPATestConfig getInstance(){
			return Singleton.instance;
		}
	}
	
	private static class TestSession extends SessionTemplate{

		/**
		 * {@inheritDoc}
		 * @see at.chrl.orm.hibernate.SessionTemplate#getHibernateConfig()
		 */
		@Override
		protected IHibernateConfig getHibernateConfig() {
			return HibernateJPATestConfig.getInstance();
		}
	}
	
	@Before
	public void startUp(){
		HibernateService.getInstance().connect(HibernateJPATestConfig.getInstance());
	}
	
	@After
	public void shutDown(){
		HibernateService.getInstance().disconnect(HibernateJPATestConfig.getInstance());
	}
	
	@Test
	public void testJPAConnection() {
		
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testCreateTestEntitesHibernate() throws Exception {
		
		SessionFactory sf = HibernateService.getInstance().getSessionFactory(HibernateJPATestConfig.getInstance());
		
		Session session = sf.openSession();
		
		session.getTransaction().begin();
		
		session.persist(new TestClass("random text", new Date(), 124235, TestEnum.FAIR));
		session.persist(new TestClass("random text2", new Date(), 12743734, TestEnum.GOOD));
		session.persist(new TestClass("random text3", new Date(), 235, TestEnum.OKAY));
		session.persist(new TestClass("random text4", new Date(), 2458, TestEnum.VERY_GOOD));
		
		session.getTransaction().commit();
		
		session.close();

		Session esession = sf.openSession();
		esession.getTransaction().begin();
        List result = esession.createQuery( "from TestClass" ).list();
		for ( TestClass tc : (List<TestClass>) result ) {
			System.out.println(tc);
		}
        esession.getTransaction().commit();
        esession.close();
	}
	
	@Test
	public void testCreateTestEntitesJPA() throws Exception {
		EntityManagerFactory emf = HibernateService.getInstance().getEntityManagerFactory(HibernateJPATestConfig.getInstance());
		
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		em.persist(new TestClass("random text", new Date(), 124235, TestEnum.FAIR));
		em.persist(new TestClass("random text2", new Date(), 12743734, TestEnum.GOOD));
		em.persist(new TestClass("random text3", new Date(), 235, TestEnum.OKAY));
		em.persist(new TestClass("random text4", new Date(), 2458, TestEnum.VERY_GOOD));
		
		em.getTransaction().commit();
		
		em.close();
		
		

		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
        List<TestClass> result = entityManager.createQuery( "from TestClass", TestClass.class ).getResultList();
		for ( TestClass test : result ) {
			System.out.println(test);
		}
        entityManager.getTransaction().commit();
        entityManager.close();
	}
	
	@Test
	public void testScrollAll() throws Exception {
		try (TestSession ts = new TestSession()){
			ts.getAll(TestClass.class).forEach(System.out::println);
			ts.scrollAll(TestClass.class).forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetSession() throws Exception {
		Session session = HibernateService.getInstance().getSession(HibernateJPATestConfig.getInstance());
		session.getTransaction().begin();
		
		session.persist(new TestClass("random text", new Date(), 124235, TestEnum.FAIR));
		session.persist(new TestClass("random text2", new Date(), 12743734, TestEnum.GOOD));
		session.persist(new TestClass("random text3", new Date(), 235, TestEnum.OKAY));
		session.persist(new TestClass("random text4", new Date(), 2458, TestEnum.VERY_GOOD));
		
		session.flush();

		session.getTransaction().commit();
		session.close();
		
		
		/**
		 * Test object map
		 */
		session = HibernateService.getInstance().getSession(HibernateJPATestConfig.getInstance());
		session.getTransaction().begin();
		
		TestClass tc = session.get(TestClass.class, 1L);
		tc.getTypesss().put(TestTypes.TEST_2_CLASS_1, session.get(TestClass.class, 2L));
		tc.getTypesss().put(TestTypes.TEST_2_CLASS_2, session.get(TestClass.class, 3L));
		
		session.getTransaction().commit();
		
		session.close();
		
		/**
		 * test read from object map
		 */
		session = HibernateService.getInstance().getSession(HibernateJPATestConfig.getInstance());
		session.getTransaction().begin();

		tc = session.get(TestClass.class, 1L);
		
		tc.getTypesss().values().forEach(System.out::println);
		TestClass testClass2 = tc.getTypesss().get(session, TestTypes.TEST_2_CLASS_1);
		System.out.println(testClass2);
		
		session.getTransaction().commit();
		
		session.close();
		
		/**
		 * Test multi map
		 */
		session = HibernateService.getInstance().getSession(HibernateJPATestConfig.getInstance());
		session.getTransaction().begin();
		
		tc = session.get(TestClass.class, 1L);
//		tc.getMaap().add(TestTypes.TEST_2_CLASS_1, (TestClass) session.get(TestClass.class, 3L));
//		tc.getMaap().add(TestTypes.TEST_2_CLASS_1, (TestClass) session.get(TestClass.class, 4L));
		
		session.getTransaction().commit();
		
		session.close();
		
		/**
		 * test read from object map
		 */
		session = HibernateService.getInstance().getSession(HibernateJPATestConfig.getInstance());
		session.getTransaction().begin();

		tc = session.get(TestClass.class, 1L);
		
//		tc.getMaap().get(session, TestTypes.TEST_2_CLASS_1).forEach(System.out::println);
		
		session.getTransaction().commit();
		
		session.close();
	}
	
	@Test
	public void testPersistMassiveData() throws Exception {
		try (TestSession session = new TestSession()){
			int count = 20_000;
			List<TestClass> t = new ArrayList<TestClass>(count);
			for (int i = 0; i < count; i++) {
				if(i % (count >> 3) == 0)
					System.out.println(i);
				TestClass testClass = new TestClass("rand:" + Rnd.nextInt(), new Date(Rnd.nextInt()), Rnd.nextInt(), TestEnum.values()[i%TestEnum.values().length]);
				t.add(testClass);
			}
			System.out.println("persist");
			HibernateService.persistList(t, HibernateJPATestConfig.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBatchFetch() throws Exception {
		try (TestSession s = new TestSession()){
//			Query q = s.createQuery("SELECT c FROM TestClass c where c.value >= 0");
			
//			long su = s.<TestClass>streamReadOnly(q)
			long su = s.<TestClass>streamStateless("SELECT c FROM TestClass c where c.value >= 0")
			.mapToLong(TestClass::getValue).sum();
			System.out.println(su);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetEntityManager() throws Exception {
		EntityManager em = HibernateService.getInstance().getEntityManager(HibernateJPATestConfig.getInstance());
		
		em.getTransaction().begin();
		
		em.persist(new TestClass("random text", new Date(), 124235, TestEnum.FAIR));
		em.persist(new TestClass("random text2", new Date(), 12743734, TestEnum.GOOD));
		em.persist(new TestClass("random text3", new Date(), 235, TestEnum.OKAY));
		em.persist(new TestClass("random text4", new Date(), 2458, TestEnum.VERY_GOOD));
		
		em.getTransaction().commit();
		
		em.close();
		
		System.out.println(HibernateService.getInstance());
	}
	
}
