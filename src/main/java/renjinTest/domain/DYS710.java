package renjinTest.domain;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@NodeEntity
public class DYS710 {

	@GraphId
	private Long id;
	
	private int val;
	
	public DYS710() {}
	
	public DYS710(int val) { this.val = val; }
	
	@Relationship(type = "HAS_LOCUS_DYS710", direction = Relationship.INCOMING)
	private List<Person> persons = new ArrayList<>();
	
	public Long getId() { return id; }
	
	public int getVal() { return val; }
	
	public void setVal( int val ) { this.val = val; }
}
