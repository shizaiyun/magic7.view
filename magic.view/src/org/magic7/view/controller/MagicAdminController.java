package org.magic7.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/magic/admin")
public class MagicAdminController {
	@RequestMapping(value = "/showList", method = RequestMethod.GET)
	public ModelAndView listSpace(@RequestParam(value = "spaceName") String spaceName) {
		return null;
	}
}
