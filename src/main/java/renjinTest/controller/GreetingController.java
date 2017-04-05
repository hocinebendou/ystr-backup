package renjinTest.controller;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import renjinTest.repositories.PersonRepository;
import renjinTest.searchform.SearchForm;


@Controller
public class GreetingController {
	@RequestMapping("/greeting")
	public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		
		try {
			RConnection c = new RConnection();
			c.eval("source('/home/hocine/script.R')");
			c.eval("source('/home/hocine/script2.R')");
			
			RList pop = c.eval("pop.sim()").asList();
			// System.out.println(pop.get(0).getClass().getName());
			REXPDouble v = (REXPDouble) pop.get(3);
			double[] d = (double[]) v.asDoubles();
			System.out.println(d);
			
			REXPGenericVector pRaw = (REXPGenericVector)c.eval("pop.sim()");
			c.assign("pop", pRaw);
			c.eval("pop$PopFreq <- pop$N/sum(pop$N)");
			RList freqs = c.eval("sim.sample()").asList();
			double [] predicted = (double[]) ((REXPDouble)freqs.get(0)).asDoubles();
			double [] observed = (double[]) ((REXPDouble)freqs.get(1)).asDoubles();
			//double[] y = (double[]) c.eval("fit$y").asDoubles();
			System.out.println(predicted[0]);
			System.out.println(observed[0]);
		} catch (RserveException e) {
			e.printStackTrace();
		} catch(REXPMismatchException re) {
			re.printStackTrace();
		}
		return "greeting";
	}
	
	@Autowired PersonRepository personRep;
	
	@Autowired Session template;
	
	@RequestMapping("/saluer")
	public String saluer(@RequestParam(value="name", required=false, defaultValue="Hocine") String name, Model model) {
		model.addAttribute("name", name);
		
		try {
			/*RConnection c = new RConnection();
			c.eval("source('/home/hocine/Rserve/script3.R')");
			c.eval("mikl()");*/
			
			String dys710Val = "32";
			String dys518Val = "35";
			String dys385aVal = "11";
			String dys385bVal = "11";
			String dys644Val = "23";
			String dys612Val = "32";
			String dys626Val = "31";
			String dys504Val = "31";
			String dys481Val = "31";
			String dys447Val = "31";
			String dys449Val = "31";
			int ct = personRep.countYPerson(dys710Val, dys518Val, dys385aVal, dys385bVal,
											dys644Val, dys612Val, dys626Val, dys504Val,
											dys481Val, dys447Val, dys449Val);
			System.out.println(ct);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "greeting";
	}
	
	@RequestMapping(value = "/search")
	public String search(SearchForm searchForm) {
		return "greeting";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String searchProfile(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		
		final Map<String, String> parameterValues = new LinkedHashMap<>();
		
		parameterValues.put("DYS710", request.getParameter("dys710"));
		parameterValues.put("DYS518", request.getParameter("dys518"));
		parameterValues.put("DYS385a", request.getParameter("dys385a"));
		parameterValues.put("DYS385b", request.getParameter("dys385b"));
		parameterValues.put("DYS644", request.getParameter("dys644"));
		parameterValues.put("DYS612", request.getParameter("dys612"));
		parameterValues.put("DYS626", request.getParameter("dys626"));
		parameterValues.put("DYS504", request.getParameter("dys504"));
		parameterValues.put("DYS481", request.getParameter("dys481"));
		parameterValues.put("DYS447", request.getParameter("dys447"));
		parameterValues.put("DYS449", request.getParameter("dys449"));
		
		String query = constructQuery(parameterValues);		
		final Map<String, String> paramsQuery = removeNullParams(parameterValues);
		int hapCount = runNeoQuery(query, paramsQuery);

		int totalCount = personRep.countPersons();
		int matchPerObserved = hapCount == 0 ? totalCount : (int)totalCount/hapCount;
		int matchPerExpected = (int)(totalCount)/(hapCount + 1);
		redirectAttributes.addFlashAttribute("hc", hapCount);
		redirectAttributes.addFlashAttribute("tc", totalCount);
		redirectAttributes.addFlashAttribute("mpo", matchPerObserved);
		redirectAttributes.addFlashAttribute("mpe", matchPerExpected);
		return "redirect:/search";
	}
	
	/**
	 * Remove YSTR loci with empty string and return a new map collection of only 
	 * YSTR with non empty value.
	 */
	private LinkedHashMap<String, String> removeNullParams(Map<String, String> params) {
		LinkedHashMap<String, String> paramsQuery = new LinkedHashMap<>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if(entry.getValue() != "") {
				String key = entry.getKey().substring(0, 3).toLowerCase() + 
							 entry.getKey().substring(3) + "Val";
				paramsQuery.put(key, entry.getValue());
			}
		}
		return paramsQuery;
	}
	
	/**
	 * Construct query from @parameterValues and use it as entry
	 * to query Neo4j.
	 */
	private String constructQuery(Map<String, String> parameterValues) {
		String query = "";
		String queryFirstPart = "MATCH (p:Person) ";
		String querySecondPart = "";
		int i = 1;
		for (Map.Entry<String, String> entry : parameterValues.entrySet()) {
			if(entry.getValue() != "") {
				queryFirstPart += "MATCH (y" + i + ":" + entry.getKey() + " " +
						          "{val: {" + entry.getKey().substring(0, 3).toLowerCase() + 
						          entry.getKey().substring(3) +"Val}}) ";
				querySecondPart += (i == 1) ? "WHERE" : "AND";
				querySecondPart += " (p)-[:HAS_LOCUS_" + entry.getKey() + "]->(y"+ i + ") ";
				i++;
			}
		}
		query += queryFirstPart + querySecondPart + "RETURN count(p)";
		System.out.println(query);
		return query;
	}
	
	/**
	 * Run dynamic Neo4j query. If the interface "PersonRepository.java" is used
	 * the query should be static and final.  
	 */
	private int runNeoQuery(String query, Map<String, String> paramsQuery) {
		Result result = template.query(query, paramsQuery);
		int countPersons = ((Long)result.iterator().next().get("count(p)")).intValue();
		
		return countPersons;
	}
}
