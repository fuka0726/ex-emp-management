package jp.co.sample.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import jp.co.sample.domain.Employee;
import jp.co.sample.domain.LoginAdministrator;
import jp.co.sample.form.EmployeeInsertForm;
import jp.co.sample.form.SearchForm;
import jp.co.sample.form.UpdateEmployeeForm;
import jp.co.sample.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * @author fuka
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}
	
	@ModelAttribute
	public SearchForm setUpForm2() {
		return new SearchForm();
	}
	
	@ModelAttribute
	public EmployeeInsertForm setUpForm3() {
		return new EmployeeInsertForm();
	}
	
	
	private static final int PAGE_SIZE = 10;
	
	/**
	 * 従業員一覧を出力する.
	 * @param model
	 * @return　従業員一覧
	 */
	@RequestMapping("/showList")
	public String showList(SearchForm form,Model model,String searchName, @AuthenticationPrincipal LoginAdministrator loginAdministrator) {
		List<Employee> AllemployeeList = employeeService.showList();
		
		//ページング機能
		//トップページは１ページ目を表示
		if(form.getPage() == null) {
			form.setPage(1);
		}
		
		//検索したページ数
		Integer count = employeeService.count(form);
		//最大ページ数を初期化
		Integer maxPage = 0;
		
		//数が10で割り切れた場合(検索された全従業員数/10が最大ページ数)
		if (count % PAGE_SIZE ==0) {
			maxPage = count / PAGE_SIZE;
		//検索された従業員が10で割り切れなかった場合は1ページ増やす
		} else {
			maxPage = count / PAGE_SIZE + 1;
		}
		
		//ページ数を最大ページまでリストに詰めていく
		List<Integer> pageNumberList = new ArrayList<>();
		for (int i = 1; i <= maxPage; i++) {
			pageNumberList.add(i);
		}
		
		//検索された従業員をリストに詰める
		List<Employee> employeeList = employeeService.search(form);
		//それぞれリクエストスコープにセット
		model.addAttribute("nowPageNumber", form.getPage());
		model.addAttribute("pageNumberList", pageNumberList);
		model.addAttribute("employeeList", employeeList);
		
		//従業員名検索があったら
		if (searchName != null ) {
			employeeList = employeeService.search(form);
		//ページングの数字からも検索できるように検索文字列をスコープに格納する.
			model.addAttribute("searchName", searchName);
		}
		if (employeeList.size() == 0) {
			employeeList = employeeService.showList();
			model.addAttribute("errormessage", "該当する名前がありません");
			model.addAttribute("employeeList", employeeList);
		}
		
		//オートコンプリート用にJavaScriptの配列の中身を文字列で作ってスコープへ格納
		StringBuilder employeeListForAutocomplete = employeeService.getEmployeeListForAutocomplete(AllemployeeList);
		model.addAttribute("employeeListForAutocomplete", employeeListForAutocomplete);
		
		System.out.println(loginAdministrator.getAdministrator().getName() + "さんがログイン中");
		
		return "employee/list";
	}
	
	
	
	
	
	/**
	 * 従業員詳細ページを表示する.
	 * @param id
	 * @param model
	 * @return 従業員詳細ページ
	 */
	@RequestMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}
	
	/**
	 * 従業員情報情報を更新する.
	 * @param form
	 * @return
	 */
	@RequestMapping("/update")
	public String update(UpdateEmployeeForm form) {
		Employee employee = new Employee();
//		employeeService.showDetail(Integer.parseInt(form.getId()));
		employee.setId(Integer.parseInt(form.getId()));
		employee.setDependentsCount(Integer.parseInt(form.getDependentsCount()));
		employeeService.update(employee);
		return "redirect:/employee/showList";
		
	}
	
	@RequestMapping("/toInsert")
	public String toInsert(Model model) {
		//性別セレクトボックス
		Map<String,String> genderMap = new LinkedHashMap<>();
		genderMap.put("男性", "男性");
		genderMap.put("女性", "女性");
		model.addAttribute("genderMap", genderMap);
		
		//扶養人数セレクトボックス
		Map<Integer,Integer> dependentsCountMap = new LinkedHashMap<>();
		for(int i =0; i < 10; i++) {
			dependentsCountMap.put(i, i);
		}
		model.addAttribute("dependentsCountMap", dependentsCountMap);
		
		return "employee/insert";
	}
	
	
	@RequestMapping("/insert")
	public String insert(@Validated EmployeeInsertForm form, BindingResult result, Model model) throws IOException {
		
		//画像のファイル形式チェック
		MultipartFile imageFile = form.getImageFile();
		String fileExtention = null;
		try {
			fileExtention = getExtention(imageFile.getOriginalFilename());
			
			if (!"jpg".equals(fileExtention) && !"png".equals(fileExtention)) {
				result.rejectValue("imageFile", "", "拡張子は、.jpgか.pngのみに対応しています");
			}
		}catch (Exception e) {
			result.rejectValue("imageFile", "", "拡張子は、.jpgか.pngのみに対応しています");
		}
		
		//メールアドレス重複チェック
		Employee existEmployee = employeeService.findByMailAddress(form.getMailAddress());
		if(existEmployee !=  null) {
			result.rejectValue("mailAddress", "", "そのメールアドレスは既に登録されています");
		}
		
		
		
		if (result.hasErrors()) {
			return toInsert(model);
		}
		
		Employee employee = new Employee();
		BeanUtils.copyProperties(form, employee);
		
		
		//画像ファイルをBase64形式にエンコード
		String base64FileString = Base64.getEncoder().encodeToString(imageFile.getBytes());
		if("jpg".equals(fileExtention)) {
			base64FileString = "data:image/jpeg;base64," + base64FileString;
		} else if ("png".equals(fileExtention)) {
			base64FileString = "data:image/png;base64," + base64FileString;
		}
		employee.setImage(base64FileString);
		
		employeeService.insert(employee);
		
		return  "redirect:/employee/showList";
	}

	/**
	 * ファイル名から拡張子だけを返す.
	 * @param originFileName ファイル名
	 * @return .を除いた拡張子(jpegやpng)
	 * @throws Exception
	 */
	private String getExtention(String originFileName) throws Exception {
		//ファイルがなければ例外
		if (originFileName == null) {
			throw new FileNotFoundException();
		}
		//.がある後ろからのインデックス番号　(sample.pngなら、6)
		int point = originFileName.lastIndexOf(".");
		//見つからなかった場合(-1が返る)
		if (point == -1) {
			throw new FileNotFoundException();
		}
		//.の次の文字列以降を取得 (sample.pngなら、png)
		return originFileName.substring(point + 1);
	}
	
	
}
