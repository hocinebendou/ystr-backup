package renjinTest.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@NodeEntity
public class Country {
	
	@GraphId
	private Long id;
	
	private String name;
	private String longitude;
	private String latitude;
	
	public Country() {}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getLatitude() {
		return latitude;
	}
}
