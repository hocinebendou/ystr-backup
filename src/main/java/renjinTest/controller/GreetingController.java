package renjinTest.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
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
		String dys710Val = request.getParameter("dys710");
		String dys518Val = request.getParameter("dys518");
		String dys385aVal = request.getParameter("dys385a");
		String dys385bVal = request.getParameter("dys385b");
		String dys644Val = request.getParameter("dys644");
		String dys612Val = request.getParameter("dys612");
		String dys626Val = request.getParameter("dys626");
		String dys504Val = request.getParameter("dys504");
		String dys481Val = request.getParameter("dys481");
		String dys447Val = request.getParameter("dys447");
		String dys449Val = request.getParameter("dys449");
		int hapCount = personRep.countYPerson(dys710Val, dys518Val, dys385aVal, dys385bVal,
											  dys644Val, dys612Val, dys626Val, dys504Val,
											  dys481Val, dys447Val, dys449Val);
		int totalCount = personRep.countPersons();
		int matchPerObserved = hapCount == 0 ? totalCount : (int)totalCount/hapCount;
		int matchPerExpected = (int)(totalCount)/(hapCount + 1);
		redirectAttributes.addFlashAttribute("hc", hapCount);
		redirectAttributes.addFlashAttribute("tc", totalCount);
		redirectAttributes.addFlashAttribute("mpo", matchPerObserved);
		redirectAttributes.addFlashAttribute("mpe", matchPerExpected);
		return "redirect:/search";
	}
}
