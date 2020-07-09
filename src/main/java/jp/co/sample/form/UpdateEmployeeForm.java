package jp.co.sample.form;

import javax.validation.constraints.Pattern;

public class UpdateEmployeeForm {

	//従業員ID
	private String id;
	//扶養人数
	@Pattern(regexp="^[0-9]+$",message="扶養人数は数値で入力して下さい")
	private String dependentsCount;
	
	/**
	 * idを数値として返します.
	 * @return id
	 */
	public Integer getIntId() {
		return Integer.parseInt(id);
	}
	
	/**
	 * 扶養人数を数値として返します.
	 * @return 数値の扶養人数
	 */
	public Integer getIntDependentsCount() {
		return Integer.parseInt(dependentsCount);
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDependentsCount() {
		return dependentsCount;
	}
	public void setDependentsCount(String dependentsCount) {
		this.dependentsCount = dependentsCount;
	}
	@Override
	public String toString() {
		return "UpdateEmployeeForm [id=" + id + ", dependentsCount=" + dependentsCount + "]";
	}
	
	
	
}
