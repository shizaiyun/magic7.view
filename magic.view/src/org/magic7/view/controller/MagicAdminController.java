package org.magic7.view.controller;

import java.util.List;

import org.magic7.core.domain.MagicSpace;
import org.magic7.core.service.MagicService;
import org.magic7.core.service.MagicServiceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/magic/admin")
public class MagicAdminController {
	private static final MagicService service =  MagicServiceFactory.getMagicService();
	private static Integer defaultPageSize = 10;
	@RequestMapping(value = "/showList", method = RequestMethod.GET)
	public ModelAndView listSpace(@RequestParam(value = "spaceName") String spaceName,@RequestParam(value = "orderBy") String orderBy,
			@RequestParam(value = "currentPage")Integer currentPage,@RequestParam(value = "pageSize")Integer pageSize) {
		if(pageSize==null)
			pageSize = defaultPageSize;
		if(currentPage==null)
			currentPage = 0;
		List<MagicSpace> spaces = service.listSpace(spaceName, orderBy, (currentPage-1)*currentPage, pageSize);
		ModelAndView mode = new ModelAndView();
		mode.addObject("spaces", spaces);
		mode.setViewName("admin/listSpace");
		return mode;
	}
}
