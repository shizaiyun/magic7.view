package org.magic7.view.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.magic7.core.service.MagicService;
import org.magic7.core.service.MagicServiceFactory;
import org.magic7.view.utils.MagicTagUtil;


public final class MagicViewTag extends TagSupport {
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -137059850317165619L;
	public static MagicService service = MagicServiceFactory.getMagicService();
	HttpSession session;
	private String space = null;
	private String view = null;
	private Map<String, Object> parmMap;

	public int doEndTag() throws JspTagException {
		try {
			String html = MagicTagUtil.getMagicRegionView(space, view,parmMap);
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
	
	public MagicViewTag() {
		this.session = null;
	}


	public Map<String, Object> getParmMap() {
		return parmMap;
	}

	public void setParmMap(Map<String, Object> parmMap) {
		this.parmMap = parmMap;
	}


}
