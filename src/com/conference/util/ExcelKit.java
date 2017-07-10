package com.conference.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.jfinal.kit.StrKit;

public class ExcelKit {
	
	public static String parseExcel(Cell cell) {
		if(cell==null){
			return null;
		}
        String result = "";  
       // cell.setCellType(Cell.CELL_TYPE_STRING);
	    switch (cell.getCellType()) {  
	        case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型  
	        	  SimpleDateFormat sdf = null;  
	        	if (HSSFDateUtil.isCellDateFormatted(cell)) {   
	    	        double d = cell.getNumericCellValue();   
	    	        Date date = HSSFDateUtil.getJavaDate(d);   
	    	        sdf = new SimpleDateFormat("yyyy-MM-dd HH");  
	    	        result= sdf.format(date);
	    	    } else{
	    	    	DecimalFormat df = new DecimalFormat("0");  
	    	    	result = df.format(cell.getNumericCellValue()); 
	    	    	cell.setCellType(Cell.CELL_TYPE_STRING);
	    	    }
	            break;  
	        case HSSFCell.CELL_TYPE_STRING:// String类型  
	            result = cell.getRichStringCellValue().toString();  
	            break;  
	        case HSSFCell.CELL_TYPE_BLANK:  
	            result = "";  
	        default:  
	            result = "";  
	            break;  
	        }  
        return result;  
    }  
	
	
	 /**
     * 判断是否是空行
     * @author DELL
     * @date 2015-5-7上午11:18:36
     * @return
     */
    public static boolean isEmptyRow(Row row){
    	for (int i = row.getFirstCellNum(); i <= row.getLastCellNum()-1; i++) {
    		if(row.getCell(i)!=null){
    			if(StrKit.notBlank(parseExcel(row.getCell(i)))){
    				return false;
    			}
    		}
		}
    	return  true;
    }

}
