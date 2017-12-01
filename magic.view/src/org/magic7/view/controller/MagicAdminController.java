package org.magic7.view.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringUtils;
import org.magic7.core.domain.MagicChoice;
import org.magic7.core.domain.MagicChoiceItem;
import org.magic7.core.domain.MagicCodeLib;
import org.magic7.core.domain.MagicDimension;
import org.magic7.core.domain.MagicRegionCodeLnk;
import org.magic7.core.domain.MagicSpace;
import org.magic7.core.domain.MagicSpaceRegion;
import org.magic7.core.domain.MagicSpaceRegionView;
import org.magic7.core.domain.MagicSpaceRegionViewItem;
import org.magic7.core.domain.MagicTriggerAssembler;
import org.magic7.core.service.MagicService;
import org.magic7.core.service.MagicServiceFactory;
import org.magic7.core.service.MagicSpaceHandler;
import org.magic7.dynamic.loader.MagicLoaderUtils;
import org.magic7.utils.MagicUtil;
import org.magic7.utils.ConvertMhtToHtml;
import org.magic7.utils.ServiceUtil;
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
		if(space!=null) {
			request.setAttribute("spaceName", space.getName());
			request.setAttribute("spaceId", space.getId());
		}
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/spaceDetail");
		return mode;
	}
	
	@RequestMapping(value = "/saveRegion", method = RequestMethod.GET)
	public ModelAndView saveRegion(HttpServletRequest request) {
		String regionId = request.getParameter("regionId");
		String codeIds = request.getParameter("codeIds");
		String codeIdArray[] = null;
		if(StringUtils.isNotEmpty(codeIds)) {
			codeIdArray = codeIds.replaceAll("\\,$", "").split(",");
		}
		MagicSpaceRegion region = null;
		HashSet<String> newLnks = new HashSet<>();
		if(StringUtils.isNotEmpty(regionId)) {
			region = service.getSpaceRegionById(regionId);
			List<MagicCodeLib> javaCodesWithLnk = service.listCodeLibWithLnk(region.getSpaceName(), region.getName(), MagicCodeLib.CodeType.JAVA.getCode());
			HashMap<String,MagicCodeLib> deleteLnks = new HashMap<>();
			if(codeIdArray!=null) {
				for(MagicCodeLib lnk:javaCodesWithLnk) {
					deleteLnks.put(lnk.getId(),lnk);
				}
				for(String codeId:codeIdArray) {
					System.out.println(codeIds);
					System.out.println("process:"+codeId);
					if(deleteLnks.get(codeId)!=null) {
						deleteLnks.remove(codeId);
						System.out.println("keep:"+codeId);
					} else {
						newLnks.add(codeId);
						System.out.println("add:"+codeId);
					}
				}
				for(String codeId:deleteLnks.keySet())
					service.deleteCodeLnk(codeId, region.getSpaceName(), region.getName());
				
			}
		} else 
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
		
		for(String codeId:newLnks) {
			MagicRegionCodeLnk lnk = new MagicRegionCodeLnk();
			MagicCodeLib lib = service.getCodeLibById(codeId);
			lnk.setCodeLidId(codeId);
			lnk.setCodeName(lib.getName());
			lnk.setParameterNames(lib.getParameterNames());
			lnk.setRegionId(region.getId());
			lnk.setRegionName(region.getName());
			lnk.setSignature(lib.getSignature());
			lnk.setSpaceId(region.getSpaceId());
			lnk.setSpaceName(region.getSpaceName());
			service.saveReginCodeLnk(lnk);
		}
		
		List<MagicCodeLib> javaCodesWithLnk = service.listCodeLibWithLnk(region.getSpaceName(), region.getName(), MagicCodeLib.CodeType.JAVA.getCode());
		request.setAttribute("javaCodesWithLnk", javaCodesWithLnk);
		List<MagicCodeLib> javaCodes = service.listCodeLib(null, null, MagicCodeLib.CodeType.JAVA.getCode(), null, 0, 1000);
		request.setAttribute("javaCodes", javaCodes);
		
		request.setAttribute("region", region);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionName", region.getName());
		request.setAttribute("regionId", region.getId());
		listDimension(request);
		listDimensionForQuery(request);
		listDimensionForButton(request);
		listView(request);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("redirect:showRegion?regionId="+region.getId()+"&spaceName="+region.getSpaceName()+"&spaceId="+region.getSpaceId());
		return mode;
	}
	
	@RequestMapping(value = "/showRegion", method = RequestMethod.GET)
	public ModelAndView showRegion(HttpServletRequest request) {
		String regionId = request.getParameter("regionId");
		MagicSpaceRegion region = null;
		if(StringUtils.isNotEmpty(regionId)) {
			region = service.getSpaceRegionById(regionId);
			List<MagicCodeLib> javaCodesWithLnk = service.listCodeLibWithLnk(region.getSpaceName(), region.getName(), MagicCodeLib.CodeType.JAVA.getCode());
			request.setAttribute("javaCodesWithLnk", javaCodesWithLnk);
		}
		List<MagicCodeLib> javaCodes = service.listCodeLib(null, null, MagicCodeLib.CodeType.JAVA.getCode(), null, 0, 1000);
		request.setAttribute("javaCodes", javaCodes);
		request.setAttribute("region", region);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		if(region!=null) {
			request.setAttribute("regionName", region.getName());
			request.setAttribute("regionId", region.getId());
			listDimension(request);
			listDimensionForQuery(request);
			listDimensionForButton(request);
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
	
	@RequestMapping(value = "/listDimensionForQuery", method = RequestMethod.GET)
	public ModelAndView listDimensionForQuery(HttpServletRequest request) {
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
		Integer destination = MagicDimension.Destination.FOR_QUERY.getCode();
		System.out.println("spaceName:"+spaceName);
		System.out.println("regionName:"+regionName);
		List<MagicDimension> dimensions = MagicSpaceHandler.listDimension(spaceName, regionName, viewName, dimensionNames, destination);
		request.setAttribute("dimensionsForQuery", dimensions);
		String spaceId = request.getParameter("spaceId");
		request.setAttribute("spaceName", spaceName);
		request.setAttribute("spaceId", spaceId);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/dimensionListForQuery");
		return mode;
	}
	
	@RequestMapping(value = "/listDimensionForButton", method = RequestMethod.GET)
	public ModelAndView listDimensionForButton(HttpServletRequest request) {
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
		Integer destination = MagicDimension.Destination.FOR_BUTTON.getCode();
		System.out.println("spaceName:"+spaceName);
		System.out.println("regionName:"+regionName);
		List<MagicDimension> dimensions = MagicSpaceHandler.listDimension(spaceName, regionName, viewName, dimensionNames, destination);
		request.setAttribute("dimensionsForButton", dimensions);
		String spaceId = request.getParameter("spaceId");
		request.setAttribute("spaceName", spaceName);
		request.setAttribute("spaceId", spaceId);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/dimensionListForQuery");
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
		String command = request.getParameter("command");
		MagicDimension dimension = null;
		if(StringUtils.isNotEmpty(dimensionId)) {
			dimension = service.getDimensionById(dimensionId);
		}
		request.setAttribute("dimension", dimension);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionId", request.getParameter("regionId"));
		request.setAttribute("regionName", request.getParameter("regionName"));
		ModelAndView mode = new ModelAndView();
		if(command==null)
			mode.setViewName("admin/dimensionDetail");
		else if("forQuery".equals(command))
			mode.setViewName("admin/dimensionDetailForQuery");
		else if("forButton".equals(command))
			mode.setViewName("admin/dimensionDetailForButton");
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
		dimension.setChoiceCode(request.getParameter("choiceCode"));
		
		service.saveDimension(dimension);
		request.setAttribute("dimension", dimension);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionId", request.getParameter("regionId"));
		request.setAttribute("regionName", request.getParameter("regionName"));
		ModelAndView mode = new ModelAndView();
		mode.setViewName("redirect:showDimension?dimensionId="+dimension.getId()+"&spaceName="+dimension.getSpaceName()
		+"&spaceId="+dimension.getSpaceId()+"&regionId="+dimension.getSpaceRegionId()+"&regionName="+dimension.getSpaceRegionName());
		return mode;
	}
	
	@RequestMapping(value = "/saveDimensionForQuery", method = RequestMethod.GET)
	public ModelAndView saveDimensionForQuery(HttpServletRequest request) {
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
		dimension.setDestination(MagicDimension.Destination.FOR_QUERY.getCode());
		dimension.setPageType(Integer.parseInt(request.getParameter("pageType")));
		dimension.setValueType(Integer.parseInt(request.getParameter("valueType")));
		dimension.setLnk(Boolean.parseBoolean(request.getParameter("lnk")));
		dimension.setVirtual(!dimension.getLnk());
		dimension.setQueryType(Integer.parseInt(request.getParameter("queryType")));
		dimension.setChoiceCode(request.getParameter("choiceCode"));
		
		dimension.setRequired(false);
		dimension.setEditable(true);
		dimension.setVisible(true);
		
		service.saveDimension(dimension);
		request.setAttribute("dimension", dimension);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionId", request.getParameter("regionId"));
		request.setAttribute("regionName", request.getParameter("regionName"));
		ModelAndView mode = new ModelAndView();
		mode.setViewName("redirect:showDimension?dimensionId="+dimension.getId()+"&spaceName="+dimension.getSpaceName()
			+"&spaceId="+dimension.getSpaceId()+"&regionId="+dimension.getSpaceRegionId()+"&regionName="+dimension.getSpaceRegionName()+"&command=forQuery");
		return mode;
	}
	
	@RequestMapping(value = "/saveDimensionForButton", method = RequestMethod.GET)
	public ModelAndView saveDimensionForButton(HttpServletRequest request) {
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
		dimension.setDestination(MagicDimension.Destination.FOR_BUTTON.getCode());
		dimension.setButtonTrigger(request.getParameter("buttonTrigger"));
		dimension.setLnk(false);
		dimension.setVirtual(true);
		dimension.setPageType(MagicDimension.PageType.BUTTON.getCode());
		dimension.setValueType(MagicDimension.ValueType.STR_VALUE.getCode());
		
		dimension.setRequired(false);
		dimension.setEditable(true);
		dimension.setVisible(true);
		
		service.saveDimension(dimension);
		request.setAttribute("dimension", dimension);
		request.setAttribute("spaceName", request.getParameter("spaceName"));
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionId", request.getParameter("regionId"));
		request.setAttribute("regionName", request.getParameter("regionName"));
		ModelAndView mode = new ModelAndView();
		mode.setViewName("redirect:showDimension?dimensionId="+dimension.getId()+"&spaceName="+dimension.getSpaceName()
			+"&spaceId="+dimension.getSpaceId()+"&regionId="+dimension.getSpaceRegionId()+"&regionName="+dimension.getSpaceRegionName()+"&command=forButton");
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
		String spaceName = request.getParameter("spaceName");
		List<MagicSpaceRegion> regions = service.listSpaceRegion(spaceName, null, " seq ", 0, 1000);
		
		request.setAttribute("view", view);
		request.setAttribute("regions", regions);
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
		else 
			view = new MagicSpaceRegionView();
		view.setName(request.getParameter("name"));
		view.setDestination(Integer.parseInt(request.getParameter("destination")));
		view.setViewType(Integer.parseInt(request.getParameter("viewType")));
		
		view.setSpaceId(request.getParameter("spaceId"));
		view.setSpaceName(request.getParameter("spaceName"));
		view.setDisplayName(request.getParameter("displayName"));
		String regionId = request.getParameter("regionId");
		String regionName = request.getParameter("regionName");
		if(StringUtils.isNotEmpty(regionId)) {
			MagicSpaceRegion region = service.getSpaceRegionById(regionId);
			regionName = region.getName();
			view.setSpaceRegionId(request.getParameter("regionId"));
			view.setSpaceRegionName(regionName);
		}
		
		service.saveSpaceRegionView(view);
		
		List<MagicSpaceRegionViewItem> items = service.listSpaceRegionViewItem(view.getSpaceName(), view.getSpaceRegionName(), view.getName(), " seq ");
		request.setAttribute("view", view);
		request.setAttribute("items", items);
		String spaceName = request.getParameter("spaceName");
		String spaceId = request.getParameter("spaceId");
		ModelAndView mode = new ModelAndView();
		mode.setViewName("redirect:showView?viewId="+viewId+"&spaceName="+spaceName+"&spaceId="+spaceId+"&regionId="+regionId+"&regionName="+regionName);
		return mode;
	}
	
	@RequestMapping(value = "/showViewItem", method = RequestMethod.GET)
	public ModelAndView showViewItem(HttpServletRequest request) {
		String itemId = request.getParameter("itemId");
		MagicSpaceRegionViewItem item = null;
		if(StringUtils.isNotEmpty(itemId))
			item = service.getViewItemById(itemId);
		else
			item = new MagicSpaceRegionViewItem();
		String spaceName = request.getParameter("spaceName");
		String regionName = request.getParameter("regionName");
		Integer destination = Integer.parseInt(request.getParameter("destination"));
		List<MagicDimension> dimensions = MagicSpaceHandler.listDimension(spaceName, regionName, null, null, destination);
		
		if(MagicDimension.Destination.FOR_BUTTON.getCode().equals(destination)) {
			List<MagicDimension> dataDimensions = MagicSpaceHandler.listDimension(spaceName, regionName, null, null, MagicDimension.Destination.FOR_DATA.getCode());
			List<MagicCodeLib> javaCodesWithLnk = service.listCodeLibWithLnk(spaceName, regionName, MagicCodeLib.CodeType.JAVA.getCode());
			request.setAttribute("javaCodesWithLnk", javaCodesWithLnk);
			request.setAttribute("dataDimensions", dataDimensions);
			if(StringUtils.isNotEmpty(itemId)) {
				List<MagicTriggerAssembler> assemblers = service.listTriggerAssembler(item.getBusinessTrigger(), spaceName, regionName, " seq ");
				request.setAttribute("assemblers", assemblers);
			}
		}
		
		request.setAttribute("dimensions", dimensions);
		request.setAttribute("item", item);
		request.setAttribute("spaceName",request.getParameter("spaceName") );
		request.setAttribute("spaceId", request.getParameter("spaceId"));
		request.setAttribute("regionId", request.getParameter("regionId"));
		request.setAttribute("regionName", request.getParameter("regionName"));
		request.setAttribute("viewId", request.getParameter("viewId"));
		request.setAttribute("viewName", request.getParameter("viewName"));
		request.setAttribute("destination", destination);
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
		String spaceName = request.getParameter("spaceName");
		String spaceId = request.getParameter("spaceId");
		String regionName = request.getParameter("regionName");
		String regionId = request.getParameter("regionId");
		String viewId = request.getParameter("viewId");
		String viewName = request.getParameter("viewName");
		
		item.setName(request.getParameter("name"));
		item.setSpaceId(spaceId);
		item.setSpaceName(spaceName);
		item.setSpaceRegionId(regionId);
		item.setSpaceRegionName(regionName);
		item.setSeq(Integer.parseInt(request.getParameter("seq")));
		//item.setPageType(Integer.parseInt(request.getParameter("pageType")));
		item.setRequired(Boolean.parseBoolean(request.getParameter("required")));
		item.setEditable(Boolean.parseBoolean(request.getParameter("editable")));
		item.setVisible(Boolean.parseBoolean(request.getParameter("visible")));
		item.setDimensionId(request.getParameter("dimensionId"));
		item.setViewId(viewId);
		item.setViewName(viewName);
		item.setChoiceCode(request.getParameter("choiceCode"));
		item.setBusinessTrigger(request.getParameter("businessTrigger"));
		
		service.saveSpaceRegionViewItem(item);
		
		request.setAttribute("item", item);
		request.setAttribute("spaceName", spaceName);
		request.setAttribute("spaceId", spaceId);
		request.setAttribute("regionId", regionId);
		request.setAttribute("regionName", regionName);
		request.setAttribute("viewId", viewId);
		request.setAttribute("viewName", viewName);
		MagicSpaceRegionView view = service.getViewById(viewId);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("redirect:showViewItem?itemId="+item.getId()+"&spaceId="+spaceId+"&spaceName="+spaceName+"&regionName="+regionName+"&regionId="+regionId+"&viewId="+viewId+"&viewName="+viewName+"&destination="+view.getDestination());
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
		if(StringUtils.isNotEmpty(choiceId)) {
			choice = service.getChoiceById(choiceId);
			request.setAttribute("code", choice.getChoiceCode());
		}
		request.setAttribute("choice", choice);
		listChoiceItem(request);
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
		listChoiceItem(request);
		request.setAttribute("choice", choice);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("redirect:showChoice?choiceId="+choice.getId());
		return mode;
	}
	@RequestMapping(value = "/listChoiceItem", method = RequestMethod.GET)
	public ModelAndView listChoiceItem(HttpServletRequest request) {
		String code = request.getParameter("code");
		Object temp = request.getAttribute("code");
		if(code==null&&temp!=null)
			code = temp.toString();
		List<MagicChoiceItem> items = service.listChoiceItem(null, code);
		request.setAttribute("items", items);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/choiceItemList");
		return mode;
	}
	@RequestMapping(value = "/showChoiceItem", method = RequestMethod.GET)
	public ModelAndView showChoiceItem(HttpServletRequest request) {
		String itemId = request.getParameter("itemId");
		String choiceId = request.getParameter("choiceId");
		MagicChoiceItem item = null;
		if(StringUtils.isNotEmpty(itemId))
			item = service.getChoiceItemById(itemId);
		request.setAttribute("item", item);
		request.setAttribute("choiceId", choiceId);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/choiceItemDetail");
		return mode;
	}
	@RequestMapping(value = "/saveChoiceItem", method = RequestMethod.GET)
	public ModelAndView saveChoiceItem(HttpServletRequest request) {
		String itemId = request.getParameter("itemId");
		String choiceId = request.getParameter("choiceId");
		MagicChoiceItem item = null;
		MagicChoice choice = service.getChoiceById(choiceId);
		if(StringUtils.isNotEmpty(itemId))
			item = service.getChoiceItemById(itemId);
		else
			item = new MagicChoiceItem();
		item.setChoiceName(choice.getChoiceName());
		item.setChoiceCode(choice.getChoiceCode());
		item.setValueCode(request.getParameter("valueCode"));
		item.setValueName(request.getParameter("valueName"));
		item.setSeq(Integer.parseInt(request.getParameter("seq")));
		service.saveChoiceItem(item);
		request.setAttribute("item", item);
		request.setAttribute("choiceId", choiceId);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/choiceItemDetail");
		return mode;
	}
	@RequestMapping(value = "/listCode", method = RequestMethod.GET)
	public ModelAndView listCode(HttpServletRequest request) {
		List<MagicCodeLib> codes = service.listCodeLib(null, null, MagicCodeLib.CodeType.JAVA.getCode(),null,0,1000);
		request.setAttribute("codes", codes);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/codeList");
		return mode;
	}
	@RequestMapping(value = "/showCode", method = RequestMethod.GET)
	public ModelAndView showCode(HttpServletRequest request) {
		String codeId = request.getParameter("codeId");
		MagicCodeLib code = null;
		if(StringUtils.isNotEmpty(codeId))
			code = service.getCodeLibById(codeId);
		request.setAttribute("code", code);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("admin/codeDetail");
		return mode;
	}
	@RequestMapping(value = "/saveCode", method = RequestMethod.GET)
	public ModelAndView saveCode(HttpServletRequest request) {
		String codeId = request.getParameter("codeId");
		MagicCodeLib code = null;
		if(StringUtils.isNotEmpty(codeId))
			code = service.getCodeLibById(codeId);
		else
			code = new MagicCodeLib();
		code.setName(request.getParameter("name"));
		code.setDescription(request.getParameter("description"));
		code.setCode(request.getParameter("code"));
		code.setParameterNames(request.getParameter("parameterNames"));
		code.setSignature(MagicLoaderUtils.generateSignatur("test", "test", code));
		code.setCodeType(Integer.parseInt(request.getParameter("codeType")));
		service.saveCodeLib(code);
		request.setAttribute("code", code);
		ModelAndView mode = new ModelAndView();
		mode.setViewName("redirect:showCode?codeId="+code.getId());
		return mode;
	}
	@RequestMapping(value = "/saveAssembler", method = RequestMethod.GET)
	public ModelAndView saveAssembler(HttpServletRequest request) {
		String dimensionId = request.getParameter("dimensionId");
		String codeId = request.getParameter("codeId");
		String itemId = request.getParameter("itemId");
		String seqStr = request.getParameter("seq");
		Integer seq = 0;
		if(StringUtils.isNotEmpty(seqStr))
			seq = Integer.parseInt(seqStr);
		MagicDimension dimension = service.getDimensionById(dimensionId);
		MagicCodeLib lib = service.getCodeLibById(codeId);
		MagicSpaceRegionViewItem item = service.getViewItemById(itemId);
		String assemblerId = request.getParameter("assemblerId");
		MagicUtil.bindTrigger(assemblerId, item.getBusinessTrigger(), lib, dimension, seq);
		
		ModelAndView mode = new ModelAndView();
		mode.setViewName("redirect:showViewItem?itemId="+itemId+"&spaceId="+dimension.getSpaceId()+"&spaceName="+dimension.getSpaceName()+"&regionName="+dimension.getSpaceRegionName()+"&regionId="+dimension.getSpaceRegionId()+"&viewId="+item.getViewId()+"&viewName="+item.getViewName()+"&destination="+MagicDimension.Destination.FOR_BUTTON.getCode());
		return mode;
	}
	@RequestMapping(value = "/deleteAssembler", method = RequestMethod.GET)
	public ModelAndView deleteAssembler(HttpServletRequest request) {
		String assemblerId = request.getParameter("assemblerId");
		MagicTriggerAssembler assembler = service.getAssemblerById(assemblerId);
		String itemId = request.getParameter("itemId");
		ModelAndView mode = new ModelAndView();
		MagicDimension dimension = service.getDimensionById(assembler.getDimensionId());
		MagicSpaceRegionViewItem item = service.getViewItemById(itemId);
		service.deleteAssembler(assembler);
		mode.setViewName("redirect:showViewItem?itemId="+itemId+"&spaceId="+dimension.getSpaceId()+"&spaceName="+dimension.getSpaceName()+"&regionName="+dimension.getSpaceRegionName()+"&regionId="+dimension.getSpaceRegionId()+"&viewId="+item.getViewId()+"&viewName="+item.getViewName()+"&destination="+MagicDimension.Destination.FOR_BUTTON.getCode());
		return mode;
	}
	@RequestMapping(value = "/uploadCustomerPage", method = RequestMethod.GET)
	protected void uploadCustomerPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String viewId = request.getParameter("viewId");
			MagicSpaceRegionView view = service.getViewById(viewId);
			ServiceUtil.notNull(viewId, "viewId is null");
			request.setCharacterEncoding("utf-8");
	        response.setCharacterEncoding("utf-8");
	        List<String> fileList = new ArrayList<String>();
	        //存储路径
	        String savePath = request.getServletContext().getRealPath("uploadFile");
	        File file = new File(savePath);
	        if(!file.exists()){
	        	file.mkdir();
	        	
	        }
	        //获取上传的文件集合
	        Collection<Part> parts = request.getParts();
	        System.out.println("parts.size():"+parts.size());
			// 一次性上传多个文件
	        String text = "";
	        String fileName = null;
			for (Part part : parts) {// 循环处理上传的文件
				// 获取请求头，请求头的格式：form-data; name="file"; filename="snmp4j--api.zip"
				String header = part.getHeader("content-disposition");
				String content = part.getContentType();
				// 获取文件名
				fileName = getFileName(header);
				System.out.println(fileName);
				System.out.println(content);
				// 把文件写到指定路径
				if (fileName != null && !"".equals(fileName)) {
					part.write(savePath + File.separator + fileName);
					fileList.add("uploadFile" + File.separator + fileName);
					text = ConvertMhtToHtml.mht2html(savePath+ File.separator +fileName,
							savePath+ File.separator +fileName.replaceAll("mht", "html"),fileName.replaceAll("mht", "html")); 
				}
			}
			if(StringUtils.isNotEmpty(fileName)) {
				view.setCutomerPageName(fileName);
				service.saveSpaceRegionView(view);
			}
	        PrintWriter out = response.getWriter();
	        out.write(text);
	        out.flush();
	        out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	 /**
     * 根据请求头解析出文件名
     * 请求头的格式：火狐和google浏览器下：form-data; name="file"; filename="snmp4j--api.zip"
     *                 IE浏览器下：form-data; name="file"; filename="E:\snmp4j--api.zip"
     * @param header 请求头
     * @return 文件名
     */
    public String getFileName(String header) {
        /**
         * String[] tempArr1 = header.split(";");代码执行完之后，在不同的浏览器下，tempArr1数组里面的内容稍有区别
         * 火狐或者google浏览器下：tempArr1={form-data,name="file",filename="snmp4j--api.zip"}
         * IE浏览器下：tempArr1={form-data,name="file",filename="E:\snmp4j--api.zip"}
         */
    	if(!header.contains("filename")){
    		return null;
    	}
    	
        String tempArr1 = ("\u0000"+header.replaceAll("filename=", "\u0001")).replaceAll("\u0000[a-zA-Z\\\"\\;\\-= ]{1,}\u0001", "");
		tempArr1=tempArr1.replaceAll("\"", "").replaceAll("\\&\\#", "");
		tempArr1=tempArr1.replaceAll(" ", "");
        
        
        System.out.println("tempArr1:"+tempArr1);
        String fileName = tempArr1;
        if ((fileName == null || "".equals(fileName))) {  
        	return null;  
        }
        System.out.println("fileName:"+fileName);
        return fileName+".mht";
    }
    
    public String getRealName(String header) {
        /**
         * String[] tempArr1 = header.split(";");代码执行完之后，在不同的浏览器下，tempArr1数组里面的内容稍有区别
         * 火狐或者google浏览器下：tempArr1={form-data,name="file",filename="snmp4j--api.zip"}
         * IE浏览器下：tempArr1={form-data,name="file",filename="E:\snmp4j--api.zip"}
         */
    	if(!header.contains("filename")){
    		return null;
    	}
    	
        String tempArr1 = ("\u0000"+header.replaceAll("filename=", "\u0001")).replaceAll("\u0000[a-zA-Z\\\"\\;\\-= ]{1,}\u0001", "");
		tempArr1=tempArr1.replaceAll("\"", "").replaceAll("\\&\\#", "\\u");
		tempArr1=tempArr1.replaceAll(" ", "").replaceAll("\\.mht", "");
        
        
        System.out.println("getRealName:"+tempArr1);
        String fileName = tempArr1;
        if ((fileName == null || "".equals(fileName))) {  
        	return null;  
        }
        System.out.println("fileName:"+fileName);
        return fileName;
    }
}
