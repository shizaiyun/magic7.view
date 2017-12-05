package org.magic7.view.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.magic7.core.domain.MagicChoiceItem;
import org.magic7.core.domain.MagicDimension;
import org.magic7.core.domain.MagicDimension.Destination;
import org.magic7.core.domain.MagicDimension.PageType;
import org.magic7.core.domain.MagicDimension.QueryType;
import org.magic7.core.domain.MagicRegionRow;
import org.magic7.core.domain.MagicSpace;
import org.magic7.core.domain.MagicSpaceRegion;
import org.magic7.core.domain.MagicSpaceRegion.RegionType;
import org.magic7.core.domain.MagicSpaceRegionView;
import org.magic7.core.domain.MagicSpaceRegionViewItem;
import org.magic7.core.domain.MagicSuperRowItem;
import org.magic7.core.service.MagicService;
import org.magic7.core.service.MagicServiceFactory;
import org.magic7.core.service.MagicSpaceHandler;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

public class MagicTagUtil {
	public static MagicService service = MagicServiceFactory.getMagicService();
	public static String getMagicRegionView(String space, String queryView,Map<String, Object> parmMap) {
		StringBuffer html = new StringBuffer("<div id=\"queryContent\">");
		MagicSpace magicSpace = service.getSpaceByName(space);
		html.append("<div class=\"queryArea_title\">"+magicSpace.getDescription()+"</div>");
			if(StringUtils.isNotBlank(queryView)) {
				MagicSpaceRegionView  listRegion= service.getSpaceRegionView(space, null, queryView);
				Assert.notNull(listRegion," can not find region by queryView:"+queryView+",please check!!");
				String region = listRegion.getSpaceRegionName();
				List<MagicSpaceRegionViewItem> items =service.listSpaceRegionViewItem(space, region, queryView, " seq ");
				html.append(assembleRegionSingleForQuery(items,parmMap));
			}else {
				List<MagicSpaceRegion> regions = service.listSpaceRegion(space, magicSpace.getId(), " seq", 0, 1000);
				String region = null;
				for (MagicSpaceRegion magicSpaceRegion : regions) {
					MagicSpaceRegion.RegionType regionType = MagicSpaceRegion.RegionType.getRegionType(magicSpaceRegion.getRegionType());
					if(regionType == RegionType.MAIN) {
						region = magicSpaceRegion.getName();
						break;
					}
				}
				Assert.notNull(region," can not find main region by space:"+space+",please check!!");
				List<MagicDimension> dimensions  = MagicSpaceHandler.listDimension(space, region, null,null,Destination.FOR_QUERY.getCode());
				html.append(assembleRegionSingleWithoutViewForQuery(dimensions,parmMap));
			}
			html.append("</div><div style=\"text-align: center;\"><input style=\"margin-right: 5px;\" class=\"button\" type=\"button\" value=\"查询\" onclick=\"loadGridTable(1)\"><input class=\"button\" type=\"button\" value=\"重置\" onclick=\"resetForm()\"></div>");
		return html.toString();
	}
	
	private static String assembleRegionSingleWithoutViewForQuery(List<MagicDimension> dimensions,Map<String, Object> parmMap) {
		StringBuffer html = new StringBuffer();
		if(!CollectionUtils.isEmpty(dimensions)) {
			MagicSpaceRegion magicSpaceRegion  = service.getSpaceRegion(dimensions.get(0).getSpaceName(), dimensions.get(0).getSpaceRegionName());
			Integer lineItemCount = magicSpaceRegion.getDimensionNum()==null?3:magicSpaceRegion.getDimensionNum();
			Integer itemCount = 1;
			for (MagicDimension dimension : dimensions) {
				if(dimension.getVisible()!=null &&!dimension.getVisible()) {
					continue;
				}
				String region = dimension.getSpaceRegionName();
				String displayName = dimension.getDisplayName();
				String value = null;
				if(parmMap.get(displayName)!=null) {
					value = parmMap.get(displayName).toString();
				}
				String input = getInput(region+"_"+displayName, displayName, value, null,dimension,Destination.FOR_QUERY);
				html.append("<div class=\"item\"><span class=\"title\">"+dimension.getDescription()+":</span><span class=\"content\">"+input+"</span></div>");
				if(itemCount%lineItemCount==0 && itemCount != dimensions.size()) {
					html.append("<br>");
				}
				itemCount++;
			}
		
		}
		return html.toString();
	}
	
	private static String assembleRegionSingleForQuery(List<MagicSpaceRegionViewItem> viewItems, Map<String, Object> parmMap) {
		StringBuffer html = new StringBuffer();
		if(!CollectionUtils.isEmpty(viewItems)) {
			MagicSpaceRegion magicSpaceRegion  = service.getSpaceRegion(viewItems.get(0).getSpaceName(), viewItems.get(0).getSpaceRegionName());
			Integer lineItemCount = magicSpaceRegion.getDimensionNum()==null?3:magicSpaceRegion.getDimensionNum();
			Integer itemCount = 1;
			for (MagicSpaceRegionViewItem viewItem : viewItems) {
				if(viewItem.getVisible()!=null &&!viewItem.getVisible()) {
					continue;
				}
				String region = viewItem.getSpaceRegionName();
				MagicDimension dimension = service.getDimensionById(viewItem.getDimensionId());
				String displayName = dimension.getDisplayName();
				String value = null;
				if(parmMap.get(displayName)!=null) {
					value = parmMap.get(displayName).toString();
				}
				String input =  getInput(region+"_"+displayName, displayName, value, viewItem,dimension,Destination.FOR_QUERY);
				html.append("<div class=\"item\"><span class=\"title\">"+viewItem.getName()+":</span><span class=\"content\">"+input+"</span></div>");
				if(itemCount%lineItemCount==0 && itemCount != viewItems.size()) {
					html.append("<br>");
				}
				itemCount++;
			}
		}
		return html.toString();
	}

	

	public static String getMagicListView(String space,  String listView,List<MagicRegionRow> rows, Integer destination) {
		StringBuffer html = new StringBuffer("<div style=\"text-align: right;padding-right: 10px;padding-bottom:5px\"><input class=\"button\" type=\"button\" value=\"新增\" onclick=\"addItem()\"></div><table class=\"gridTable\" style=\"width: 100%\" id=\"gridTable\">");
		MagicSpaceRegionView  listRegion= service.getSpaceRegionView(space, null, listView);
		Assert.notNull(listRegion," can not find region by mainlistView,please check!!");
		String region = listRegion.getSpaceRegionName();
		List<MagicSpaceRegionViewItem> viewItems =service.listSpaceRegionViewItem(space, region, listView, " seq ");
		if(!CollectionUtils.isEmpty(viewItems)) {
			html.append("<thead><tr id=\"title\">");
			for (MagicSpaceRegionViewItem viewItem : viewItems) {
				if(viewItem.getVisible()!=null &&!viewItem.getVisible()) {
					continue;
				}
				MagicDimension dimension = service.getDimensionById(viewItem.getDimensionId());
				html.append("<th id=\""+dimension.getDisplayName()+"\" name=\""+dimension.getDisplayName()+"\">"+viewItem.getName()+"</th>");
			}
			html.append("<th style=\"width:10px\">操作</th></tr></thead>");
			
			if(rows!=null) {
				for (MagicRegionRow row : rows) {
					List<MagicSuperRowItem> rowItems = row.getRowItems();
					if(rowItems == null || rowItems.size()==0) {
						continue;
					}
					html.append("<tr id=\""+row.getObjectId()+"\" ondblclick=\"modifyItem("+row.getObjectId()+")\">");
					for (MagicSpaceRegionViewItem viewItem : viewItems) { 
						if(viewItem.getVisible()!=null &&!viewItem.getVisible()) {
							continue;
						}
						for (MagicSuperRowItem rowItem : rowItems) {
							if(viewItem.getDimensionId().equals(rowItem.getDimensionId())) {
								String value = getValue(rowItem);
								MagicDimension dimension = service.getDimensionById(viewItem.getDimensionId());
								MagicDimension.PageType  pageType =  MagicDimension.PageType.getPageType(dimension.getPageType());
								if(StringUtils.isNotBlank(value)) {
									if(pageType==PageType.DROP_DOWN_LIST) {
										List<MagicChoiceItem> magicChoiceItems =  service.listChoiceItem(null,dimension.getChoiceCode());
										for (MagicChoiceItem magicChoiceItem : magicChoiceItems) {
											if(value.equals(magicChoiceItem.getValueCode())) {
												value=magicChoiceItem.getValueName();
												break;
											}
										}
									}
									
								}
								html.append("<td id=\""+rowItem.getRowId()+"_"+rowItem.getDisplayName()+"\">"+value+"</td>");
							}
						}
					}
					html.append("<td style=\"width:10px\"><input type=\"button\" value=\"删除\" onclick=\"deleteItem("+row.getObjectId()+")\"></td></tr>");
				}
			}
		}
		html.append("</table><div style=\"margin-top: 5px\" class=\"M-box\" align=\"center\"></div>");
		return html.toString();
	}
	
	public static String getMagicDetail(String contextPath,String space,String objectId, String mainlistView, String mainViewAndMainButtonView, String regionViewAndRegionButtonView) {
		List<Map<String, Object>> tabRegionAttrs = new ArrayList<>();
		if(StringUtils.isNotBlank(regionViewAndRegionButtonView)) {
			String[] ereryRegionAttrs = regionViewAndRegionButtonView.split(",");
			for (String ereryRegionAttr : ereryRegionAttrs) {
				String[] regionAttrs = ereryRegionAttr.split(":");
				if(regionAttrs!=null && regionAttrs.length==2) {
					MagicSpaceRegionView  tabRegion= service.getSpaceRegionView(space, null, regionAttrs[0]);
					Assert.notNull(tabRegion," can not find region by regionView:"+regionAttrs[0]+",please check!!");
					String regionName = tabRegion.getSpaceRegionName();
					Map<String,Object> map =  new HashMap<>();
					map.put("regionName", regionName);
					map.put("view", regionAttrs[0]);
					map.put("buttonView", regionAttrs[1]);
					tabRegionAttrs.add(map);
				}
			}
		}
		
		
		StringBuffer html = new StringBuffer();
		MagicSpace magicSpace = service.getSpaceByName(space);
		List<Map<String,Object>> tabRegions = new ArrayList<>();
		List<MagicSpaceRegion> spaceRegions = service.listSpaceRegion(space,magicSpace.getId(), " seq ", 0, 1000);
		MagicSpaceRegion mainRegion = null;
		for (MagicSpaceRegion magicSpaceRegion : spaceRegions) {
			MagicSpaceRegion.RegionType regionType = MagicSpaceRegion.RegionType.getRegionType(magicSpaceRegion.getRegionType());
			if(regionType == MagicSpaceRegion.RegionType.MAIN) {
				mainRegion = magicSpaceRegion;
			}else if(regionType == MagicSpaceRegion.RegionType.TAB)  {
				for (Map<String, Object> map : tabRegionAttrs) {
					if(map.get("regionName").toString().equals(magicSpaceRegion.getName())) {
						Map<String,Object> tab = new HashMap<>();
						tab.put("region", magicSpaceRegion);
						tab.putAll(map);
						tabRegions.add(tab);
					}
				}
				
			}
		}
		
		
		if(mainRegion!=null) {
			String view = mainlistView;
			String buttonView = StringUtils.EMPTY;
			if(StringUtils.isNotBlank(mainViewAndMainButtonView)) {
				String[] mainAttrs = mainViewAndMainButtonView.split(":");
				if(mainAttrs!=null && mainAttrs.length==2) {
					view = mainAttrs[0];
					buttonView = mainAttrs[1];
				}
			}
			html.append("<div class=\"mainArea_title\">"+mainRegion.getDescription()+"</div><div><iframe src=\""+contextPath+"/magic/showTabDetail?space="+mainRegion.getSpaceName()+"&region="+mainRegion.getName()+"&objectId="+objectId+"&view="+view+"&buttonView="+buttonView+"\" frameborder=\"0\" scrolling=\"yes\" style=\"height: 200px;width:100%\"  ></iframe></div>");
		}
		
		MagicSpace.TabLayout layout = MagicSpace.TabLayout.getTabLayout(magicSpace.getTabLayout());
		if(layout == MagicSpace.TabLayout.HORIZONTAL) {
			html.append(assembleRegionTabHorizontal(tabRegions,contextPath,objectId));
		}else if(layout == MagicSpace.TabLayout.VERTICAL) {
			html.append(assembleRegionTabVertical(tabRegions,contextPath,objectId));
		}
		return html.toString();
	}
	
	public static String getMagicRegion(String space, String region, String view,String buttonView,String objectId) {
		MagicDimension.Destination destination = MagicDimension.Destination.FOR_DATA;
		Boolean multiply = MagicSpaceHandler.isMultiply(space, region);
		List<MagicSpaceRegionViewItem> items =service.listSpaceRegionViewItem(space, region, view, " seq ");
		StringBuffer html = new StringBuffer();
		if(multiply) {
			List<MagicRegionRow> rows = MagicSpaceHandler.listRow(space, region, null, null, objectId, true, null, null, 0, 1000);
			html.append(assembleRegionMultiply(items,rows));
		}else {
			MagicRegionRow row = null;
			List<MagicRegionRow> rows = new ArrayList<>();
			rows = MagicSpaceHandler.listRow(space, region, null, null, objectId, true, null, null, 0, 1000);
			if(rows!=null && rows.size()>0) {
				row= rows.get(0);
			}else {
				rows = MagicSpaceHandler.listRow(space, region, null, null, objectId, false, null, null, 0, 1000);
				if(rows!=null && rows.size()>0) {
					row= rows.get(0);
				}
			}
			html.append(assembleRegionSingle(items,destination,row));
		}
		return html.toString();
	}
	
	
	private static String assembleRegionTabHorizontal(List<Map<String,Object>> tabRegions,String  contextPath,String objectId) {
		StringBuffer html = new StringBuffer();
		if(!CollectionUtils.isEmpty(tabRegions)) {
			html.append("<div  class=\"tab-head\">");
			Boolean firstTab = true;
			for (Map<String, Object> map : tabRegions) {
				MagicSpaceRegion magicSpaceRegion = (MagicSpaceRegion) map.get("region");
				String view = map.get("view").toString();
				String buttonView = map.get("buttonView").toString();
				if(firstTab) {
					html.append("<h2 id=\"tab_"+magicSpaceRegion.getName()+"\" onclick=\"changeTab(this,'"+magicSpaceRegion.getSpaceName()+"','"+magicSpaceRegion.getName()+"','"+view+"','"+buttonView+"',"+objectId+")\" class=\"selected\">"+magicSpaceRegion.getDescription()+"</h2>");
				}else {
					html.append("<h2 id=\"tab_"+magicSpaceRegion.getName()+"\" onclick=\"changeTab(this,'"+magicSpaceRegion.getSpaceName()+"','"+magicSpaceRegion.getName()+"','"+view+"','"+buttonView+"',"+objectId+")\">"+magicSpaceRegion.getDescription()+"</h2>");
				}
				firstTab = false;
			}
			html.append("</div>");
			html.append("<div style=\"clear: both;\"></div><div class=\"tab-content\">");
			Boolean firsContent = true;
			for (Map<String, Object> map : tabRegions) {
				MagicSpaceRegion tabRegion = (MagicSpaceRegion) map.get("region");
				String view = map.get("view").toString();
				String buttonView = map.get("buttonView").toString();
				if(firsContent) {
					html.append("<div class=\"show\"><iframe id=\"tabContent_"+tabRegion.getName()+"\" src=\""+contextPath+"/magic/showTabDetail?space="+tabRegion.getSpaceName()+"&region="+tabRegion.getName()+"&view="+view+"&buttonView="+buttonView+"&objectId="+objectId+"\" frameborder=\"0\" scrolling=\"yes\" style=\"height: 300px;width:100%\"  ></iframe></div>");
				}else {
					html.append("<div><iframe id=\"tabContent_"+tabRegion.getName()+"\" src=\"\" frameborder=\"0\" scrolling=\"yes\" style=\"height: 300px;width:100%\"  ></iframe></div>");
				}
				firsContent = false;
			}
			html.append("</div>");
		}
		return html.toString();
	}
	
	private static String assembleRegionTabVertical(List<Map<String,Object>> tabRegions,String  contextPath,String objectId) {
		StringBuffer html = new StringBuffer();
		if(!CollectionUtils.isEmpty(tabRegions)) {
			html.append("<div id=\"lib_Tab\" class=\"lib_tab_class\"><div class=\"lib_Menu \"><ul>");
			
			Boolean firstTab = true;
			for (Map<String, Object> map : tabRegions) {
				MagicSpaceRegion magicSpaceRegion = (MagicSpaceRegion) map.get("region");
				String view = map.get("view").toString();
				String buttonView = map.get("buttonView").toString();
				if(firstTab) {
					html.append("<li  onclick=\"changeVerticalTab(this,'"+magicSpaceRegion.getSpaceName()+"','"+magicSpaceRegion.getName()+"','"+view+"','"+buttonView+"',"+objectId+")\" class=\"hover\">"+magicSpaceRegion.getDescription()+"</li>");
				}else {
					html.append("<li  onclick=\"changeVerticalTab(this,'"+magicSpaceRegion.getSpaceName()+"','"+magicSpaceRegion.getName()+"','"+view+"','"+buttonView+"',"+objectId+")\">"+magicSpaceRegion.getDescription()+"</li>");
				}
				firstTab = false;
			}
			html.append("</ul></div><div class=\"lib_Content \">");
			Boolean firsContent = true;
			for (Map<String, Object> map : tabRegions) {
				MagicSpaceRegion tabRegion = (MagicSpaceRegion) map.get("region");
				String view = map.get("view").toString();
				String buttonView = map.get("buttonView").toString();
				if(firsContent) {
					html.append("<div><iframe id=\"tabContent_"+tabRegion.getName()+"\" src=\""+contextPath+"/magic/showTabDetail?space="+tabRegion.getSpaceName()+"&region="+tabRegion.getName()+"&view="+view+"&buttonView="+buttonView+"&objectId="+objectId+"\" frameborder=\"0\" scrolling=\"yes\" style=\"height: 300px;width:100%\"  ></iframe></div>");
				}else {
					html.append("<div  style=\"display:none\"><iframe id=\"tabContent_"+tabRegion.getName()+"\" src=\"\" frameborder=\"0\" scrolling=\"yes\" style=\"height: 300px;width:100%\"  ></iframe></div>");
				}
				firsContent = false;
			}
			html.append("</div></div>");
		}
		return html.toString();
	}
	
	
	private static String assembleRegionSingle(List<MagicSpaceRegionViewItem> items,MagicDimension.Destination destination,MagicRegionRow row) {
		StringBuffer html = new StringBuffer();
		if(!CollectionUtils.isEmpty(items)) {
			MagicSpaceRegion magicSpaceRegion  = service.getSpaceRegion(items.get(0).getSpaceName(), items.get(0).getSpaceRegionName());
			
			if(destination==MagicDimension.Destination.FOR_DATA) {
				MagicSpaceRegion.RegionType regionType = MagicSpaceRegion.RegionType.getRegionType(magicSpaceRegion.getRegionType());
				if(regionType == MagicSpaceRegion.RegionType.TAB)  {
					html.append("<div  class=\"toolbar\"><span class=\"toobar_title\">"+magicSpaceRegion.getDescription()+"</span><span class=\"toobar_button\"><input class=\"button\" type=\"button\" value=\"保存\" onclick=\"saveItem()\"/></span></div>");
				}
			}
			
			Integer lineItemCount = magicSpaceRegion.getDimensionNum()==null?3:magicSpaceRegion.getDimensionNum();
			Integer itemCount = 1;
			if(row!=null) {
				html.append("<input type=\"hidden\" id=\""+items.get(0).getSpaceRegionName()+"_rowId\" name=\"rowId\" value=\""+row.getId()+"\" >");
			}
			for (MagicSpaceRegionViewItem viewItem : items) {
				if(viewItem.getVisible()!=null &&!viewItem.getVisible()) {
					continue;
				}
				MagicDimension dimension = service.getDimensionById(viewItem.getDimensionId());
				String region = dimension.getSpaceRegionName();
				String displayName = dimension.getDisplayName();
				String input = StringUtils.EMPTY;
				if(row!=null) {
					MagicSuperRowItem rowItem = MagicSpaceHandler.getRowItemFromRow(row, displayName);
					input = getInput(region+"_"+row.getId()+"_"+displayName, displayName, getValue(rowItem), viewItem,dimension,destination);
				}else {
					input = getInput(region+"_"+displayName, displayName, null, viewItem,dimension,destination);
				}
				html.append("<div class=\"item\"><span class=\"title\">"+viewItem.getName()+":</span><span class=\"content\">"+input+"</span></div>");
				if(itemCount%lineItemCount==0 && itemCount != items.size()) {
					html.append("<br>");
				}
				itemCount++;
			}
			if(destination==MagicDimension.Destination.FOR_DATA) {
				MagicSpaceRegion.RegionType regionType = MagicSpaceRegion.RegionType.getRegionType(magicSpaceRegion.getRegionType());
				if(regionType == MagicSpaceRegion.RegionType.MAIN) {
					html.append("<div style=\"text-align: center;padding-right: 10px\"><input class=\"button\" type=\"button\" value=\"保存\" onclick=\"saveItem()\"/><input class=\"button\" type=\"button\" value=\"提交\" onclick=\"submitItem()\"/><input class=\"button\" type=\"button\" value=\"删除\" onclick=\"deleteItem()\"/><input class=\"button\" type=\"button\" value=\"返回\" onclick=\"closeDialog()\"/></div>");
				}
			}
		}
		return html.toString();
	}


	private static String getValue(MagicSuperRowItem rowItem) {
		String value = StringUtils.EMPTY;
		MagicDimension.ValueType valueType = MagicDimension.ValueType.getValueType(rowItem.getValueType());
		switch (valueType) {
		case STR_VALUE:
			if(rowItem.getStrValue()!=null) {
				value = rowItem.getStrValue();
			}
			break;
		case DATE_VALUE:
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(rowItem.getDateValue() !=null) {
				value = sdf.format(rowItem.getDateValue());
			}
			break;
		case NUM_VALUE:
			if(rowItem.getNumValue()!=null) {
				value = rowItem.getNumValue().toString();
			}
			break;
		case BOOLEAN_VALUE:
			if(rowItem.getBooleanValue()!=null) {
				if(rowItem.getBooleanValue()) {
					value="1";
				}else {
					value="0";
				}
			}
			break;
		case LIST_STR_VALUE:
			if(rowItem.getListStrValue()!=null) {
				value = rowItem.getListStrValue();
			}
			break;
		default:
			value = StringUtils.EMPTY;
			break;
		}
		return value;
	}
	
	private static Object assembleRegionMultiply(List<MagicSpaceRegionViewItem> items,List<MagicRegionRow> rows) {
		MagicDimension.Destination destination=MagicDimension.Destination.FOR_DATA;
		StringBuffer html = new StringBuffer();
		if(!CollectionUtils.isEmpty(items)) {
			MagicSpaceRegion region = service.getSpaceRegion(items.get(0).getSpaceName(), items.get(0).getSpaceRegionName());
			html.append("<div class=\"toolbar\"><span class=\"toobar_title\">"+region.getDescription()+"</span><span class=\"toobar_button\"><input class=\"button\" type=\"button\" value=\"新增\" onclick=\"addRow()\" /><input class=\"button\" type=\"button\" value=\"删除\" onclick=\"deleteRow()\" /><input class=\"button\" type=\"button\" value=\"保存\" onclick=\"saveRows()\"/></span></div>");
			html.append("<table class=\"gridTable\" style=\"width: 100%\" id=\"gridTable\">");
			html.append("<thead><tr id=\"title\"><th style=\"width: 10px;text-align: center;\"><input id=\"checkAll\"  type=\"checkbox\" onclick=\"checkAll()\" /></th>");
			for (MagicSpaceRegionViewItem viewItem : items) {
				if(viewItem.getVisible()!=null &&!viewItem.getVisible()) {
					continue;
				}
				String requiredStr = StringUtils.EMPTY;
				if(viewItem.getRequired()!=null && viewItem.getRequired()) {
					requiredStr="<span style=\"color:red\">*</span>";
				}
				MagicDimension dimension = service.getDimensionById(viewItem.getDimensionId());
				html.append("<th id=\""+dimension.getDisplayName()+"\" name=\""+dimension.getDisplayName()+"\">"+requiredStr+viewItem.getName()+"</th>");
			}
			html.append("</tr>");
			html.append("<tr id=\"hiddenTr\" class=\"hiddenTr_displayNone\"><td style=\"width: 10px;text-align: center;\"><input type=\"checkbox\" name=\"rowId\" value=\"\" /></td>");
			for (MagicSpaceRegionViewItem viewItem : items) {
				if(viewItem.getVisible()!=null &&!viewItem.getVisible()) {
					continue;
				}
				MagicDimension dimension = service.getDimensionById(viewItem.getDimensionId());
				String input = getInput(dimension.getDisplayName(), dimension.getDisplayName(), null, viewItem,dimension,destination);
				html.append("<td  name=\""+dimension.getDisplayName()+"\">"+input+"</td>");
			}
			html.append("</tr></thead>");
			
			if(!CollectionUtils.isEmpty(rows)) {
				for (MagicRegionRow row : rows) {
					html.append("<tr><td style=\"width: 10px;text-align: center;\"><input  type=\"checkbox\" name=\"rowId\" value=\""+row.getId()+"\" /></td>");
					for (MagicSpaceRegionViewItem viewItem : items) {
						if(viewItem.getVisible()!=null &&!viewItem.getVisible()) {
							continue;
						}
						MagicDimension dimension = service.getDimensionById(viewItem.getDimensionId());
						String displayName = dimension.getDisplayName();
						MagicSuperRowItem rowItem = MagicSpaceHandler.getRowItemFromRow(row, displayName);
						String input = getInput(row.getRegionName()+"_"+row.getId()+"_"+displayName, displayName, getValue(rowItem),viewItem, dimension,destination);
						html.append("<td>"+input+"</td>");
					}
					html.append("</tr>");
				}
			}
			
			html.append("</table>");
		}
		return html.toString();
	}
	
	
	
	private static String getInput(String id,String name,String value,MagicSpaceRegionViewItem viewItem,MagicDimension dimension,MagicDimension.Destination destination) {
		MagicDimension.PageType  pageType =  MagicDimension.PageType.getPageType(dimension.getPageType());
		MagicDimension.ValueType valueType =  MagicDimension.ValueType.getValueType(dimension.getValueType());
		MagicDimension.QueryType  queryType =  MagicDimension.QueryType.getQueryType(dimension.getQueryType());
		String choiceCode = dimension.getChoiceCode();
		String url = dimension.getUrl();
		Boolean required = viewItem!=null?viewItem.getRequired():dimension.getRequired();
		Boolean editable = viewItem!=null?viewItem.getEditable():dimension.getEditable();
		String html =StringUtils.EMPTY;
		String valueStr = StringUtils.EMPTY;
		if(value==null) {
			value = StringUtils.EMPTY;
		}
		if(StringUtils.isNotBlank(value)) {
			valueStr = "value=\""+value+"\"";
		}
		String inputClass = StringUtils.EMPTY;
		String requiredStr = StringUtils.EMPTY;
		String readonly = "";
		if(required!=null && required) {
			inputClass =inputClass+" required ";
			requiredStr = "required";
		}
		if(editable!=null && !editable) {
			inputClass =inputClass+" readonly ";
			readonly = "readonly";
		}
		switch (pageType) {
		case TEXT_EDITOR:
			if(MagicDimension.ValueType.DATE_VALUE == valueType) {
				if(editable!=null && !editable) {
					html = "<input type=\"text\" id=\""+id+"\" name=\""+name+"\" class=\"Wdate "+inputClass+" Wdate_readonly\" "+requiredStr+" "+readonly+" "+valueStr+" />";
				}else {
					html = "<input type=\"text\" id=\""+id+"\" name=\""+name+"\" class=\"Wdate "+inputClass+"\" "+requiredStr+" "+readonly+" "+valueStr+" onclick=\"WdatePicker({readOnly:true})\"/>";
				}
			}else {
				html = "<input type=\"text\" id=\""+id+"\" name=\""+name+"\" class=\""+inputClass+"\" "+requiredStr+" "+readonly+" "+valueStr+" />";
			}
			break;
		case DROP_DOWN_LIST: 
			StringBuffer options = new StringBuffer("");
			List<MagicChoiceItem> magicChoiceItems =  service.listChoiceItem(null,choiceCode);
			List<String> values = new ArrayList<>();
			if(StringUtils.isNotBlank(value)) {
				values = Arrays.asList(StringUtils.split(value, ","));
			}
			for (MagicChoiceItem magicChoiceItem : magicChoiceItems) {
				if(values.contains(magicChoiceItem.getValueCode())) {
					options.append("<option value=\""+magicChoiceItem.getValueCode()+"\" selected>"+magicChoiceItem.getValueName()+"</option>");
				}else {
					options.append("<option value=\""+magicChoiceItem.getValueCode()+"\">"+magicChoiceItem.getValueName()+"</option>");
				}
			}
			if(destination==Destination.FOR_QUERY&&queryType==QueryType.IN) {
				html= "<select id=\""+id+"\" name=\""+name+"\" class=\""+inputClass+"\" "+requiredStr+" multiple=\"multiple\" >"+options.toString()+"</select>";
			}else {
				html= "<select id=\""+id+"\" name=\""+name+"\" class=\""+inputClass+"\" "+requiredStr+" ><option></option>"+options.toString()+"</select>";
			}
			break;
		case POP_UP:
			html = "<input type=\"text\" id=\""+id+"\" name=\""+name+"\" class=\""+inputClass+"\" "+requiredStr+" "+valueStr+"  onclick=\"openWin('"+url+"')\" readonly=\"readonly\"/>";
			break;
		case BUTTON:
			html = "<input type=\"button\" id=\""+id+"\" name=\""+name+"\"/>";
			break;
		case TEXT_AREA:
			html = "<textarea id=\""+id+"\" name=\""+name+"\" class=\""+inputClass+"\" "+requiredStr+">"+value +"</textarea>";					
			break;
		default:
			break;
		}
		return html;
	}

}
