package org.magic7.view.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.magic7.core.domain.MagicChoiceItem;
import org.magic7.core.domain.MagicDimension;
import org.magic7.core.domain.MagicDimension.Destination;
import org.magic7.core.domain.MagicDimension.PageType;
import org.magic7.core.domain.MagicDimension.QueryType;
import org.magic7.core.domain.MagicRegionRow;
import org.magic7.core.domain.MagicSpace;
import org.magic7.core.domain.MagicSpaceRegion;
import org.magic7.core.domain.MagicSuperRowItem;
import org.magic7.core.service.MagicService;
import org.magic7.core.service.MagicServiceFactory;
import org.magic7.core.service.MagicSpaceHandler;
import org.springframework.util.CollectionUtils;

public class MagicTagUtil {
	public static MagicService service = MagicServiceFactory.getMagicService();
	public static String getMagicRegionView(String space, String region, String view, Integer destination) {
		Boolean multiply = MagicSpaceHandler.isMultiply(space, region);
		List<MagicDimension> dimensions  = MagicSpaceHandler.listDimension(space, region, view,null,destination);
		StringBuffer html = new StringBuffer();
		if(multiply) {
			html.append(assembleRegionMultiply(dimensions,null));
		}else {
			if(StringUtils.isNotBlank(view)) {
				MagicSpace magicSpace = service.getSpaceByName(space);
				html.append("<div class=\"queryArea_title\">"+magicSpace.getDescription()+"</div>");
			}
				html.append(assembleRegionSingle(dimensions,MagicDimension.Destination.getDestination(destination),null));
		}
		return html.toString();
	}
	

	public static String getMagicListView(String space, String region, String view,List<MagicRegionRow> rows, Integer destination) {
		List<MagicDimension> dimensions  = MagicSpaceHandler.listDimension(space, region, view,null,destination);
		StringBuffer html = new StringBuffer();
		if(!CollectionUtils.isEmpty(dimensions)) {
			html.append("<thead><tr id=\"title\">");
			for (MagicDimension dimension : dimensions) {
				html.append("<th id=\""+dimension.getDisplayName()+"\" name=\""+dimension.getDisplayName()+"\">"+dimension.getDescription()+"</th>");
			}
			html.append("<th>操作</th></tr></thead>");
			
			if(rows!=null) {
				for (MagicRegionRow row : rows) {
					html.append("<tr id=\""+row.getObjectId()+"\" ondblclick=\"modifyItem("+row.getObjectId()+")\">");
					List<MagicSuperRowItem> rowItems = row.getRowItems();
					for (MagicDimension dimension : dimensions) {
						for (MagicSuperRowItem rowItem : rowItems) {
							if(dimension.getDisplayName().equals(rowItem.getDisplayName())) {
								String value = rowItem.getStrValue()==null?StringUtils.EMPTY:rowItem.getStrValue();
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
					html.append("<td><input type=\"button\" value=\"删除\" onclick=\"deleteItem("+row.getObjectId()+")\"></td></tr>");
				}
			}
			
		}
		return html.toString();
	}
	
	public static String getMagicDetail(String space,String  contextPath,String objectId) {
		StringBuffer html = new StringBuffer();
		MagicSpace magicSpace = service.getSpaceByName(space);
		List<MagicSpaceRegion> spaceRegions = service.listSpaceRegion(space,magicSpace.getId(), " seq ", 0, 1000);
		MagicSpaceRegion mainRegion = null;
		List<MagicSpaceRegion> tabRegions = new ArrayList<>();
		for (MagicSpaceRegion magicSpaceRegion : spaceRegions) {
			MagicSpaceRegion.RegionType regionType = MagicSpaceRegion.RegionType.getRegionType(magicSpaceRegion.getRegionType());
			if(regionType == MagicSpaceRegion.RegionType.MAIN) {
				mainRegion = magicSpaceRegion;
			}else if(regionType == MagicSpaceRegion.RegionType.TAB)  {
				tabRegions.add(magicSpaceRegion);
			}
		}
		if(mainRegion!=null) {
			html.append("<div class=\"mainArea_title\">"+mainRegion.getDescription()+"</div><div><iframe src=\""+contextPath+"/magic/showTabDetail?space="+mainRegion.getSpaceName()+"&region="+mainRegion.getName()+"&objectId="+objectId+"\" frameborder=\"0\" scrolling=\"yes\" style=\"height: 200px;width:100%\"  ></iframe></div>");
		}
		
		MagicSpace.TabLayout layout = MagicSpace.TabLayout.getTabLayout(magicSpace.getTabLayout());
		if(layout == MagicSpace.TabLayout.HORIZONTAL) {
			html.append(assembleRegionTabHorizontal(tabRegions,contextPath,objectId));
		}else if(layout == MagicSpace.TabLayout.VERTICAL) {
			html.append(assembleRegionTabVertical(tabRegions,contextPath,objectId));
		}
		return html.toString();
	}
	
	public static String getMagicRegion(String space, String region, String objectId) {
		MagicDimension.Destination destination = MagicDimension.Destination.FOR_DATA;
		Boolean multiply = MagicSpaceHandler.isMultiply(space, region);
		List<MagicDimension> dimensions  = MagicSpaceHandler.listDimension(space, region, null,null,destination.getCode());
		StringBuffer html = new StringBuffer();
		if(multiply) {
			List<MagicRegionRow> rows = MagicSpaceHandler.listRow(space, region, null, null, objectId, true, null, null, 0, 1000);
			html.append(assembleRegionMultiply(dimensions,rows));
			
		}else {
			List<MagicRegionRow> rows = MagicSpaceHandler.listRow(space, region, null, null, objectId, true, null, null, 0, 1000);
			if(rows!=null && rows.size()>0) {
				MagicRegionRow row= rows.get(0);
				html.append(assembleRegionSingle(dimensions,destination,row));
			}else {
				html.append(assembleRegionSingle(dimensions,destination,null));
			}
		}
		return html.toString();
	}
	
	
	private static String assembleRegionTabHorizontal(List<MagicSpaceRegion> tabRegions,String  contextPath,String objectId) {
		StringBuffer html = new StringBuffer();
		if(!CollectionUtils.isEmpty(tabRegions)) {
			html.append("<div class=\"tab-head\">");
			Boolean firstTab = true;
			for (MagicSpaceRegion magicSpaceRegion : tabRegions) {
				if(firstTab) {
					html.append("<h2 onclick=\"changeTab(this)\" class=\"selected\">"+magicSpaceRegion.getDescription()+"</h2>");
				}else {
					html.append("<h2 onclick=\"changeTab(this)\">"+magicSpaceRegion.getDescription()+"</h2>");
				}
				firstTab = false;
			}
			html.append("</div>");
			html.append("<div style=\"clear: both;\"></div><div class=\"tab-content\">");
			Boolean firsContent = true;
			for (MagicSpaceRegion tabRegion : tabRegions) {
				if(firsContent) {
					html.append("<div class=\"show\"><iframe src=\""+contextPath+"/magic/showTabDetail?space="+tabRegion.getSpaceName()+"&region="+tabRegion.getName()+"&objectId="+objectId+"\" frameborder=\"0\" scrolling=\"yes\" style=\"height: 300px;width:100%\"  ></iframe></div>");
				}else {
					html.append("<div><iframe src=\""+contextPath+"/magic/showTabDetail?space="+tabRegion.getSpaceName()+"&region="+tabRegion.getName()+"&objectId="+objectId+"\" frameborder=\"0\" scrolling=\"yes\" style=\"height: 300px;width:100%\"  ></iframe></div>");
				}
				firsContent = false;
			}
			html.append("</div>");
		}
		return html.toString();
	}
	
	private static String assembleRegionTabVertical(List<MagicSpaceRegion> tabRegions,String  contextPath,String objectId) {
		StringBuffer html = new StringBuffer();
		if(!CollectionUtils.isEmpty(tabRegions)) {
			html.append("<div id=\"lib_Tab\" class=\"lib_tab_class\"><div class=\"lib_Menu \"><ul>");
			
			Boolean firstTab = true;
			for (MagicSpaceRegion magicSpaceRegion : tabRegions) {
				if(firstTab) {
					html.append("<li  onclick=\"changeVerticalTab(this)\" class=\"hover\">"+magicSpaceRegion.getDescription()+"</li>");
				}else {
					html.append("<li  onclick=\"changeVerticalTab(this)\">"+magicSpaceRegion.getDescription()+"</li>");
				}
				firstTab = false;
			}
			html.append("</ul></div><div class=\"lib_Content \">");
			Boolean firsContent = true;
			for (MagicSpaceRegion tabRegion : tabRegions) {
				if(firsContent) {
					html.append("<div><iframe src=\""+contextPath+"/magic/showTabDetail?space="+tabRegion.getSpaceName()+"&region="+tabRegion.getName()+"&objectId="+objectId+"\" frameborder=\"0\" scrolling=\"yes\" style=\"height: 300px;width:100%\"  ></iframe></div>");
				}else {
					html.append("<div style=\"display:none\"><iframe src=\""+contextPath+"/magic/showTabDetail?space="+tabRegion.getSpaceName()+"&region="+tabRegion.getName()+"&objectId="+objectId+"\" frameborder=\"0\" scrolling=\"yes\" style=\"height: 300px;width:100%\"  ></iframe></div>");
				}
				firsContent = false;
			}
			html.append("</div></div>");
		}
		return html.toString();
	}
	
	
	private static String assembleRegionSingle(List<MagicDimension> dimensions,MagicDimension.Destination destination,MagicRegionRow row) {
		StringBuffer html = new StringBuffer();
		if(!CollectionUtils.isEmpty(dimensions)) {
			MagicSpaceRegion magicSpaceRegion  = service.getSpaceRegion(dimensions.get(0).getSpaceName(), dimensions.get(0).getSpaceRegionName());
			
			if(destination==MagicDimension.Destination.FOR_DATA) {
				MagicSpaceRegion.RegionType regionType = MagicSpaceRegion.RegionType.getRegionType(magicSpaceRegion.getRegionType());
				if(regionType == MagicSpaceRegion.RegionType.TAB)  {
					html.append("<div  class=\"toolbar\"><span class=\"toobar_title\">"+magicSpaceRegion.getDescription()+"</span><span class=\"toobar_button\"><input class=\"button\" type=\"button\" value=\"保存\" onclick=\"saveItem()\"/></span></div>");
				}
			}
			
			Integer lineItemCount = magicSpaceRegion.getDimensionNum()==null?3:magicSpaceRegion.getDimensionNum();
			Integer itemCount = 1;
			if(row!=null) {
				html.append("<input type=\"hidden\" id=\""+dimensions.get(0).getSpaceRegionName()+"_rowId\" name=\"rowId\" value=\""+row.getId()+"\" >");
			}
			for (MagicDimension dimension : dimensions) {
				if(dimension.getVisible()!=null &&!dimension.getVisible()) {
					continue;
				}
				String region = dimension.getSpaceRegionName();
				String displayName = dimension.getDisplayName();
				String input = StringUtils.EMPTY;
				if(row!=null) {
					MagicSuperRowItem rowItem = MagicSpaceHandler.getRowItemFromRow(row, displayName);
					input = getInput(region+"_"+row.getId()+"_"+displayName, displayName, rowItem.getStrValue(), dimension,destination);
				}else {
					input = getInput(region+"_"+displayName, displayName, null, dimension,destination);
				}
				html.append("<div class=\"item\"><span class=\"title\">"+dimension.getDescription()+":</span><span class=\"content\">"+input+"</span></div>");
				if(itemCount%lineItemCount==0 && itemCount != dimensions.size()) {
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
	
	private static Object assembleRegionMultiply(List<MagicDimension> dimensions,List<MagicRegionRow> rows) {
		MagicDimension.Destination destination=MagicDimension.Destination.FOR_DATA;
		StringBuffer html = new StringBuffer();
		if(!CollectionUtils.isEmpty(dimensions)) {
			MagicSpaceRegion region = service.getSpaceRegion(dimensions.get(0).getSpaceName(), dimensions.get(0).getSpaceRegionName());
			html.append("<div class=\"toolbar\"><span class=\"toobar_title\">"+region.getDescription()+"</span><span class=\"toobar_button\"><input class=\"button\" type=\"button\" value=\"新增\" onclick=\"addRow()\" /><input class=\"button\" type=\"button\" value=\"删除\" onclick=\"deleteRow()\" /><input class=\"button\" type=\"button\" value=\"保存\" onclick=\"saveRows()\"/></span></div>");
			html.append("<table class=\"gridTable\" style=\"width: 100%\" id=\"gridTable\">");
			html.append("<thead><tr id=\"title\"><th style=\"width: 10px;text-align: center;\"><input id=\"checkAll\"  type=\"checkbox\" onclick=\"checkAll()\" /></th>");
			for (MagicDimension dimension : dimensions) {
				if(dimension.getVisible()!=null &&!dimension.getVisible()) {
					continue;
				}
				String requiredStr = StringUtils.EMPTY;
				if(dimension.getRequired()) {
					requiredStr="<span style=\"color:red\">*</span>";
				}
				html.append("<th id=\""+dimension.getDisplayName()+"\" name=\""+dimension.getDisplayName()+"\">"+requiredStr+dimension.getDescription()+"</th>");
			}
			html.append("</tr>");
			html.append("<tr id=\"hiddenTr\" class=\"hiddenTr_displayNone\"><td style=\"width: 10px;text-align: center;\"><input type=\"checkbox\" name=\"rowId\" value=\"\" /></td>");
			for (MagicDimension dimension : dimensions) {
				if(dimension.getVisible()!=null &&!dimension.getVisible()) {
					continue;
				}
				String input = getInput(dimension.getDisplayName(), dimension.getDisplayName(), null, dimension,destination);
				html.append("<td  name=\""+dimension.getDisplayName()+"\">"+input+"</td>");
			}
			html.append("</tr></thead>");
			
			if(!CollectionUtils.isEmpty(rows)) {
				for (MagicRegionRow row : rows) {
					html.append("<tr><td style=\"width: 10px;text-align: center;\"><input  type=\"checkbox\" name=\"rowId\" value=\""+row.getId()+"\" /></td>");
					for (MagicDimension dimension : dimensions) {
						if(dimension.getVisible()!=null &&!dimension.getVisible()) {
							continue;
						}
						String displayName = dimension.getDisplayName();
						MagicSuperRowItem rowItem = MagicSpaceHandler.getRowItemFromRow(row, displayName);
						String input = getInput(row.getRegionName()+"_"+row.getId()+"_"+displayName, displayName, rowItem.getStrValue(), dimension,destination);
						html.append("<td>"+input+"</td>");
					}
					html.append("</tr>");
				}
			}
			
			html.append("</table>");
		}
		return html.toString();
	}
	
	
	
	private static String getInput(String id,String name,String value,MagicDimension dimension,MagicDimension.Destination destination) {
		MagicDimension.PageType  pageType =  MagicDimension.PageType.getPageType(dimension.getPageType());
		MagicDimension.ValueType valueType =  MagicDimension.ValueType.getValueType(dimension.getValueType());
		MagicDimension.QueryType  queryType =  MagicDimension.QueryType.getQueryType(dimension.getQueryType());
		String choiceCode = dimension.getChoiceCode();
		String url = dimension.getUrl();
		Boolean required = dimension.getRequired();
		
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
		if(required) {
			inputClass =inputClass+" required ";
			requiredStr = "required";
		}
		switch (pageType) {
		case TEXT_EDITOR:
			if(MagicDimension.ValueType.DATE_VALUE == valueType) {
				html = "<input type=\"text\" id=\""+id+"\" name=\""+name+"\" class=\"Wdate "+inputClass+"\" "+requiredStr+" "+valueStr+" class=\"Wdate\" onclick=\"WdatePicker({readOnly:true})\"/>";
			}else {
				html = "<input type=\"text\" id=\""+id+"\" name=\""+name+"\" class=\""+inputClass+"\" "+requiredStr+" "+valueStr+" />";
			}
			break;
		case DROP_DOWN_LIST:
			StringBuffer options = new StringBuffer("<option></option>");
			List<MagicChoiceItem> magicChoiceItems =  service.listChoiceItem(null,choiceCode);
			for (MagicChoiceItem magicChoiceItem : magicChoiceItems) {
				if(StringUtils.isNotBlank(value) && value.equals(magicChoiceItem.getValueCode())) {
					options.append("<option value=\""+magicChoiceItem.getValueCode()+"\" selected>"+magicChoiceItem.getValueName()+"</option>");
				}
				options.append("<option value=\""+magicChoiceItem.getValueCode()+"\">"+magicChoiceItem.getValueName()+"</option>");
			}
			if(destination==Destination.FOR_QUERY&&queryType==QueryType.IN) {
				html= "<select id=\""+id+"\" name=\""+name+"\" class=\""+inputClass+"\" "+requiredStr+" multiple=\"multiple\" size=\"2\" >"+options.toString()+"</select>";
			}else {
				html= "<select id=\""+id+"\" name=\""+name+"\" class=\""+inputClass+"\" "+requiredStr+" >"+options.toString()+"</select>";
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
