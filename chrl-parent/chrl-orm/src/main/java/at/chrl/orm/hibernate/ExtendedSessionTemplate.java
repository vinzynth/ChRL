package at.chrl.orm.hibernate;

import org.hibernate.Session;
import org.hibernate.resource.transaction.spi.TransactionStatus;

public abstract class ExtendedSessionTemplate extends SessionTemplate {

	public ExtendedSessionTemplate(final Session session) {
		super(session);
	}
	
	@Override
	public void close() throws Exception {
		if(!TransactionStatus.COMMITTED.equals(session.getTransaction().getStatus()))
			session.getTransaction().commit();
	}

}
