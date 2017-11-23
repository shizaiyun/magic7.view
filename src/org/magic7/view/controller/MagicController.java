package org.magic7.view.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.magic7.core.domain.MagicDimension;
import org.magic7.core.domain.MagicObject;
import org.magic7.core.domain.MagicRegionRow;
import org.magic7.core.domain.MagicSuperRowItem;
import org.magic7.core.service.MagicSpaceHandler;
import org.magic7.view.module.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/magic")
public class MagicController {
	private static final Integer PAGE_SIZE = 10;

	/**
	 * 初始化列表页面
	 * 
	 * @param space
	 * @param region
	 * @param queryView
	 *            查询
	 * @param listView
	 * @return 列表页面
	 */
	@RequestMapping(value = "/showList")
	public ModelAndView showList(HttpServletRequest request) {
		String space = request.getParameter("space");
		String region = request.getParameter("region");
		String queryView = request.getParameter("queryView");
		String listView = request.getParameter("listView");

		Integer currentPage = 1;
		String page = request.getParameter("currentPage");
		if (page != null) {
			currentPage = Integer.parseInt(page);
		}
		String size = request.getParameter("pageSize");
		Integer pageSize = PAGE_SIZE;
		if (size != null) {
			pageSize = Integer.parseInt(size);
		}
		Map<String, Object> parmMap = assembleParmMap(request);
		List<MagicDimension> searchCriterias = MagicSpaceHandler.createSearchCriterias(space, region,queryView, parmMap);
		List<MagicRegionRow> rows = MagicSpaceHandler.listRow(space, region, listView, null, null, true,
				searchCriterias, " id ", (currentPage - 1) * pageSize, pageSize);
		Integer totalCount = MagicSpaceHandler.listRowCount(space, region, null, null, true, searchCriterias);

		ModelAndView mode = new ModelAndView();
		mode.addObject("space", space);
		mode.addObject("region", region);
		mode.addObject("queryView", queryView);
		mode.addObject("listView", listView);
		mode.addObject("rows", rows);
		mode.addObject("currentPage", currentPage);
		mode.addObject("pageSize", pageSize);
		mode.addObject("totalCount", totalCount);
		mode.addObject("parmMap",parmMap);
		mode.setViewName("magic/list");
		return mode;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> assembleParmMap(HttpServletRequest request) {
		Map<String, Object> parmMap = new HashMap<>();
		Map<String, Object> requestMap = request.getParameterMap();
		Set<Entry<String, Object>> entrySet = requestMap.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			if (!entry.getKey().equals("space") && !entry.getKey().equals("region")
					&& !entry.getKey().equals("queryView") && !entry.getKey().equals("listView")
					&& !entry.getKey().equals("currentPage") && !entry.getKey().equals("pageSize")) {
				List<String> parms = new ArrayList<>();
				String[] parameterValues = request.getParameterValues(entry.getKey());
				for (String parmeterValue : parameterValues) {
					if(parmeterValue!=null && StringUtils.isNotBlank(parmeterValue)) {
						parms.add(parmeterValue);
					}
				}
				parmMap.put(entry.getKey(), StringUtils.join(parms, ","));
			}
		}
		return parmMap;
	}

	/**
	 * 初始化详细页面 没有objct就根据space创建一个，有object就直接加载
	 * 
	 * @param space
	 * @param objectId
	 * @return 详细页面
	 */
	@RequestMapping(value = "/showDetail", method = RequestMethod.GET)
	public ModelAndView showDetail(@RequestParam(value = "space") String space,
			@RequestParam(value = "objectId") String objectId) {
		if (StringUtils.isBlank(objectId)) {
			MagicObject magicObject = MagicSpaceHandler.createMagicObjectBySpace(space);
			objectId = magicObject.getId();
		} else {
			MagicSpaceHandler.createSupplementMagicObject(objectId);
		}
		ModelAndView mode = new ModelAndView();
		mode.addObject("space", space);
		mode.addObject("objectId", objectId);
		mode.setViewName("magic/detail");
		return mode;
	}

	/**
	 * 详细页面下方的tab页面初始化
	 * 
	 * @param space
	 * @param region
	 * @param objectId
	 * @return tab页面
	 */
	@RequestMapping(value = "/showTabDetail", method = RequestMethod.GET)
	public ModelAndView showTabDetail(@RequestParam(value = "space") String space,
			@RequestParam(value = "region") String region, @RequestParam(value = "objectId") String objectId) {
		ModelAndView mode = new ModelAndView();
		mode.addObject("space", space);
		mode.addObject("region", region);
		mode.addObject("objectId", objectId);
		mode.addObject("destination", MagicDimension.Destination.FOR_DATA.getCode());
		Boolean multiply = MagicSpaceHandler.isMultiply(space, region);
		if (multiply) {
			mode.setViewName("magic/tabMutiply");
		} else {
			mode.setViewName("magic/tabSingle");
		}
		return mode;
	}

	/**
	 * 保存单行信息
	 * 
	 * @param parm
	 *            行信息
	 * @return 是否保存成功
	 * @throws Exception
	 *             异常
	 */
	@RequestMapping(value = "/saveRow")
	@ResponseBody
	public ResultBean<Boolean> saveRow(@RequestBody String parm) throws Exception {
		JSONObject rowData = JSONObject.fromObject(parm);
		saveRow(rowData);
		return new ResultBean<Boolean>(true);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/submitRow")
	@ResponseBody
	public ResultBean<Boolean> submitRow(@RequestBody String parm) throws Exception {
		JSONObject rowData = JSONObject.fromObject(parm);
		MagicRegionRow row = MagicSpaceHandler.getRowById(rowData.getString("rowId"));
		Iterator<String> iterator = rowData.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (key.equals("rowId")) {
				continue;
			}
			String value = rowData.getString(key);
			MagicSuperRowItem item = MagicSpaceHandler.getRowItemFromRow(row, key);
			item.setStrValue(value);
		}
		row.setValid(true);
		MagicSpaceHandler.saveRow(row);
		HashMap<String, Object> params = new HashMap<>();
		params.put("status", "Submitted");
		MagicSpaceHandler.executeTrigger(row, "onSubmit", params);
		return new ResultBean<Boolean>(true);
	}

	/**
	 * 删除整个object
	 * 
	 * @param parm
	 *            objectId
	 * @return 是否删除成功
	 * @throws Exception
	 *             异常
	 */
	@RequestMapping(value = "/deleteObject")
	@ResponseBody
	public ResultBean<Boolean> deleteObject(@RequestBody String parm) throws Exception {
		JSONObject requestParm = JSONObject.fromObject(parm);
		return new ResultBean<Boolean>(MagicSpaceHandler.deleteMagicObjectById(requestParm.getString("objectId")));
	}

	/**
	 * 新增一行临时数据
	 * 
	 * @param parm
	 *            space+region+objectId
	 * @return rowId
	 * @throws Exception
	 *             异常
	 */
	@RequestMapping(value = "/addRow")
	@ResponseBody
	public ResultBean<String> addRow(@RequestBody String parm) throws Exception {
		JSONObject requestParm = JSONObject.fromObject(parm);
		String space = requestParm.getString("space");
		String region = requestParm.getString("region");
		String objectId = requestParm.getString("objectId");
		MagicRegionRow row = MagicSpaceHandler.createRow(space, region, objectId, false);
		MagicSpaceHandler.saveRow(row);
		return new ResultBean<String>(row.getId());
	}

	/**
	 * region mutiply=true时候删除所选行
	 * 
	 * @param parm
	 *            需要删除行的rowId
	 * @return 是否删除成功
	 * @throws Exception
	 *             异常
	 */
	@RequestMapping(value = "/deleteRows")
	@ResponseBody
	public ResultBean<Boolean> deleteRows(@RequestBody String parm) throws Exception {
		JSONObject requestParm = JSONObject.fromObject(parm);
		JSONArray rowIds = requestParm.getJSONArray("rowIds");
		for (Object object : rowIds) {
			String rowId = String.valueOf(object);
			MagicSpaceHandler.deleteRow(rowId);
		}
		return new ResultBean<Boolean>(true);
	}

	/**
	 * region mutiply=true时候保存整个列表
	 * 
	 * @param parm
	 *            整个聊表数据信息
	 * @return 是否保存成功
	 * @throws Exception
	 *             异常
	 */
	@RequestMapping(value = "/saveRows")
	@ResponseBody
	public ResultBean<Boolean> saveRows(@RequestBody String parm) throws Exception {
		JSONObject requestParm = JSONObject.fromObject(parm);
		JSONArray rowDatas = requestParm.getJSONArray("rowDatas");
		for (Object object : rowDatas) {
			JSONObject rowData = (JSONObject) object;
			saveRow(rowData);
		}
		return new ResultBean<Boolean>(true);
	}

	@SuppressWarnings("unchecked")
	private void saveRow(JSONObject rowData) throws Exception {
		MagicRegionRow row = MagicSpaceHandler.getRowById(rowData.getString("rowId"));
		Iterator<String> iterator = rowData.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (key.equals("rowId")) {
				continue;
			}
			Object value = null;
			MagicSuperRowItem item = MagicSpaceHandler.getRowItemFromRow(row, key);
			String valueStr = rowData.getString(key);
			if(StringUtils.isNotBlank(valueStr)) {
				MagicDimension.ValueType valueType = MagicDimension.ValueType.getValueType(item.getValueType());
				switch (valueType) {
				case STR_VALUE:
					value = valueStr;
					break;
				case DATE_VALUE:
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					value = sdf.parse(valueStr);
					break;
				case NUM_VALUE:
					value = new BigDecimal(valueStr);
					break;
				case BOOLEAN_VALUE:
					Boolean bool = null;
					if("1".equals(valueStr)) {
						bool = true;
					}else if("0".equals(valueStr)) {
						bool = false;
					}
					value = Boolean.valueOf(bool);
					break;
				case LIST_STR_VALUE:
					value = valueStr;
					break;
				default:
					value = valueStr;
					break;
				}
			}
			MagicSpaceHandler.setRowItemValue(item, value);
		}
		row.setValid(true);
		MagicSpaceHandler.saveRow(row);
		MagicSpaceHandler.executeTrigger(row, "onSave", null);
	}
	
	public static void main(String[] args) {
		BigDecimal a = new BigDecimal("1");
		System.out.println(a.toString());
	}

}
