package org.magic7.view.utils;

import java.io.Serializable;

public class PageBean<T> implements Serializable {
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 8708089455289768927L;
	private Integer currentPage;
	private Integer totalCount;
	private Integer pageSize;
	private T data;
	
	public PageBean() {
		super();
	}

	public PageBean(Integer currentPage,Integer pageSize,Integer totalCount,T data) {
		super();
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.data = data;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
