package jp.co.sample.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.domain.Administrator;
import jp.co.sample.form.InsertAdministratorForm;
import jp.co.sample.form.LoginForm;
import jp.co.sample.service.AdministratorService;

/**
 * 管理者情報を操作するコントローラー.
 * @author fuka
 *
 */
@Controller
@RequestMapping("/")
public class AdministratorController {

	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private HttpSession session;
	
	@ModelAttribute
	public InsertAdministratorForm setUpForm() {
		return new InsertAdministratorForm();
	}
	
	@ModelAttribute
	public LoginForm setUpForm2() {
		return new LoginForm();
	}
	
	
	
	/**
	 * 管理者登録画面の表示.
	 * @return　管理者登録画面
	 */
	@RequestMapping("/toInsert")
	public String toInsert() {
		return "administrator/insert";
	}
	
	
	/**
	 * 管理者登録をする.
	 * @param form
	 * @return  ログイン画面
	 */
	@RequestMapping("/insert")
	public String insert(@Validated InsertAdministratorForm form,BindingResult result) {
		
		if(administratorService.findByMailAddress(form.getMailAddress())!= null) {
			result.rejectValue("mailAddress", "", "そのメールアドレスは既に登録されています");
		}
		
		if(result.hasErrors()) {
			return toInsert();
		}
		
		Administrator administrator = new Administrator();
		BeanUtils.copyProperties(form, administrator);
		administratorService.insert(administrator);
		return "redirect:/";
	}
	
	
	/**
	 * ログイン画面の表示
	 * @return　ログイン画面
	 */
	@RequestMapping("/")
	public String toLogin() {
		return "administrator/login";
	}
	
	/**
	 * ログインする.
	 * @param form 管理者情報用フォーム
	 * @param result　エラー格納用オブジェクト
	 * @param model　エラー格納用オブジェクト
	 * @return　ログイン後の従業員一覧画面
	 */
	@RequestMapping("/login")
	public String login(@Validated LoginForm form,BindingResult result,Model model) {
		Administrator administrator = administratorService.login(form.getMailAddress(),form.getPassword());
		
		if(result.hasErrors()) {
			return toLogin();
		}
		
		if(administrator == null) {
//			result.addError(new ObjectError("", "メールアドレスまたはパスワードが不正です"));
		model.addAttribute("error", "メールアドレスまたはパスワードが不正です");
			return toLogin();
		}
		session.setAttribute("administratorName", administrator.getName());
		return "forward:/employee/showList";
	}
	
	/**
	 * ログアウトする.
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/";
//		return toLogin();
	}
	
	
	
	
}
