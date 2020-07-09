package jp.co.sample.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.sample.domain.Administrator;

/**
 * administratorsテーブルを操作するリポジトリ.
 * @author fuka
 *
 */
@Repository
public class AdministratorRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * Administratorオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Administrator> ADMIN_ROW_MAPPER = (rs, i) -> {
		Administrator administrator = new Administrator();
		administrator.setId(rs.getInt("id"));
		administrator.setName(rs.getString("name"));
		administrator.setMailAddress(rs.getString("mail_address"));
		administrator.setPassword(rs.getString("password"));
		return administrator;
	};
	
	
	/**
	 * 管理者情報を挿入する.
	 * @param administrator　管理者情報
	 */
	public void insert(Administrator administrator) {
		String sql = "insert into administrators (name, mail_address, password) values (:name, :mailAddress, :password) ";
		SqlParameterSource param = new BeanPropertySqlParameterSource(administrator);
		template.update(sql, param);
	}
	
	
	/**
	 * メールアドレスとパスワードから管理者情報を取得する.
	 * @param mailAddress メールアドレス
	 * @param password　パスワード
	 * @return　管理者情報
	 */
	public Administrator findByMailAddressAndPassword(String mailAddress, String password) {
		String sql = "select id, name, mail_address, password from administrators where mail_address = :mailAddress and password = :password ";
		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress).addValue("password", password);
		List<Administrator> administrator = template.query(sql, param, ADMIN_ROW_MAPPER);
		if(administrator.size() == 0) {
			return null;
		}return administrator.get(0);
//		try {
//			Administrator administrator = template.queryForObject(sql, param, ADMIN_ROW_MAPPER);
//			return administrator;
//		}catch (Exception e) {
//			return null;
//		}
		
	}
	/**
	 * メールアドレスを受け取って管理者情報を返す.
	 * @param mailAddress
	 * @return 検索結果がなければnullを、あれば管理者情報を返す.
	 */
	public Administrator findByMailAddress(String mailAddress) {
		String sql = "select id, name, mail_address, password from administrators where mail_address = :mailAddress  ";
		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress);
		List<Administrator> administrator = template.query(sql,param, ADMIN_ROW_MAPPER);
		if(administrator.size() == 0) {
			return null;
		}
		return administrator.get(0);
	}
	
	
}
