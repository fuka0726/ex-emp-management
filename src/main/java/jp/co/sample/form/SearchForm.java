package jp.co.sample.form;

public class SearchForm {

	//ページング用
	private Integer page;
	//名前検索
	private String name;
	//並び替え
	private String sort;
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	@Override
	public String toString() {
		return "SearchForm [page=" + page + ", name=" + name + ", sort=" + sort + "]";
	}
	
	
	
}
