/*
 * Copyright (c) 2014. 骆驼CMS
 */

package com.conference.util.excel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oecp.framework.util.excel.ExcelReadException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * Excel文件读取类，读取Excel表格记录为JAVA对像 <br>
 * 支持2003和2007两个版本，不做设置则默认为2007版本
 * 
 * @author yongtree
 * @date 2011-12-20 上午11:42:45
 * @param <E>
 *            读取后转换的目标对像类型
 */
public class ExcelReader<E> {
	/**
	 * 实体对像
	 */
	private E entity;

	private Excel2EntityConfig excel2EntityConfig;
	/**
	 * 创建文件输入流
	 */
	private BufferedReader reader = null;
	/**
	 * 文件二进制输入流
	 */
	private InputStream is = null;
	/**
	 * 当前工作表 sheet
	 */
	private int currSheet;
	/**
	 * 当前位置
	 */
	private int currPosittion;
	/**
	 * 工作表sheet的数量 *
	 */
	private int numOfSheets;
	/**
	 * HSSFWordbook
	 */
	private Workbook workbook = null;

	private ExcelVersion version;

	public ExcelReader(ExcelVersion version) {
		this.version = version;
	}

	public ExcelReader() {
	}

	/**
	 * 由文件输入流创建初始化一个ExcelReader
	 * 
	 * @param inputfile
	 *            文件输入流
	 * @throws IOException
	 * @throws Exception
	 */
	public void InitExcelReader(InputStream inputfile)
			throws ExcelReadException {
		if (inputfile == null) {
			throw new ExcelReadException("文件输入流为空");
		}
		/**
		 * 设置开始行
		 */
		this.currPosittion = this.excel2EntityConfig.getCurrPosittion();
		/**
		 * 设置当前位置为0
		 */
		this.currSheet = this.excel2EntityConfig.getCurSheet();
		/**
		 * 创建文件输入流 *
		 */
		this.is = inputfile;
		/**
		 * 判断文件格式
		 */
		try {
			if (ExcelVersion.V2003.equals(getVersion())) {
				this.workbook = new HSSFWorkbook(this.is);
			} else if (ExcelVersion.V2007.equals(getVersion())) {
				this.workbook = new XSSFWorkbook(this.is);
			}
		} catch (IOException e) {
			throw new ExcelReadException(e);
		}

		/**
		 * 设置工作表Sheet数
		 */
		this.numOfSheets = this.workbook.getNumberOfSheets();
		this.sheet = this.workbook.getSheetAt(this.currSheet);
	}

	public List<String> readFirstLine() {
		return getLine(0);
	}

	private Sheet sheet;

	/**
	 * 读到文件的一行
	 * 
	 * @return
	 * @throws ExcelReadException
	 */
	public E readLine() throws ExcelReadException {
		/**
		 * 根据currSheet值获得当前的工作表Sheet
		 */
		if (sheet == null) {
			sheet = this.workbook.getSheetAt(this.currSheet);
		}
		/**
		 * 判断当前行是否到当前工作表sheet的结尾
		 */
		if (currPosittion > sheet.getLastRowNum()
				&& !this.excel2EntityConfig.isOnlyOneSheet()) {
			/**
			 * 当前行位置清0
			 */
			this.currPosittion = this.excel2EntityConfig.getCurrPosittion();
			/**
			 * 判断是否还有工作表sheet
			 */
			while (currSheet < this.numOfSheets - 1) {
				/**
				 * 得到下一张工作表sheet
				 */
				currSheet = currSheet + 1;
				sheet = this.workbook.getSheetAt(currSheet);
				/**
				 * 当前行数是否已到达文件末尾
				 */
				if (this.currPosittion - 1 == sheet.getLastRowNum()) {
					/**
					 * 不前工作表sheet指向一下张sheet
					 */
					currSheet++;
					continue;
				} else {
					/**
					 * 获到当前行数
					 */
					int row = currPosittion;
					currPosittion++;
					return getLine2E(row);
				}
			}
			return null;
		}
		int row = currPosittion;
		currPosittion++;
		return getLine2E(row);
	}

	/***************************************************************************
	 * 返回工作表sheet的一行数据
	 * 
	 * @param sheet
	 *            工作表
	 * @param row
	 *            行
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws ParseException
	 */
	private E getLine2E(int row) throws ExcelReadException {
		List<String> line = getLine(row);
		if (line != null && line.size() > 0) {
			HashMap map = null;
			Model model = null;
			Record record = null;
			if (entity instanceof HashMap) {
				map = (HashMap) entity;
			} else if (entity instanceof Model) {
				model = (Model) entity;
			} else if (entity instanceof Record) {
				record = (Record) entity;
			}
			int len = line.size() > this.excel2EntityConfig.getColumns().length ? this.excel2EntityConfig
					.getColumns().length : line.size();
			boolean empty = true;
			for (int i = 0; i < len; i++) {
				String cellvalue = line.get(i);
				if (StrKit.notBlank(cellvalue)) {
					empty = false;
				}
				String column = this.excel2EntityConfig.getColumns()[i];
				if (StringUtils.isNotBlank(column)) {// 如果不为空采取为实体赋值
					if (column.contains("=")) {// 如果有=的话，说明包含了枚举的转换
						String[] arr = column.split("=");
						column = arr[0];
						String json = arr[1];
						Map<String, Object> enums = (Map<String, Object>) JSON
								.parse(json);
						String n_cellvalue = (String) enums.get(cellvalue);
						if (StringUtils.isNotBlank(n_cellvalue)) {
							cellvalue = n_cellvalue;
						}
					}
					if (map != null) {
						map.put(column, cellvalue);
						entity = (E) map;
					} else if (model != null) {
						model.put(column, cellvalue);
						entity = (E) model;
					} else if (record != null) {
						record.set(column, cellvalue);
						entity = (E) record;
					} else {

						Field[] field = entity.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
						for (int j = 0; j < field.length; j++) { // 遍历所有属性
							String name = field[j].getName(); // 获取属性的名字
							name = this.A2UpperCase(name);
							String type = field[j].getGenericType().toString(); // 获取属性的类型
							if (this.A2UpperCase(column).trim().equals(name)
									&& cellvalue != null
									&& cellvalue.trim().equals("") == false) {
								try {
									if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class//
										// "，后面跟类名
										Method sm = entity.getClass()
												.getDeclaredMethod(
														"set" + name,
														String.class);
										sm.invoke(entity, cellvalue);
									}
									if (NumberUtils.isNumber(cellvalue)) {
										if (type.equals("class java.lang.Integer")) {
											Method sm = entity.getClass()
													.getDeclaredMethod(
															"set" + name,
															Integer.class);
											sm.invoke(entity,
													Integer.parseInt(cellvalue));
										}
										if (type.equals("class java.lang.Short")) {
											Method sm = entity.getClass()
													.getDeclaredMethod(
															"set" + name,
															Short.class);
											sm.invoke(entity,
													Short.parseShort(cellvalue));
										}
										if (type.equals("class java.lang.Double")) {
											Method sm = entity.getClass()
													.getDeclaredMethod(
															"set" + name,
															Double.class);
											sm.invoke(entity, Double
													.parseDouble(cellvalue));
										}
										if (type.equals("class java.lang.Boolean")) {
											Method sm = entity.getClass()
													.getDeclaredMethod(
															"set" + name,
															Boolean.class);
											sm.invoke(entity, Boolean
													.parseBoolean(cellvalue));
										}
									}
									if (type.equals("class java.util.Date")) {
										Method sm = entity.getClass()
												.getDeclaredMethod(
														"set" + name,
														Date.class);
										sm.invoke(
												entity,
												this.excel2EntityConfig
														.getFormater().parse(
																cellvalue));
									}
								} catch (SecurityException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

					}
				}
			}
			if (empty) {
				return null;
			} else {
				return entity;
			}
		}
		return null;
	}

	private List<String> getLine(int row) {
		List<String> line = new ArrayList<String>();
		/**
		 * 根据行数取得sheet的一行
		 */
		Row rowline = sheet.getRow(row);
		if (rowline != null) {
			/**
			 * 创建字符串缓冲区
			 */
			StringBuffer buffer = new StringBuffer();
			/**
			 * 获到当前行的列数
			 */
			int filledColumns = rowline.getLastCellNum();
			Cell cell = null;
			/***
			 * 开始读取的列，从第几列开始读。
			 */
			int colStart = this.getExcel2EntityConfig().getColStartPosittion();
			/**
			 * 遍历所有列
			 */
			for (int i = colStart; i < filledColumns; i++) {
				/**
				 * 取得当前单元格
				 */
				cell = rowline.getCell(i);
				String cellvalue = null;
				if (cell != null) {
					/**
					 * 判断当前单元格的type
					 */
					switch (cell.getCellType()) {
					// 如果当前Cell的type为NUMERIC
					case Cell.CELL_TYPE_NUMERIC: {
						// 判断当前的Cell是否为Date
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							// 如果是在Date类型，则取得该Cell的Date值
							Date date = cell.getDateCellValue();
							cellvalue = this.excel2EntityConfig.getFormater()
									.format(date);
							// 把Date转换成本地格式的的字符串
							// celldatevalue = cell.getDateCellValue();
						} else {
							// 如果是纯数字
							// 取得当前cell的数值
							long num = (long) cell.getNumericCellValue();
							cellvalue = String.valueOf(num);
						}
						break;
					}
					case Cell.CELL_TYPE_STRING:
						// /取得当前Cell的字符串
						cellvalue = cell.getStringCellValue()
								.replace("'", "''");
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						cellvalue = String.valueOf(cell.getBooleanCellValue());
					case Cell.CELL_TYPE_FORMULA:
						cellvalue = String.valueOf(cell.getBooleanCellValue());
					default:
						cellvalue = " ";
					}
				} else {
					cellvalue = "";
				}
				line.add(cellvalue);
			}
			return line;
		} else {
			return null;
		}
	}

	public List<E> readLines() throws ExcelReadException {
		List<E> list = new ArrayList<E>();
		while (entity != null) {
			E e = readLine();
			if (e != null) {
				list.add(e);
				try {
					entity = (E) entity.getClass().newInstance();
				} catch (InstantiationException e1) {
					throw new ExcelReadException(e1);
				} catch (IllegalAccessException e1) {
					throw new ExcelReadException(e1);
				}
			} else {
				entity = null;
			}
		}
		return list;
	}

	/***************************************************************************
	 * 关闭函数执行流的操作
	 */
	public void close() {
		// 如果is不为空，则关闭InputStream文件输入流
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				is = null;
				e.printStackTrace();
			}
		}
		// 如果reader不为空,则关闭BufferedReader文件输入流
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				reader = null;
				e.printStackTrace();
			}
		}
	}

	public E getEntity() {
		return entity;
	}

	/***************************************************************************
	 * 设置读取Excel记录行后转换的实体对像实例
	 * 
	 * @param entity
	 */
	public void setEntity(E entity) {
		this.entity = entity;
	}

	public Excel2EntityConfig getExcel2EntityConfig() {
		return excel2EntityConfig;
	}

	public void setExcel2EntityConfig(Excel2EntityConfig excel2EntityConfig) {
		this.excel2EntityConfig = excel2EntityConfig;
	}

	/**
	 * 将指定英文字符串首字母大写
	 * 
	 * @param filed
	 * @return 首字母大写后的字符串
	 */
	private String A2UpperCase(String filed) {
		return filed.substring(0, 1).toUpperCase()
				+ filed.substring(1, filed.length());
	}

	public ExcelVersion getVersion() {
		if (version == null)
			return ExcelVersion.V2007;
		return version;
	}

	public void setVersion(ExcelVersion version) {
		this.version = version;
	}

	public static void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {

	}

}
