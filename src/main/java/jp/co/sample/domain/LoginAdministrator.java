package jp.co.sample.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * 
 * 管理者のログイン情報を格納するエンティティ.
 * @author fuka
 *
 */
public class LoginAdministrator extends User{
	
	
	private static final long serialVersionUID = 1L;
	
	//管理者情報
	private final Administrator administrator;
	
	

	/**
	 * 通常の管理者情報に加え、認可用ロールを設定する.
	 * 
	 * @param administrator 管理者情報
	 * @param authorityList　権限情報が入ったリスト
	 */
	public LoginAdministrator(Administrator administrator, Collection<GrantedAuthority> authorityList) {
		super(administrator.getMailAddress(), administrator.getPassword() , authorityList);
		this.administrator = administrator;
	}

	
	public Administrator getAdministrator() {
		return administrator;
	}
	
	
	
	
}
