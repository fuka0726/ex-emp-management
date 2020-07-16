package jp.co.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

@Configuration //設定用クラス
@EnableWebSecurity  //Spring Securityのweb用機能を利用
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService memberDetailsService;
	
	/**
	 * 
	 * このメソッドをオーバーライドすることで、特定のリクエストに対して
	 * 「セキュリティー設定」を無視する設定など全体に関わる設定が可能
	 * 　静的リソースに対してセキュリティの設定を無効にする
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.WebSecurity)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers( "/css/**" ,  "/img/**" , "/js/**", "/fonts/**");
	}
	
	/* 
	 * 認可の設定やログイン・ログアウトに関する設定
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//認可に関する設定
		http.authorizeRequests()
			.antMatchers("/", "/toInsert", "/insert").permitAll()  //全てのユーザーに許可
			.antMatchers("/user/**").hasRole("USER") // /user/から始まるパスはUSER権限でログインしている場合のみアクセス可
			.anyRequest().authenticated(); //それ以外のパスは認証が必要
		
		//ログインに関する設定
		http.formLogin()
			.loginPage("/")   //ログイン画面に遷移させるパス
			.loginProcessingUrl("/login") //ログインを押した際に遷移させるパス
			.failureUrl("/?error=true") //ログイン失敗に遷移させるパス
			.defaultSuccessUrl("/employee/showList",true) //ログイン成功時に遷移させるパス
			.usernameParameter("mailAddress") //認証時に使用するユーザー名のリクエストパラメーター
			.passwordParameter("password"); //認証時に使用するパスワードのリクエストパラメーター
		
		//ログアウトに関する設定
		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout**")) //ログアウトさせる際に遷移させるパス
			.logoutSuccessUrl("/") //ログアウト後に遷移させるパス
			.deleteCookies("JSESSIONID") //ログアウト後、Cookieに保存されているセッションのIDを削除
			.invalidateHttpSession(true); //true:ログアウト後、セッションを無効にする
	}
	
	/* 
	 * 「認証」に関する設定.
	 * 認証ユーザを取得する「UserDetailService」の設定
	 * パスワード照合時に使う「PasswordEncorder」の設定
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	public void configure (AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	
	
	
	/**
	 * bcryptアルゴリズムでハッシュ化する実装を返します.
	 * パスワード暗号化やマッチ確認する際に
	 * @Autowired
	 * private PasswordEncorder passwordEncorder;
	 * と記載するとDIされるようになる
	 * 
	 * @return bcryptアルゴリズムで暗号化する実装オブジェクト
	 */
	@Bean
	public  PasswordEncoder passwordEncorder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	
	
}
