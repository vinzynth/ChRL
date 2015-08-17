package at.chrl.orm.hibernate.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;

import at.chrl.nutils.Rnd;
import at.chrl.orm.hibernate.HibernateService;
import at.chrl.orm.hibernate.SessionTemplate;
import at.chrl.orm.hibernate.configuration.HibernateConfig;
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
		 * @see com.bravestone.hibernate.configuration.IHibernateConfig#getAnnotatedClasses()
		 */
		@Override
		public List<Class<?>> getAnnotatedClasses() {
			return clazzes;
		}
		
		private static class Singleton{
			private static final HibernateJPATestConfig instancte = new HibernateJPATestConfig();
		}
		
		public static HibernateJPATestConfig getInstance(){
			return Singleton.instancte;
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
	
	@Test
	public void testJPAConnection() {
		HibernateService.getInstance().connect(new HibernateJPATestConfig());
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testCreateTestEntitesHibernate() throws Exception {
		HibernateConfig conf = new HibernateJPATestConfig();
		HibernateService.getInstance().connect(conf);
		
		SessionFactory sf = HibernateService.getInstance().getSessionFactory(conf);
		
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
		HibernateService.getInstance().disconnect(conf);
	}
	
	@Test
	public void testCreateTestEntitesJPA() throws Exception {
		HibernateJPATestConfig conf = new HibernateJPATestConfig();
		HibernateService.getInstance().connect(conf);
		
		EntityManagerFactory emf = HibernateService.getInstance().getEntityManagerFactory(conf);
		
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
		HibernateService.getInstance().disconnect(conf);
	}
	
//	@Test
//	public void testCreateTestEnvers() throws Exception {
//		HibernateJPATestConfig conf = new HibernateJPATestConfig();
//		HibernateService.getInstance().connect(conf);
//		
//		EntityManagerFactory emf = HibernateService.getInstance().getEntityManagerFactory(conf);
//		
//		EntityManager entityManager = emf.createEntityManager();
////		entityManager.getTransaction().begin();
////        List<TestClass> result = entityManager.createQuery( "from TestClass", TestClass.class ).getResultList();
////		for ( TestClass test : result ) {
////			System.out.println(test);
////		}
////        entityManager.getTransaction().commit();
////        entityManager.close();
//		
//		entityManager = emf.createEntityManager();
//		entityManager.getTransaction().begin();
//		TestClass tc = entityManager.find( TestClass.class, 2L );
//		tc.setText("A follow up event (rescheduled)");
//		tc.setDate(new Date());
//		entityManager.getTransaction().commit();
//		entityManager.close();
//		
//		
//		System.out.println(tc);
//		
//		entityManager = emf.createEntityManager();
//		entityManager.getTransaction().begin();
//		entityManager.merge(tc);
//		assertEquals( "A follow up event (rescheduled)", tc.getText() );
//		AuditReader reader = AuditReaderFactory.get( entityManager );
//		
//		for (Number number : reader.getRevisions(TestClass.class, tc.getId())) {
//			System.out.println("Rev: " + number);
//			System.out.println(reader.find(TestClass.class, tc.getId(), number.intValue()));
//		}
//		
//		entityManager.getTransaction().commit();
//        entityManager.close();
//		HibernateService.getInstance().disconnect(conf);
//	}
	
	@Test
	public void testScrollAll() throws Exception {
		try (TestSession ts = new TestSession()){
			ts.getAll(TestClass.class).forEach(System.out::println);
			ts.scrollAll(TestClass.class).forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HibernateService.getInstance().disconnect(HibernateJPATestConfig.getInstance());
	}
	
	@Test
	public void testGetSession() throws Exception {
		HibernateJPATestConfig conf = new HibernateJPATestConfig();
		Session session = HibernateService.getInstance().getSession(conf);
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
		session = HibernateService.getInstance().getSession(conf);
		session.getTransaction().begin();
		
		TestClass tc = (TestClass) session.get(TestClass.class, 1L);
		tc.getTypesss().put(TestTypes.TEST_2_CLASS_1, (TestClass) session.get(TestClass.class, 2L));
		tc.getTypesss().put(TestTypes.TEST_2_CLASS_2, (TestClass) session.get(TestClass.class, 3L));
		
		session.getTransaction().commit();
		
		session.close();
		
		/**
		 * test read from object map
		 */
		session = HibernateService.getInstance().getSession(conf);
		session.getTransaction().begin();

		tc = (TestClass) session.get(TestClass.class, 1L);
		
		tc.getTypesss().values().forEach(System.out::println);
		TestClass testClass2 = (TestClass) tc.getTypesss().get(session, TestTypes.TEST_2_CLASS_1);
		System.out.println(testClass2);
		
		session.getTransaction().commit();
		
		session.close();
		
		/**
		 * Test multi map
		 */
		session = HibernateService.getInstance().getSession(conf);
		session.getTransaction().begin();
		
		tc = (TestClass) session.get(TestClass.class, 1L);
//		tc.getMaap().add(TestTypes.TEST_2_CLASS_1, (TestClass) session.get(TestClass.class, 3L));
//		tc.getMaap().add(TestTypes.TEST_2_CLASS_1, (TestClass) session.get(TestClass.class, 4L));
		
		session.getTransaction().commit();
		
		session.close();
		
		/**
		 * test read from object map
		 */
		session = HibernateService.getInstance().getSession(conf);
		session.getTransaction().begin();

		tc = (TestClass) session.get(TestClass.class, 1L);
		
//		tc.getMaap().get(session, TestTypes.TEST_2_CLASS_1).forEach(System.out::println);
		
		session.getTransaction().commit();
		
		session.close();
		HibernateService.getInstance().disconnect(conf);
	}
	
	@Test
	public void testPersistMassiveData() throws Exception {
		HibernateService.getInstance().connect(HibernateJPATestConfig.getInstance());
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
		HibernateService.getInstance().disconnect(HibernateJPATestConfig.getInstance());
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
		HibernateService.getInstance().disconnect(HibernateJPATestConfig.getInstance());
	}

	@Test
	public void testGetEntityManager() throws Exception {
		HibernateJPATestConfig conf = new HibernateJPATestConfig();
		
		EntityManager em = HibernateService.getInstance().getEntityManager(conf);
		
		em.getTransaction().begin();
		
		em.persist(new TestClass("random text", new Date(), 124235, TestEnum.FAIR));
		em.persist(new TestClass("random text2", new Date(), 12743734, TestEnum.GOOD));
		em.persist(new TestClass("random text3", new Date(), 235, TestEnum.OKAY));
		em.persist(new TestClass("random text4", new Date(), 2458, TestEnum.VERY_GOOD));
		
		em.getTransaction().commit();
		
		em.close();
		
		System.out.println(HibernateService.getInstance());
		HibernateService.getInstance().disconnect(conf);
	}
	
//	@Test
//	public void testExportSchema() throws Exception {
//		HibernateService service = HibernateService.getInstance();
//		service.exportSchema(new HibernateJPATestConfig(), "testExport");
//	}
}