package org.magic7.view.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.magic7.core.service.MagicService;
import org.magic7.core.service.MagicServiceFactory;
import org.magic7.view.utils.MagicTagUtil;


public final class MagicDetailTag extends TagSupport {
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -137059850317165619L;
	public static MagicService service = MagicServiceFactory.getMagicService();
	HttpSession session;
	private String space = null;
	private String objectId = null;
	private String mainlistView = null;
	private String mainViewAndMainButtonView = null;
	private String regionViewAndRegionButtonView = null;
	
	public int doEndTag() throws JspTagException {
		try {
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			String  contextPath =  request.getContextPath();
			String html = MagicTagUtil.getMagicDetail(contextPath,space,objectId,mainlistView,mainViewAndMainButtonView,regionViewAndRegionButtonView);
			pageContext.getOut().write(html);
		} catch (IOException e) {
			throw new JspTagException("MagicViewTag: " + e.getMessage());
		}
		return EVAL_PAGE;
	}

	public String getspace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	
	public MagicDetailTag() {
		this.session = null;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getMainlistView() {
		return mainlistView;
	}

	public void setMainlistView(String mainlistView) {
		this.mainlistView = mainlistView;
	}

	public String getMainViewAndMainButtonView() {
		return mainViewAndMainButtonView;
	}

	public void setMainViewAndMainButtonView(String mainViewAndMainButtonView) {
		this.mainViewAndMainButtonView = mainViewAndMainButtonView;
	}

	public String getRegionViewAndRegionButtonView() {
		return regionViewAndRegionButtonView;
	}

	public void setRegionViewAndRegionButtonView(String regionViewAndRegionButtonView) {
		this.regionViewAndRegionButtonView = regionViewAndRegionButtonView;
	}

	public String getSpace() {
		return space;
	}

}
