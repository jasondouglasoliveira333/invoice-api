package br.com.lkm.nfse.notafiscal.view;

import java.util.List;

public class PageResponse<T> {
	private Integer totalPages;
	private List<T> content;

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

}
