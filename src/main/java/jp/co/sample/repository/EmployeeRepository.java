package jp.co.sample.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jp.co.sample.domain.Employee;
import jp.co.sample.form.SearchForm;

/**
 * Employeeテーブルを操作するリポジトリ.
 * @author fuka
 *
 */
@Repository
public class EmployeeRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Employee> EMPLOYEE_ROW_MAPPER = (rs, i) -> {
		Employee employee = new Employee();
		employee.setId(rs.getInt("id"));
		employee.setName(rs.getString("name"));
		employee.setImage(rs.getString("image"));
		employee.setGender(rs.getString("gender"));
		employee.setHireDate(rs.getDate("hire_date"));
		employee.setMailAddress(rs.getString("mail_address"));
		employee.setZipcode(rs.getString("zip_code"));
		employee.setAddress(rs.getString("address"));
		employee.setTelephone(rs.getString("telephone"));
		employee.setSalary(rs.getInt("salary"));
		employee.setCharacteristics(rs.getString("characteristics"));
		employee.setDependentsCount(rs.getInt("dependents_count"));
		return employee;
		
	};
	
	/**
	 * 従業員一覧情報を入社日順で取得.
	 * @return 従業員一覧
	 */
	public List<Employee> findAll(){
		String sql = "select id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count "
				+ " from employees order by hire_date DESC ";
		List<Employee> employeeList = template.query(sql, EMPLOYEE_ROW_MAPPER);
		return employeeList;
	}
	
	/**
	 * 主キーから従業員情報を取得する.
	 * @param id 検索したい従業員ID
	 * @return　検索された従業員情報
	 */
	public Employee load(Integer id) {
		String sql = "select id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count "
				+ " from employees where id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Employee employee = template.queryForObject(sql, param, EMPLOYEE_ROW_MAPPER);
		return employee;
	}
	
	/**
	 * 従業員情報を変更する.
	 * @param employee
	 */
	public void update(Employee employee) {
		String sql = "update employees set dependents_count = :dependentsCount where id = :id ";
		SqlParameterSource param = new BeanPropertySqlParameterSource(employee);
		template.update(sql, param);
	}
	
	
	public StringBuilder createSql(SearchForm form,MapSqlParameterSource param,String mode) {
		StringBuilder sql = new StringBuilder();
		
		//検索ヒット件数モード
		if("count".equals(mode)) {
			sql.append("select count(*) ");
		}else {
			sql.append("select id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count ");
		}
			sql.append("from employees where 1 = 1");
			
		//名前の曖昧検索 (検索フォームから名前を受け取ったら)
		if(!StringUtils.isEmpty(form.getName())) {
			sql.append("AND name ilike :name ");
			param.addValue("name", "%" + form.getName() + "%");
		}
		
		//検索ヒット件数モードでなかったら
		if(!"count".equals(mode)) {
			Integer startNumber = calcStartNumber(form);
			if("0".equals(form.getSort())) {
				sql.append(" ORDER BY id ");
			} else if ("1".equals(form.getSort())) {
				sql.append(" ORDER BY hire_date DESC, id ");
			} else if ("2".equals(form.getSort())) {
				sql.append(" ORDER BY hire_date ASC, id ");
			} else if ("3".equals(form.getSort())) {
				sql.append(" ORDER BY salary DESC, id ");
			} else if ("4".equals(form.getSort())) {
				sql.append(" ORDER BY salary ASC, id ");
			} 
			sql.append("LIMIT 10 OFFSET " + startNumber);
		}
		
		return sql;
		
	}
	
	/**
	 * 検索にヒットした件数を取得する.
	 * @param form 名前検索フォーム
	 * @return　検索ヒット数
	 */
	public Integer count(SearchForm form) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		StringBuilder sql = createSql(form, param, "count");
		return template.queryForObject(sql.toString(), param, Integer.class);
	}
	
	/**
	 * 名前検索を行う.
	 * @param form 名前検索フォーム
	 * @return 検索結果
	 */
	public List<Employee> search(SearchForm form){
		MapSqlParameterSource param = new MapSqlParameterSource();
		StringBuilder sql = createSql(form, param, null);
		return template.query(sql.toString(), param, EMPLOYEE_ROW_MAPPER);
	}
	
	/**
	 * 現在のページでの開始番号-1を求める.
	 * @param form　名前検索フォーム
	 * @return 現在のページの開始番号-1 (OFFSET用の数字)
	 */
	private Integer calcStartNumber(SearchForm form) {
		Integer pageNumber = form.getPage();
		Integer startNumber =  10 * (pageNumber -1);
		return startNumber;
	}
	
	
	
	/**
	 * メールアドレスから従業員情報を取得します.
	 * @param mailAddress　メールアドレス
	 * @return　従業員情報　存在しない場合はnullを返す.
	 */
	public Employee findByMailAddress(String mailAddress) {
		String sql = "select id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count "
				+ " from employees where mail_address = :mailAddress ";
		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress);
		List<Employee> employeeList = template.query(sql, param, EMPLOYEE_ROW_MAPPER);
		if(employeeList.size() == 0) {
			return null;
		}
		return employeeList.get(0);
	}
	
	/**
	 * 従業員情報を登録する.
	 * 
	 * @param employee 従業員情報
	 */
	synchronized public void insert(Employee employee) {
		//IDの採番
		employee.setId(getPrimaryID());
		String sql = "INSERT INTO employees(id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count) "
				+ " VALUES(:id,:name,:image,:gender,:hireDate,:mailAddress,:zipcode,:address,:telephone,:salary,:characteristics,:dependentsCount)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(employee);
		template.update(sql, param);
	}	
	
	/**
	 * 従業員テーブルの中で一番大きいID + 1(プライマリーキー = 主キー)を取得する.
	 * 
	 * @return　テーブル内で一番値が大きいID + 1.データがない場合は1
	 */
	private Integer getPrimaryID() {
		try {
			String sql = "select MAX(id) FROM employees ";
			SqlParameterSource param = new MapSqlParameterSource();
			Integer maxID = template.queryForObject(sql, param, Integer.class);
			return maxID + 1;
		}catch (DataAccessException e) {
			//データが存在しない場合
			return 1;
		}
	}
	
	
	
	
}
