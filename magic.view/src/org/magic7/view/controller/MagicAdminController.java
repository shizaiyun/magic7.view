package org.magic7.view.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.magic7.core.domain.MagicSpace;
import org.magic7.core.service.MagicService;
import org.magic7.core.service.MagicServiceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/magic/admin")
public class MagicAdminController {
	private static final MagicService service =  MagicServiceFactory.getMagicService();
	private static Integer defaultPageSize = 10;
	@RequestMapping(value = "/listSpaces", method = RequestMethod.GET)
	public ModelAndView listSpace(HttpServletRequest request) {
		String spaceName = request.getParameter("spaceName");
		String orderBy = request.getParameter("orderBy");
		Integer currentPage = 1;
		String page = request.getParameter("currentPage");
		if(page!=null)
			currentPage = Integer.parseInt(page);
		String size = request.getParameter("pageSize");
		Integer pageSize = defaultPageSize;
		if(size!=null)
			pageSize = Integer.parseInt(size);
		List<MagicSpace> spaces = service.listSpace(spaceName, orderBy, (currentPage-1)*currentPage, pageSize);
		request.setAttribute("spaces", spaces);
		request.setAttribute("spaceName", spaceName);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/listSpaces");
		return mode;
	}
	
	@RequestMapping(value = "/saveSpace", method = RequestMethod.GET)
	public ModelAndView saveSpace(HttpServletRequest request) {
		String spaceId = request.getParameter("spaceId");
		MagicSpace space = null;
		if(StringUtils.isNotEmpty(spaceId))
			space = service.getSpaceById(spaceId);
		else 
			space = new MagicSpace();
		space.setName(request.getParameter("name"));
		space.setDescription(request.getParameter("description"));
		space.setCreateDate(new Date());
		space.setPartition(request.getParameter("partition"));
		space.setTabLayout(request.getParameter("tabLayout"));
		String valid = request.getParameter("valid");
			if(valid!=null)
		space.setValid(Boolean.parseBoolean(valid));
		
		service.saveSpace(space);
		request.setAttribute("space", space);
		
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/addSpace");
		return mode;
	}
	
	@RequestMapping(value = "/showSpace", method = RequestMethod.GET)
	public ModelAndView showSpace(HttpServletRequest request) {
		String spaceId = request.getParameter("spaceId");
		MagicSpace space = null;
		if(StringUtils.isNotEmpty(spaceId))
			space = service.getSpaceById(spaceId);
		
		request.setAttribute("space", space);
		
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/addSpace");
		return mode;
	}
}
