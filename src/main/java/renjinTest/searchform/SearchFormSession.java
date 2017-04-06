package renjinTest.searchform;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SearchFormSession {
	
	private String dys710;
	private String dys518;
	private String dys385a;
	private String dys385b;
	private String dys644;
	private String dys612;
	private String dys626;
	private String dys504;
	private String dys481;
	private String dys447;
	private String dys449;
	
	public void saveForm(SearchForm searchForm) {
		this.dys710 = searchForm.getDys710();
		this.dys518 = searchForm.getDys518();
		this.dys385a = searchForm.getDys385a();
		this.dys385b = searchForm.getDys385b();
		this.dys644 = searchForm.getDys644();
		this.dys612 = searchForm.getDys612();
		this.dys626 = searchForm.getDys626();
		this.dys504 = searchForm.getDys504();
		this.dys481 = searchForm.getDys481();
		this.dys447 = searchForm.getDys447();
		this.dys449 = searchForm.getDys449();
	}
	
	public SearchForm toForm() {
		SearchForm searchForm = new SearchForm();
		searchForm.setDys710(dys710);
		searchForm.setDys518(dys518);
		searchForm.setDys385a(dys385a);
		searchForm.setDys385b(dys385b);
		searchForm.setDys644(dys644);
		searchForm.setDys612(dys612);
		searchForm.setDys626(dys626);
		searchForm.setDys504(dys504);
		searchForm.setDys481(dys481);
		searchForm.setDys447(dys447);
		searchForm.setDys449(dys449);
		return searchForm;
	}
}
