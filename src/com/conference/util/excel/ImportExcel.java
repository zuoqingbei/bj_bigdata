/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.conference.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.upload.UploadFile;

/**
 * 导入Excel文件（支持“XLS”和“XLSX”格式）
 * @author ThinkGem
 * @version 2013-03-10
 */
public class ImportExcel {
	
	private static Logger log = LoggerFactory.getLogger(ImportExcel.class);
			
	/**
	 * 工作薄对象
	 */
	private Workbook wb;
	
	/**
	 * 工作表对象
	 */
	private Sheet sheet;
	
	/**
	 * 标题行号
	 */
	private int headerNum;
	
	/**
	 * 构造函数
	 * @param path 导入文件，读取第一个工作表
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(String fileName, int headerNum) 
			throws InvalidFormatException, IOException {
		this(new File(fileName), headerNum);
	}
	
	/**
	 * 构造函数
	 * @param path 导入文件对象，读取第一个工作表
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(File file, int headerNum) 
			throws InvalidFormatException, IOException {
		this(file, headerNum, 0);
	}

	/**
	 * 构造函数
	 * @param path 导入文件
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(String fileName, int headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		this(new File(fileName), headerNum, sheetIndex);
	}
	
	/**
	 * 构造函数
	 * @param path 导入文件对象
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(File file, int headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		this(file.getName(), new FileInputStream(file), headerNum, sheetIndex);
	}
	
	/**
	 * 构造函数
	 * @param file 导入文件对象
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
//	public ImportExcel(MultipartFile multipartFile, int headerNum, int sheetIndex) 
//			throws InvalidFormatException, IOException {
//		this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headerNum, sheetIndex);
//	}
	
	/**
	 * 构造函数
	 * @param file 导入文件对象
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(UploadFile multipartFile, int headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		this(multipartFile.getOriginalFileName(), new FileInputStream(multipartFile.getFile()) , headerNum, sheetIndex);
	}

	/**
	 * 构造函数
	 * @param path 导入文件对象
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(String fileName, InputStream is, int headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		if (StringUtils.isBlank(fileName)){
			throw new RuntimeException("导入文档为空!");
		}else if(fileName.toLowerCase().endsWith("xls")){    
			this.wb = new HSSFWorkbook(is);    
        }else if(fileName.toLowerCase().endsWith("xlsx")){  
        	this.wb = new XSSFWorkbook(is);
        }else{  
        	throw new RuntimeException("文档格式不正确!");
        }  
		if (this.wb.getNumberOfSheets()<sheetIndex){
			throw new RuntimeException("文档中没有工作表!");
		}
		this.sheet = this.wb.getSheetAt(sheetIndex);
		this.headerNum = headerNum;
		log.debug("Initialize success.");
	}
	
	/**
	 * 获取行对象
	 * @param rownum
	 * @return
	 */
	public Row getRow(int rownum){
		return this.sheet.getRow(rownum);
	}

	/**
	 * 获取数据行号
	 * @return
	 */
	public int getDataRowNum(){
		return headerNum+1;
	}
	
	/**
	 * 获取最后一个数据行号
	 * @return
	 */
	public int getLastDataRowNum(){
//		return this.sheet.getLastRowNum()+headerNum;
		return this.sheet.getLastRowNum();
	}
	
	/**
	 * 获取最后一个列号
	 * @return
	 */
	public int getLastCellNum(){
		return this.getRow(headerNum).getLastCellNum();
	}
	
	/**
	 * 获取单元格值
	 * @param row 获取的行
	 * @param column 获取单元格列号
	 * @return 单元格值
	 */
	public Object getCellValue(Row row, int column){
		Object val = "";
		try{
			Cell cell = row.getCell(column);
			if (cell != null){
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
					//去除科学计数法 和0
					val = cell.getNumericCellValue();
					val = getPrettyNumber(val);
				}else if (cell.getCellType() == Cell.CELL_TYPE_STRING){
					val = cell.getStringCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
					val = cell.getCellFormula();
				}else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
					val = cell.getBooleanCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_ERROR){
					val = cell.getErrorCellValue();
				}
			}
		}catch (Exception e) {
			return val;
		}
		return val;
	}
	
	
	/** 
	 * @Desc: 后去单元值
	 * @param cell
	 * @return
	 * @return: Object
	 * @author: longjunfeng   
	 * @date: 2016年10月11日 下午4:12:14 
	 */
	public static Object getCellValue(Cell cell){
		Object val = "";
		try{
			if (cell != null){
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
					//去除科学计数法 和0
					val = cell.getNumericCellValue();
					val = getPrettyNumber(val);
				}else if (cell.getCellType() == Cell.CELL_TYPE_STRING){
					val = cell.getStringCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
					val = cell.getCellFormula();
				}else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
					val = cell.getBooleanCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_ERROR){
					val = cell.getErrorCellValue();
				}
			}
		}catch (Exception e) {
			return val;
		}
		return val;
	}
	
	public static String subZeroAndDot(String s){
	    if(s.indexOf(".") > 0){
	        s = s.replaceAll("0+?$", "");//去掉多余的0
	        s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
	    }else{
	    	DecimalFormat df = new DecimalFormat("0");  
			s= df.format(s);  
	    }
	    return s;
	}
	
	/**
	 * @desc	:获取数字,不用科学技术法标识
	 * @author	:作者 longjunfeng E-mail:463527083@qq.com
	 * @version :创建时间 2016年12月5日
	 * @param number
	 * @return
	 */
	public static String getPrettyNumber(Object number) {  
		double number2 = Double.parseDouble(number.toString());
		if(number2 % 1 == 0){// 是这个整数，小数点后面是0
			return BigDecimal.valueOf(Double.parseDouble(number.toString())).stripTrailingZeros().toPlainString(); 
		}else{//不是整数，小数点后面不是0
			return BigDecimal.valueOf(Double.parseDouble(number.toString())).toPlainString(); 
		}
	    
	}  

	/** 
	 * @Desc: 判断一行是否为空
	 * @param row
	 * @return
	 * @return: boolean
	 * @author: longjunfeng   
	 * @date: 2016年10月11日 下午4:09:42 
	 */
	public static boolean isEmptyRow(Row row){
		Iterator<Cell> cells = row.cellIterator();
		boolean isEmty=true;
		for (Cell cell : row) {
			Object obj = getCellValue(cell);
			if(obj!=null&&obj!=""){
				isEmty=false;
				break;
			}
		}
		return isEmty;
	}
	
//	/**
//	 * 导入测试
//	 */
	public static void main(String[] args) throws Throwable {
		ImportExcel ei = new ImportExcel("D:\\test.xlsx", 0);
		
		for (int i = ei.getDataRowNum(); i < ei.getLastDataRowNum()+1; i++) {
			Row row = ei.getRow(i);
			for (int j = 0; j < ei.getLastCellNum(); j++) {
				Object val = ei.getCellValue(row, j);
				System.out.print(val+", ");
			}
			System.out.print("\n");
		}
		
	}

}
