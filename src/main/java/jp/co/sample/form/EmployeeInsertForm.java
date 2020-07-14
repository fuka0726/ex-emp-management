package jp.co.sample.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;

public class EmployeeInsertForm {
	
	/** id */
	private Integer id;
	
	/** 従業員名 */
	@NotBlank(message = "従業員名を入力して下さい")
	private String name;
	
	/** 画像 */	
	private MultipartFile imageFile;
	
	/** 性別 */
	@NotBlank(message = "性別を入力してください")
	private String gender;
	
	/** 入社日 */
	@Pattern(message = "入社日の形式が不正です", regexp = "[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]")
	private String hireDateString;
	
	/** メールアドレス */
	@Email(message="メールアドレスの形式が不正です")
	@NotBlank(message="メールアドレスを入力してください")
	private String mailAddress;
	
	/** 郵便番号 */
	@NotBlank(message ="郵便番号を入力してください")
	private String zipcode;
	
	/** 住所 */
	@NotBlank(message="住所を入力してください")
	private String address;
	
	/** 電話番号 */
	@NotBlank(message="電話番号を入力してください")
	private String telephone;
	
	/** 給料 */
	@NotBlank(message="給料を入力してください")
	private String salaryString;
	
	/** 特性 */
	@NotBlank(message="特性を入力してください")
	private String characteristics;
	
	/** 扶養人数 */
	@NotBlank(message="不要人数を入力してください")
	private String dependentsCountString;
	
	/**
	 * 入社年月日をDate型で返す.
	 * @return　入社年月日
	 * @throws ParseException
	 */
	public Date getHireDate() throws ParseException {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		Date formatDate = date.parse(hireDateString);
		return formatDate;
	}
	
	/**
	 * 給料をInteger型で返す.
	 * @return　給料
	 */
	public Integer getSalary() {
		return Integer.parseInt(salaryString);
	}
	
	
	/**
	 * 扶養人数をInteger型で返す.
	 * @return
	 */
	public Integer getDependentsCount () {
		return Integer.parseInt(dependentsCountString);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHireDateString() {
		return hireDateString;
	}

	public void setHireDateString(String hireDateString) {
		this.hireDateString = hireDateString;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getSalaryString() {
		return salaryString;
	}

	public void setSalaryString(String salaryString) {
		this.salaryString = salaryString;
	}

	public String getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(String characteristics) {
		this.characteristics = characteristics;
	}

	public String getDependentsCountString() {
		return dependentsCountString;
	}

	public void setDependentsCountString(String dependentsCountString) {
		this.dependentsCountString = dependentsCountString;
	}

	@Override
	public String toString() {
		return "EmployeeInsertForm [id=" + id + ", name=" + name + ", imageFile=" + imageFile + ", gender=" + gender
				+ ", hireDateString=" + hireDateString + ", mailAddress=" + mailAddress + ", zipcode=" + zipcode
				+ ", address=" + address + ", telephone=" + telephone + ", salaryString=" + salaryString
				+ ", characteristics=" + characteristics + ", dependentsCountString=" + dependentsCountString + "]";
	}
	
	

	
	
}
