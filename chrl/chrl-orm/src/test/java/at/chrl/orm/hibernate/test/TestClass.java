package at.chrl.orm.hibernate.test;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

@Entity
@Audited
@GenericGenerator(name = "incrementgen", strategy = "increment")
public class TestClass {

	@Id
	@GeneratedValue
	private Long id ;
	
	private String text;
	private Date date;
	private Integer value;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "embed1", column = @Column(name = "embedMe1_embed1")),
		@AttributeOverride(name = "embed2", column = @Column(name = "embedMe1_embed2"))
	})
	private Test2Class embedMe1;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "embed1", column = @Column(name = "embedMe2_embed1")),
		@AttributeOverride(name = "embed2", column = @Column(name = "embedMe2_embed2"))
	})
	private Test2Class embedMe2;
	
	@Enumerated(EnumType.STRING)
	private TestEnum rating;
	
	@ElementCollection
	private Set<String> additional;

	@ElementCollection
	private Set<TestEnum> additional2;

	@ElementCollection
	private Set<Test2Class> additional3;

	@ElementCollection
	private List<String> additional4;
	
	@ElementCollection
	private Map<String, String> stringMap;

	@ElementCollection
	private Map<String, Integer> intMap;
	
	@ElementCollection
	private Map<TestEnum, String> enumMap;

//	@ElementCollection
//	private Map<TestEnum, List<String>> listMap;
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private TestClass() {
		// for hibernate
	}
	
	public TestClass(String text, Date date, Integer value, TestEnum rating) {
		this.setText(text);
		this.setDate(date);
		this.setValue(value);
		this.setRating(rating);
		this.setAdditional(new TreeSet<>());
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
	
	/**
	 * {@inheritDoc}
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getId() + " | " + this.text + " | " + this.date.getTime() + " | " + this.getValue();
	}

	public Test2Class getEmbedMe1() {
		return embedMe1;
	}

	public void setEmbedMe1(Test2Class embedMe1) {
		this.embedMe1 = embedMe1;
	}

	public Test2Class getEmbedMe2() {
		return embedMe2;
	}

	public void setEmbedMe2(Test2Class embedMe2) {
		this.embedMe2 = embedMe2;
	}

	public TestEnum getRating() {
		return rating;
	}

	public void setRating(TestEnum rating) {
		this.rating = rating;
	}

	public Set<String> getAdditional() {
		return additional;
	}

	public void setAdditional(Set<String> additional) {
		this.additional = additional;
	}

	public Set<TestEnum> getAdditional2() {
		return additional2;
	}

	public void setAdditional2(Set<TestEnum> additional2) {
		this.additional2 = additional2;
	}

	public Set<Test2Class> getAdditional3() {
		return additional3;
	}

	public void setAdditional3(Set<Test2Class> additional3) {
		this.additional3 = additional3;
	}

	public List<String> getAdditional4() {
		return additional4;
	}

	public void setAdditional4(List<String> additional4) {
		this.additional4 = additional4;
	}

	public Map<String, String> getStringMap() {
		return stringMap;
	}

	public void setStringMap(Map<String, String> stringMap) {
		this.stringMap = stringMap;
	}

	public Map<TestEnum, String> getEnumMap() {
		return enumMap;
	}

	public void setEnumMap(Map<TestEnum, String> enumMap) {
		this.enumMap = enumMap;
	}

	public Map<String, Integer> getIntMap() {
		return intMap;
	}

	public void setIntMap(Map<String, Integer> intMap) {
		this.intMap = intMap;
	}
}
