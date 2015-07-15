package at.chrl.orm.hibernate.test;

import javax.persistence.Embeddable;

/**
 * @author bravestone
 *
 */
@Embeddable
public class Test2Class {

	private String embed1;

	private String embed2;
	
	
	/**
	 * 
	 */
	public Test2Class() {}


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
}
