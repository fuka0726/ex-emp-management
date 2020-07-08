package jp.co.sample.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class InsertAdministratorForm {

	
	// 名前
	@NotBlank(message="名前を入力してください")
	private String name;
	//メールアドレス
	@NotBlank(message="メールアドレスを入力してください")
	@Email(message="Eメールの形式が不正です")
	private String mailAddress;
	//パスワード
	@NotBlank(message="パスワードを入力してください")
	@Pattern(regexp="^.*(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
	message="8字以上・数字・大文字・小文字・記号を含めてください")
	private String password;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "InsertAdministratorForm [name=" + name + ", mailAddress=" + mailAddress + ", password=" + password
				+ "]";
	}
	
	
	
}
