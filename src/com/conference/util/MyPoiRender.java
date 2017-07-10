package com.conference.util;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

@SuppressWarnings("serial")
public class MyPoiRender extends Render {

    protected final Logger logger = Logger.getLogger(getClass());
    private final static String CONTENT_TYPE = "application/msexcel;charset=utf-8";
    private static final String DEFAULT_FILE_NAME = "file1.xls";
    private static final String DEFAULT_SHEET_NAME = "sheet1";

    private List<Object> data;
    private String[] columns;
    private String[] headers;
    private String fileName;
    private String sheetName;

    public MyPoiRender() {
    }

    public MyPoiRender(String fileName, String sheetName, String[] headers, List<Object> data) {
        this.fileName = fileName;
        this.headers = headers;
        this.data = data;
    }

    public MyPoiRender(String fileName, String sheetName, String[] headers, String[] columns, List<Object> data) {
        this.fileName = fileName;
        this.headers = headers;
        this.columns = columns;
        this.data = data;
    }

    public static MyPoiRender excel(List<Object> data, String[] headers) {
        return new MyPoiRender(DEFAULT_FILE_NAME, DEFAULT_SHEET_NAME, headers, data);
    }

    public static MyPoiRender excel(List<Object> data, String[] headers, String[] columns) {
        MyPoiRender render = new MyPoiRender(DEFAULT_FILE_NAME, DEFAULT_SHEET_NAME, headers, data);
        render.setColumns(columns);
        return render;
    }

    public static MyPoiRender excel(List<Object> data, String fileName, String[] headers) {
        return new MyPoiRender(fileName, DEFAULT_SHEET_NAME, headers, data);
    }

    public static MyPoiRender excel(List<Object> data, String fileName, String sheetName, String[] headers) {
        MyPoiRender render = new MyPoiRender(fileName, sheetName, headers, data);
        render.setSheetName(sheetName);
        return render;
    }

    public static MyPoiRender excel(List<Object> data, String fileName, String sheetName, String[] headers,
            String[] columns) {
        MyPoiRender render = new MyPoiRender(fileName, sheetName, headers, data);
        render.setSheetName(sheetName);
        render.setColumns(columns);
        return render;
    }

    @Override
    public void render() {
        response.reset();
        try {
			response.setHeader("Content-disposition", "attachment; filename=" +  java.net.URLEncoder.encode(fileName, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        response.setContentType(CONTENT_TYPE);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            HSSFWorkbook wb = PoiKit.export(sheetName, 1, headers, columns, data, 8000);
            wb.write(os);
        } catch (Exception e) {
            throw new RenderException(e);
        } finally {
            try {
            	if(os!=null){
            		os.flush();
            		os.close();
            	}
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }

        }
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

}
