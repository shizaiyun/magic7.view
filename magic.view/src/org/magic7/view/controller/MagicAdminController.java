package org.magic7.view.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.magic7.core.domain.MagicChoice;
import org.magic7.core.domain.MagicDimension;
import org.magic7.core.domain.MagicSpace;
import org.magic7.core.domain.MagicSpaceRegion;
import org.magic7.core.domain.MagicSpaceRegionView;
import org.magic7.core.domain.MagicSpaceRegionViewItem;
import org.magic7.core.service.MagicService;
import org.magic7.core.service.MagicServiceFactory;
import org.magic7.core.service.MagicSpaceHandler;
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
		mode.setViewName("admin/spaceList");
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
		listRegion(request);
		request.setAttribute("spaceName", space.getName());
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/spaceDetail");
		return mode;
	}
	
	@RequestMapping(value = "/showSpace", method = RequestMethod.GET)
	public ModelAndView showSpace(HttpServletRequest request) {
		String spaceId = request.getParameter("spaceId");
		MagicSpace space = null;
		if(StringUtils.isNotEmpty(spaceId))
			space = service.getSpaceById(spaceId);
		request.setAttribute("space", space);
		listRegion(request);
		request.setAttribute("spaceName", space.getName());
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/spaceDetail");
		return mode;
	}
	
	@RequestMapping(value = "/saveRegion", method = RequestMethod.GET)
	public ModelAndView saveRegion(HttpServletRequest request) {
		String regionId = request.getParameter("regionId");
		MagicSpaceRegion region = null;
		if(StringUtils.isNotEmpty(regionId))
			region = service.getSpaceRegionById(regionId);
		else 
			region = new MagicSpaceRegion();
		region.setName(request.getParameter("name"));
		region.setDescription(request.getParameter("description"));
		region.setPartition(request.getParameter("partition"));
		region.setSpaceId(request.getParameter("spaceId"));
		region.setSpaceName(request.getParameter("spaceName"));
		region.setRegionType(Integer.parseInt(request.getParameter("regionType")));
		region.setMultiply(Boolean.parseBoolean(request.getParameter("multiply")));
		region.setSeq(Integer.parseInt(request.getParameter("seq")));
		
		service.saveSpaceRegion(region);
		request.setAttribute("region", region);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionName", region.getName());
		request.setAttribute("regionId", region.getId());
		listDimension(request);
		listView(request);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/regionDetail");
		return mode;
	}
	
	@RequestMapping(value = "/showRegion", method = RequestMethod.GET)
	public ModelAndView showRegion(HttpServletRequest request) {
		String regionId = request.getParameter("regionId");
		MagicSpaceRegion region = null;
		if(StringUtils.isNotEmpty(regionId))
			region = service.getSpaceRegionById(regionId);
		request.setAttribute("region", region);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		if(region!=null) {
			request.setAttribute("regionName", region.getName());
			request.setAttribute("regionId", region.getId());
			listDimension(request);
			listView(request);
		}
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/regionDetail");
		return mode;
	}
	
	@RequestMapping(value = "/listRegions", method = RequestMethod.GET)
	public ModelAndView listRegion(HttpServletRequest request) {
		String spaceName = request.getParameter("spaceName");
		String spaceId = request.getParameter("spaceId");
		System.out.println("spaceName:"+spaceName);
		List<MagicSpaceRegion> spaceRegions = service.listSpaceRegion(spaceName, spaceId, " seq ", 0, 100);
		request.setAttribute("spaceRegions", spaceRegions);
		request.setAttribute("spaceName", spaceName);
		request.setAttribute("spaceId", spaceId);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/regionList");
		return mode;
	}
	
	@RequestMapping(value = "/listDimensions", method = RequestMethod.GET)
	public ModelAndView listDimension(HttpServletRequest request) {
		Object temp = request.getAttribute("spaceName");
		String spaceName = null;
		if(temp!=null)
			spaceName = temp.toString();
		else
			spaceName = request.getParameter("spaceName");
		
		String regionName = null;
		temp = request.getAttribute("regionName");
		if(temp!=null)
			regionName = temp.toString();
		else
			regionName = request.getParameter("regionName");
		
		String viewName = request.getParameter("viewName");
		String dimensionNames = request.getParameter("dimensionNames");
		Integer destination = MagicDimension.Destination.FOR_DATA.getCode();
		if(request.getParameter("destination")!=null)
			destination = Integer.parseInt(request.getParameter("destination"));
		System.out.println("spaceName:"+spaceName);
		System.out.println("regionName:"+regionName);
		List<MagicDimension> dimensions = MagicSpaceHandler.listDimension(spaceName, regionName, viewName, dimensionNames, destination);
		request.setAttribute("dimensions", dimensions);
		String spaceId = request.getParameter("spaceId");
		request.setAttribute("spaceName", spaceName);
		request.setAttribute("spaceId", spaceId);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/dimensionList");
		return mode;
	}
	
	@RequestMapping(value = "/listViews", method = RequestMethod.GET)
	public ModelAndView listView(HttpServletRequest request) {
		Object temp = request.getAttribute("spaceName");
		String spaceName = null;
		if(temp!=null)
			spaceName = temp.toString();
		else
			spaceName = request.getParameter("spaceName");
		String regionName = null;
		temp = request.getAttribute("regionName");
		if(temp!=null)
			regionName = temp.toString();
		else
			regionName = request.getParameter("regionName");
		
		System.out.println("spaceName:"+spaceName);
		System.out.println("regionName:"+regionName);
		List<MagicSpaceRegionView> views = service.listSpaceRegionView(spaceName, regionName);
		
		request.setAttribute("views", views);
		String spaceId = request.getParameter("spaceId");
		String regionId = request.getParameter("regionId");
		request.setAttribute("regionName", regionName);
		request.setAttribute("regionId", regionId);
		request.setAttribute("spaceName", spaceName);
		request.setAttribute("spaceId", spaceId);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/viewList");
		return mode;
	}
	
	@RequestMapping(value = "/showDimension", method = RequestMethod.GET)
	public ModelAndView showDimension(HttpServletRequest request) {
		String dimensionId = request.getParameter("dimensionId");
		MagicDimension dimension = null;
		if(StringUtils.isNotEmpty(dimensionId))
			dimension = service.getDimensionById(dimensionId);
		request.setAttribute("dimension", dimension);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionId", request.getParameter("regionId"));
		request.setAttribute("regionName", request.getParameter("regionName"));
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/dimensionDetail");
		return mode;
	}
	
	@RequestMapping(value = "/saveDimension", method = RequestMethod.GET)
	public ModelAndView saveDimension(HttpServletRequest request) {
		String dimensionId = request.getParameter("dimensionId");
		MagicDimension dimension = null;
		if(StringUtils.isNotEmpty(dimensionId))
			dimension = service.getDimensionById(dimensionId);
		else 
			dimension = new MagicDimension();
		dimension.setName(request.getParameter("name"));
		dimension.setDisplayName(request.getParameter("displayName"));
		dimension.setDescription(request.getParameter("description"));
		dimension.setSpaceId(request.getParameter("spaceId"));
		dimension.setSpaceName(request.getParameter("spaceName"));
		dimension.setSpaceRegionId(request.getParameter("regionId"));
		dimension.setSpaceRegionName(request.getParameter("regionName"));
		dimension.setSeq(Integer.parseInt(request.getParameter("seq")));
		dimension.setDestination(Integer.parseInt(request.getParameter("destination")));
		dimension.setPageType(Integer.parseInt(request.getParameter("pageType")));
		dimension.setValueType(Integer.parseInt(request.getParameter("valueType")));
		dimension.setRequired(Boolean.parseBoolean(request.getParameter("required")));
		dimension.setEditable(Boolean.parseBoolean(request.getParameter("editable")));
		dimension.setVisible(Boolean.parseBoolean(request.getParameter("visible")));
		dimension.setLnk(Boolean.parseBoolean(request.getParameter("lnk")));
		dimension.setVirtual(!dimension.getLnk());
		dimension.setUrl(request.getParameter("popUpUrl"));
		
		service.saveDimension(dimension);
		request.setAttribute("dimension", dimension);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionId", request.getParameter("regionId"));
		request.setAttribute("regionName", request.getParameter("regionName"));
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/dimensionDetail");
		return mode;
	}
	
	@RequestMapping(value = "/showView", method = RequestMethod.GET)
	public ModelAndView showView(HttpServletRequest request) {
		String viewId = request.getParameter("viewId");
		MagicSpaceRegionView view = null;
		System.out.println("viewId:"+viewId);
		if(StringUtils.isNotEmpty(viewId))
			view = service.getViewById(viewId);
		if(view!=null) {
			List<MagicSpaceRegionViewItem> items = service.listSpaceRegionViewItem(view.getSpaceName(), view.getSpaceRegionName(), view.getName(), " seq ");
			request.setAttribute("items", items);
		} else
			view = new MagicSpaceRegionView();
		
		request.setAttribute("view", view);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionId", request.getParameter("regionId"));
		request.setAttribute("regionName", request.getParameter("regionName"));
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/viewDetail");
		return mode;
	}
	
	@RequestMapping(value = "/saveView", method = RequestMethod.GET)
	public ModelAndView saveView(HttpServletRequest request) {
		String viewId = request.getParameter("viewId");
		MagicSpaceRegionView view = null;
		if(StringUtils.isNotEmpty(viewId))
			view = service.getViewById(viewId);
		else {
			view = new MagicSpaceRegionView();
			view.setSpaceId(request.getParameter("spaceId"));
			view.setSpaceRegionId(request.getParameter("regionId"));
			view.setSpaceName(request.getParameter("spaceName"));
			view.setSpaceRegionName(request.getParameter("regionName"));
		}
		view.setName(request.getParameter("name"));
		service.saveSpaceRegionView(view);
		
		List<MagicSpaceRegionViewItem> items = service.listSpaceRegionViewItem(view.getSpaceName(), view.getSpaceRegionName(), view.getName(), " seq ");
		request.setAttribute("view", view);
		request.setAttribute("items", items);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionId", request.getParameter("regionId"));
		request.setAttribute("regionName", request.getParameter("regionName"));
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/viewDetail");
		return mode;
	}
	
	@RequestMapping(value = "/showViewItem", method = RequestMethod.GET)
	public ModelAndView showViewItem(HttpServletRequest request) {
		String itemId = request.getParameter("itemId");
		MagicSpaceRegionViewItem item = null;
		System.out.println("itemId:"+itemId);
		if(StringUtils.isNotEmpty(itemId))
			item = service.getViewItemById(itemId);
		else
			item = new MagicSpaceRegionViewItem();
		String spaceName = request.getParameter("spaceName");
		String regionName = request.getParameter("regionName");
		List<MagicDimension> dimensions = MagicSpaceHandler.listDimension(spaceName, regionName, null, null, MagicDimension.Destination.FOR_DATA.getCode());
		System.out.println("dimensions:"+dimensions);
		request.setAttribute("dimensions", dimensions);
		request.setAttribute("item", item);
		request.setAttribute("spaceName",request.getParameter("spaceName") );
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionId", request.getParameter("regionId"));
		request.setAttribute("regionName", request.getParameter("regionName"));
		request.setAttribute("viewId", request.getParameter("viewId"));
		request.setAttribute("viewName", request.getParameter("viewName"));
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/viewItemDetail");
		return mode;
	}
	
	@RequestMapping(value = "/saveViewItem", method = RequestMethod.GET)
	public ModelAndView saveViewItem(HttpServletRequest request) {
		String itemId = request.getParameter("itemId");
		MagicSpaceRegionViewItem item = null;
		if(StringUtils.isNotEmpty(itemId))
			item = service.getViewItemById( itemId);
		else 
			item = new MagicSpaceRegionViewItem();
		
		item.setName(request.getParameter("name"));
		item.setSpaceId(request.getParameter("spaceId"));
		item.setSpaceName(request.getParameter("spaceName"));
		item.setSpaceRegionId(request.getParameter("regionId"));
		item.setSpaceRegionName(request.getParameter("regionName"));
		item.setSeq(Integer.parseInt(request.getParameter("seq")));
		//item.setPageType(Integer.parseInt(request.getParameter("pageType")));
		item.setRequired(Boolean.parseBoolean(request.getParameter("required")));
		item.setEditable(Boolean.parseBoolean(request.getParameter("editable")));
		item.setVisible(Boolean.parseBoolean(request.getParameter("visible")));
		item.setDimensionId(request.getParameter("dimensionId"));
		item.setViewId(request.getParameter("viewId"));
		item.setViewName(request.getParameter("viewName"));
		
		service.saveSpaceRegionViewItem(item);
		
		List<MagicDimension> dimensions = MagicSpaceHandler.listDimension(request.getParameter("spaceName"), request.getParameter("regionName"), null, null, MagicDimension.Destination.FOR_DATA.getCode());
		System.out.println("dimensions:"+dimensions);
		request.setAttribute("dimensions", dimensions);
		request.setAttribute("item", item);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionId", request.getParameter("regionId"));
		request.setAttribute("regionName", request.getParameter("regionName"));
		request.setAttribute("viewId", request.getParameter("viewId"));
		request.setAttribute("viewName", request.getParameter("viewName"));
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/viewItemDetail");
		return mode;
	}
	@RequestMapping(value = "/showChoiceItem", method = RequestMethod.GET)
	public ModelAndView showChoiceItem(HttpServletRequest request) {
		String itemId = request.getParameter("itemId");
		MagicSpaceRegionViewItem item = null;
		System.out.println("itemId:"+itemId);
		if(StringUtils.isNotEmpty(itemId))
			item = service.getViewItemById(itemId);
		else
			item = new MagicSpaceRegionViewItem();
		String spaceName = request.getParameter("spaceName");
		String regionName = request.getParameter("regionName");
		List<MagicDimension> dimensions = MagicSpaceHandler.listDimension(spaceName, regionName, null, null, MagicDimension.Destination.FOR_DATA.getCode());
		System.out.println("dimensions:"+dimensions);
		request.setAttribute("dimensions", dimensions);
		request.setAttribute("item", item);
		request.setAttribute("spaceName",request.getParameter("spaceName") );
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionId", request.getParameter("regionId"));
		request.setAttribute("regionName", request.getParameter("regionName"));
		request.setAttribute("viewId", request.getParameter("viewId"));
		request.setAttribute("viewName", request.getParameter("viewName"));
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/viewItemDetail");
		return mode;
	}
	@RequestMapping(value = "/listChoice", method = RequestMethod.GET)
	public ModelAndView listChoice(HttpServletRequest request) {
		String choiceName = request.getParameter("choiceName");
		List<MagicChoice> choices = service.listChoice(choiceName, null);
		request.setAttribute("choices", choices);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/choiceList");
		return mode;
	}
	@RequestMapping(value = "/showChoice", method = RequestMethod.GET)
	public ModelAndView showChoice(HttpServletRequest request) {
		String choiceId = request.getParameter("choiceId");
		MagicChoice choice = null;
		if(StringUtils.isNotEmpty(choiceId))
			choice = service.getChoiceById(choiceId);
		request.setAttribute("choice", choice);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/choiceDetail");
		return mode;
	}
	@RequestMapping(value = "/saveChoice", method = RequestMethod.GET)
	public ModelAndView saveChoice(HttpServletRequest request) {
		String choiceId = request.getParameter("choiceId");
		MagicChoice choice = null;
		if(StringUtils.isNotEmpty(choiceId))
			choice = service.getChoiceById(choiceId);
		else
			choice = new MagicChoice();
		choice.setChoiceName(request.getParameter("name"));
		choice.setChoiceCode(request.getParameter("code"));
		service.saveChoice(choice);
		request.setAttribute("choice", choice);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/choiceDetail");
		return mode;
	}
}
