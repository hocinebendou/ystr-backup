package renjinTest.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@NodeEntity
public class Person {
	
	@GraphId
	private Long id;
	
	private String name;
	
	public Person() {}
	
	public Person(String name) { this.name = name; }
	
	@Relationship(type="HAS_LOCUS_DYS710")
	private DYS710 locusDYS710 = new DYS710();
	
	public Long getId() { return id; }
	
	public String getName() { return name; }
	
	public void setName(String name) { this.name = name; }
	
}
