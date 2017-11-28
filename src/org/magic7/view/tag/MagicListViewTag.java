package org.magic7.view.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.magic7.core.domain.MagicRegionRow;
import org.magic7.view.utils.MagicTagUtil;

public final class MagicListViewTag extends TagSupport {
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -137059850317165619L;
	HttpSession session;
	private String space = null;
	private String view = null;
	private List<MagicRegionRow> rows = null;
	private Integer destination = 0;
	
	public int doEndTag() throws JspTagException {
		try {
			String html = MagicTagUtil.getMagicListView(space,view,rows,destination);
			pageContext.getOut().write(html); 
		} catch (IOException e) {
			throw new JspTagException("MagicViewTag: " + e.getMessage());
		}
		return EVAL_PAGE;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}
	
	public MagicListViewTag() {
		this.session = null;
	}

	public Integer getDestination() {
		return destination;
	}

	public void setDestination(Integer destination) {
		this.destination = destination;
	}

	public List<MagicRegionRow> getRows() {
		return rows;
	}

	public void setRows(List<MagicRegionRow> rows) {
		this.rows = rows;
	}


}
