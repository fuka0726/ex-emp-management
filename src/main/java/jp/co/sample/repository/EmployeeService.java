package jp.co.sample.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.domain.Employee;

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
	
}
