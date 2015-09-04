package at.chrl.orm.hibernate;

import org.hibernate.Session;

public abstract class ExtendedSessionTemplate extends SessionTemplate {

	public ExtendedSessionTemplate(final Session session) {
		super(session);
	}
	
	@Override
	public void close() throws Exception {
		// Do nothing...
	}

}
