package jp.co.sample.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.domain.Employee;
import jp.co.sample.form.SearchForm;
import jp.co.sample.form.UpdateEmployeeForm;
import jp.co.sample.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * @author fuka
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}
	
	@ModelAttribute
	public SearchForm setUpForm2() {
		return new SearchForm();
	}
	
	private static final int PAGE_SIZE = 10;
	
	/**
	 * 従業員一覧を出力する.
	 * @param model
	 * @return　従業員一覧
	 */
	@RequestMapping("/showList")
	public String showList(SearchForm form,Model model,String searchName) {
		List<Employee> AllemployeeList = employeeService.showList();
		
		//ページング機能
		//トップページは１ページ目を表示
		if(form.getPage() == null) {
			form.setPage(1);
		}
		
		//検索したページ数
		Integer count = employeeService.count(form);
		//最大ページ数を初期化
		Integer maxPage = 0;
		
		//数が10で割り切れた場合(検索された全従業員数/10が最大ページ数)
		if (count % PAGE_SIZE ==0) {
			maxPage = count / PAGE_SIZE;
		//検索された従業員が10で割り切れなかった場合は1ページ増やす
		} else {
			maxPage = count / PAGE_SIZE + 1;
		}
		
		//ページ数を最大ページまでリストに詰めていく
		List<Integer> pageNumberList = new ArrayList<>();
		for (int i = 1; i <= maxPage; i++) {
			pageNumberList.add(i);
		}
		
		//検索された従業員をリストに詰める
		List<Employee> employeeList = employeeService.search(form);
		//それぞれリクエストスコープにセット
		model.addAttribute("nowPageNumber", form.getPage());
		model.addAttribute("pageNumberList", pageNumberList);
		model.addAttribute("employeeList", employeeList);
		
		//従業員名検索があったら
		if (searchName != null ) {
			employeeList = employeeService.search(form);
		//ページングの数字からも検索できるように検索文字列をスコープに格納する.
			model.addAttribute("searchName", searchName);
		}
		if (employeeList.size() == 0) {
			employeeList = employeeService.showList();
			model.addAttribute("erroemessage", "該当する名前がありません");
		}
		
		
		
		return "employee/list";
	}
	
	
	
	
	
	/**
	 * 従業員詳細ページを表示する.
	 * @param id
	 * @param model
	 * @return 従業員詳細ページ
	 */
	@RequestMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}
	
	/**
	 * 従業員情報情報を更新する.
	 * @param form
	 * @return
	 */
	@RequestMapping("/update")
	public String update(UpdateEmployeeForm form) {
		Employee employee = new Employee();
//		employeeService.showDetail(Integer.parseInt(form.getId()));
		employee.setId(Integer.parseInt(form.getId()));
		employee.setDependentsCount(Integer.parseInt(form.getDependentsCount()));
		employeeService.update(employee);
		return "redirect:/employee/showList";
		
	}
	
	
	
}
