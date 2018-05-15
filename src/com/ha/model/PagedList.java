package com.ha.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PagedList <T extends ModelObject> {
	private List<T> elements = new ArrayList<>();
	private int pageSize = 20;
	private int currentPage = 1;
	
	public PagedList(Collection<T> elements, int pageSize) {
		this.elements.addAll(elements);
		this.pageSize = pageSize;
	}
	
	public void setElements(ArrayList<T> elements) {
		this.elements = elements;
	}
	
	public int getNumberOfPages() {
		int size = elements.size();
		return size % pageSize == 0? size / pageSize : (size / pageSize) +1;
	}
	
	public List<T> getCurrentElements() {
		return elements.subList(((currentPage - 1) * pageSize) + 1, (currentPage * pageSize) + 1);
	}
	
	public List<T> nextPage() {
		if(currentPage == this.getNumberOfPages()) {
			return new ArrayList<T>();
		} else {
			currentPage = currentPage++;
			return this.getCurrentElements();
		}
	}
	
	public List<T> previousPage() {
		if(currentPage == 1) {
			return new ArrayList<T>();
		} else {
			currentPage = currentPage--;
			return this.getCurrentElements();
		}
	}
	
	public List<T> setCurrentPage(int page) {
		if(page < 1 || page > this.getNumberOfPages()) {
			return new ArrayList<T>();
		} else {
			currentPage = page;
			return this.getCurrentElements();
		}
	}
}
