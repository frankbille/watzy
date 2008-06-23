package org.apache.wicket.examples.yatzy.frontend.persistence;

import java.util.List;

import org.apache.wicket.examples.yatzy.frontend.Highscore;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Order;

public class HibernatePersistence {
	
	private static SessionFactory sessionFactory;
	
	private static SessionFactory createSessionFactory() {
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		
		cfg.addAnnotatedClass(Highscore.class);
		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		cfg.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
		cfg.setProperty("hibernate.connection.url", "jdbc:hsqldb:file:data/watzy");
		cfg.setProperty("hibernate.connection.username", "sa");
		cfg.setProperty("hibernate.connection.password", "");
		cfg.setProperty("hibernate.current_session_context_class", "thread");
		cfg.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
		cfg.setProperty("hibernate.hbm2ddl.auto", "update");
		
		return cfg.buildSessionFactory();
	}
	
	private synchronized static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = createSessionFactory();
			
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			session.createSQLQuery("SET WRITE_DELAY 0 MILLIS").executeUpdate();
			session.getTransaction().commit();
			session.close();
		}
		
		return sessionFactory;
	}
	
	public static void saveHighscore(Highscore highscore) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		session.save(highscore);
		session.getTransaction().commit();
		session.close();
	}
	
	@SuppressWarnings("unchecked")
	public static List<Highscore> getHighscores() {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		Criteria c = session.createCriteria(Highscore.class);
		c.addOrder(Order.desc("score"));
		c.addOrder(Order.desc("timestamp"));
		List<Highscore> list = c.list();
		session.getTransaction().commit();
		session.close();
		
		return list;
	}
	
}
