package org.magic7.view.tag;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.magic7.core.service.MagicService;
import org.magic7.core.service.MagicServiceFactory;
import org.magic7.view.utils.MagicTagUtil;


public final class MagicRegionTag extends TagSupport {
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -137059850317165619L;
	public static MagicService service = MagicServiceFactory.getLawService();
	HttpSession session;
	private String space = null;
	private String region = null;
	private String objectId = null;

	public int doEndTag() throws JspTagException {
		try {
			String html = MagicTagUtil.getMagicRegion(space, region, objectId);
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

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	
	public MagicRegionTag() {
		this.session = null;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}



}
