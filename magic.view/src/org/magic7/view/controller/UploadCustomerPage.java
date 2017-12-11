package org.magic7.view.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringUtils;
import org.magic7.core.domain.MagicSpaceRegionView;
import org.magic7.core.service.MagicService;
import org.magic7.core.service.MagicServiceFactory;
import org.magic7.utils.ConvertMhtToHtml;
import org.magic7.utils.ServiceUtil;


@WebServlet(urlPatterns = {"/magic/admin/uploadCustomerPage.do"},asyncSupported = true)
@MultipartConfig
public class UploadCustomerPage extends HttpServlet {
	private static final long serialVersionUID = 8975093576130821180L;
	private static final MagicService service =  MagicServiceFactory.getMagicService();
	private static final String suffix = "jsp";
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String viewId = request.getParameter("viewId");
			ServiceUtil.notNull(viewId, "viewId is null");
			MagicSpaceRegionView view = service.getViewById(viewId);
			request.setCharacterEncoding("utf-8");
	        response.setCharacterEncoding("utf-8");
	        List<String> fileList = new ArrayList<String>();
	        String savePath = request.getServletContext().getRealPath("uploadFile");
	        File file = new File(savePath);
	        if(!file.exists()){
	        	file.mkdir();
	        }
	        String fileName = null;
	        String prefix = request.getContextPath();
	        Part part = request.getPart("uploadFile");
			if ( part !=null) {
				String header = part.getHeader("content-disposition");
				fileName = getFileName(header);
				if (fileName != null && !"".equals(fileName)) {
					part.write(savePath + File.separator + fileName);
					fileList.add("uploadFile" + File.separator + fileName);
					ConvertMhtToHtml.mht2html(savePath+ File.separator +fileName,
							savePath+ File.separator +fileName.replaceAll("mht", suffix),fileName.replaceAll("mht", suffix),prefix); 
				}
			}
			if(StringUtils.isNotEmpty(fileName)) {
				view.setCustomerPageName(fileName.replaceAll("mht", suffix));
				service.saveSpaceRegionView(view);
			}
			response.sendRedirect("showView?viewId="+view.getId()+"&spaceName="+view.getSpaceName()+"&spaceId="+view.getSpaceId()+"&regionId="+view.getSpaceRegionId()+"&regionName="+view.getSpaceRegionName());
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
        String fileName = tempArr1;
        if ((fileName == null || "".equals(fileName))) {  
        	return null;  
        }
        return fileName;
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
        
        String fileName = tempArr1;
        if ((fileName == null || "".equals(fileName))) {  
        	return null;  
        }
        return fileName;
    }
    
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
}
