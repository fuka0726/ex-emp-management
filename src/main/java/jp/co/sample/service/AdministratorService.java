package jp.co.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.domain.Administrator;
import jp.co.sample.repository.AdministratorRepository;

/**
 * 管理者情報を登録するサービス.
 * @author fuka
 *
 */
@Service
@Transactional
public class AdministratorService {

	
	@Autowired
	private AdministratorRepository administratorRepository;
	
	
	/**
	 * 管理者情報を挿入する.
	 * @param administrator
	 */
	public void insert(Administrator administrator) {
		administratorRepository.insert(administrator);
	}
	
	
	/**
	 * ログイン処理する.
	 * @param mailAddress
	 * @param password
	 * @return
	 */
	public Administrator login(String mailAddress, String password) {
		return administratorRepository.findByMailAddressAndPassword(mailAddress, password);
	}
	
	/**
	 * メールアドレスで情報を検索します.
	 * @param mailAddress
	 * @return 管理者情報
	 */
	public Administrator findByMailAddress(String mailAddress) {
		return administratorRepository.findByMailAddress(mailAddress);
	}
	
	
}
