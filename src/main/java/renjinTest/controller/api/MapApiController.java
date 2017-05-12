package renjinTest.controller.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import renjinTest.domain.Country;
import renjinTest.repositories.PersonRepository;

@RestController
@RequestMapping("/api/map")
public class MapApiController {

	@Autowired 
	PersonRepository personRepository;
	
	@Autowired
	Session session;
	
	@RequestMapping(value = "/{searchType}", method = RequestMethod.GET)
	public List<Map<String, Double>> mapCountHaplotypes(@PathVariable String searchType, @MatrixVariable Map<String, String> keywords) {
		
		String query = constructQuery(keywords);
		
		Map<String, String> paramsQuery = new HashMap<>();
		Collection<Country> countries = runNeoQuery(query, paramsQuery);
		List<Map<String, Double>> countHaplotypes = new ArrayList<>();
		
		for (Country country : countries) {
			Map<String, Double> coordinates = new LinkedHashMap<String, Double>();
			coordinates.put("lat", Double.parseDouble(country.getLatitude()));
			coordinates.put("lng", Double.parseDouble(country.getLongitude()));
			countHaplotypes.add(coordinates);
		}
		
		return countHaplotypes;
	}
	
	/**
	 * Construct query from @parameterValues and use it as entry
	 * to query Neo4j.
	 */
	private String constructQuery(Map<String, String> keywords) {
		String query = "";
		String queryFirstPart = "MATCH (p:Person) ";
		String querySecondPart = "";
		int i = 1;
		for (Map.Entry<String, String> entry : keywords.entrySet()) {
			if(entry.getValue() != "") {
				queryFirstPart += "MATCH (y" + i + ":" + entry.getKey().substring(0, 3).toUpperCase() + 
						          entry.getKey().substring(3) + " " + "{val: '" + entry.getValue() + "'}) ";
				querySecondPart += (i == 1) ? "WHERE " : "AND ";
				querySecondPart += "(p)-[:HAS_LOCUS_" + entry.getKey().substring(0, 3).toUpperCase() + 
						           entry.getKey().substring(3) + "]->(y"+ i + ") ";
				i++;
			}
		}
		query += queryFirstPart + querySecondPart + "WITH p ";
		query += "MATCH (p) -[:FROM_COUNTRY]->(c:Country) ";
		query += "RETURN c";
		return query;
	}
	
	/**
	 * Run dynamic Neo4j query. If the interface "PersonRepository.java" is used
	 * the query should be static and final.  
	 */
	private Collection<Country> runNeoQuery(String query, Map<String, String> paramsQuery) {
		Result result = session.query(query, paramsQuery);
		Collection<Country> countries = new ArrayList<>();
		Iterator<Map<String, Object>> iterator = result.iterator();
		while (iterator.hasNext()) {
			Map<String, Object> country = iterator.next();
			for (Map.Entry<String, Object> entry : country.entrySet()) {
				countries.add((Country)entry.getValue());
			}
		}
		
		return countries;
	}
}
