package jp.co.sample.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.domain.Employee;
import jp.co.sample.form.SearchForm;
import jp.co.sample.repository.EmployeeRepository;

/**
 * 従業員情報を操作するサービス.
 * @author fuka
 *
 */
@Service
@Transactional
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	/**
	 * 従業員情報を全件取得する.
	 * @return
	 */
	public List<Employee> showList(){
		return employeeRepository.findAll();
	}
	
	/**
	 * 従業員情報を取得する.
	 * @param id
	 * @return
	 */
	public Employee showDetail(Integer id) {
		return employeeRepository.load(id);
	}
	
	
	/**
	 * 従業員情報を更新する.
	 * @param employee
	 */
	public void update(Employee employee) {
		employeeRepository.update(employee);
	}
	
	/**
	 * 従業員検索を行う
	 * @param form
	 * @return　検索結果
	 */
	public List<Employee> search(SearchForm form){
		List<Employee> employeeList = employeeRepository.search(form);
		return employeeList;
	}
	
	/**
	 * 検索にヒットした件数を取得する.
	 * @param form
	 * @return
	 */
	public Integer count (SearchForm form) {
		Integer employee = employeeRepository.count(form);
		return employee;
	}
	
	/**
	 * 従業員情報を登録します.
	 * @param employee
	 */
	public void insert(Employee employee) {
		employeeRepository.insert(employee);
	}
	
	/**
	 * メールアドレスから従業員情報を取得する.
	 * @param mailAddress メールアドレス
	 * @return 従業員情報　ない場合はnullを返す
	 */
	public Employee findByMailAddress(String mailAddress) {
		return employeeRepository.findByMailAddress(mailAddress);
	}
	
	
	/**
	 * オートコンプリート用にJavaScriptの配列の中身を文字列で作る.
	 * @param employeeList
	 * @return
	 */
	public StringBuilder getEmployeeListForAutocomplete (List<Employee> employeeList) {
		StringBuilder employeeListForAutocomplete = new StringBuilder();
		 //従業員数だけ繰り返す
		for (int i = 0; i < employeeList.size(); i++) {
		//もし０じゃなければ「,」で区切る
			if(i != 0) {
				employeeListForAutocomplete.append(",");
			}
			//くりかえした数(従業員数)を取得
			Employee employee = employeeList.get(i);
			 // 「"」「従業員名」「"」
			employeeListForAutocomplete.append("\""); 
			employeeListForAutocomplete.append(employee.getName());
			employeeListForAutocomplete.append("\"");
		}
		return employeeListForAutocomplete;
	}
	
	
	
	
}
