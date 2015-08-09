package at.chrl.orm.hibernate.test;

import javax.persistence.Embeddable;

import at.chrl.orm.hibernate.datatypes.ObjectMapable;

/**
 * @author bravestone
 *
 */
@Embeddable
public class Test2Class implements ObjectMapable<Integer>{

	private String embed1;

	private String embed2;
	
	private int id;
	
	
	/**
	 * 
	 */
	public Test2Class() {}

	public Test2Class(String embed1, String embed2){
		this.embed1 = embed1;
		this.embed2 = embed2;
	}

	public String getEmbed1() {
		return embed1;
	}


	public void setEmbed1(String embed1) {
		this.embed1 = embed1;
	}


	public String getEmbed2() {
		return embed2;
	}


	public void setEmbed2(String embed2) {
		this.embed2 = embed2;
	}
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + " | " + embed1 + " | " + embed2;
	}

	/**
	 * {@inheritDoc}
	 * @see at.chrl.orm.hibernate.datatypes.ObjectMapable#getId()
	 */
	@Override
	public Integer getId() {
		return id;
	}
}
