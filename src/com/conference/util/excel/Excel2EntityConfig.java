/*
 * Copyright (c) 2014. 骆驼CMS
 */

package com.conference.util.excel;

import java.text.SimpleDateFormat;

/**
 * 
 * excel转化为java实体的配置类
 * 
 * @author yongtree
 * @date 2011-12-20 上午11:42:51
 * @version 1.0
 * 
 */
public class Excel2EntityConfig {

	/**
	 * 传入字段： <br>
	 * ["name", "password", "birthday"] <br>
	 * 如果需要转化可以传入类似于以下的字段，将要转化的内容用json的方式传入<br>
	 * ["name", "password={'男':'1','女':'2'}", "birthday"]
	 * 
	 */
	private String[] columns;

	private SimpleDateFormat formater = new SimpleDateFormat(
			"yyyy-MM-dd HH：mm：ss ");

	private int currPosittion = 0;

	private int colStartPosittion = 1;

	private int curSheet = 0;

	private boolean onlyOneSheet = true;

	public boolean isOnlyOneSheet() {
		return onlyOneSheet;
	}

	/**
	 * 是否只读取一个sheet，默认是读取一个sheet，否则将从该sheet往后的所有sheet都读取
	 * 
	 * @param onlyOneSheet
	 */
	public void setOnlyOneSheet(boolean onlyOneSheet) {
		this.onlyOneSheet = onlyOneSheet;
	}

	public int getCurSheet() {
		if (curSheet < 0)
			curSheet = 0;
		return curSheet;
	}

	/**
	 * 要读取当前的第几个sheet，默认是第0个
	 * 
	 * @param curSheet
	 */

	public void setCurSheet(int curSheet) {
		this.curSheet = curSheet;
	}

	public SimpleDateFormat getFormater() {
		return formater;
	}

	/***************************************************************************
	 * 设置日期型字段的转换格式 默认值为 new SimpleDateFormat("yyyy-MM-dd HH：mm：ss ");
	 */
	public void setFormater(SimpleDateFormat formater) {
		this.formater = formater;
	}

	public String[] getColumns() {
		return columns;
	}

	/***************************************************************************
	 * 设置Excel列与实体字段的对应关系
	 * 
	 * @param columns
	 *            实体字段的 字符串数组表示 例如： String[] columns = {"字段一", "字段二",
	 *            "字段三","字段n..." };
	 *            Excel表格第一列对应实体对像字段一，Excel表格第二列对应实体对像字段二....依次类推
	 */
	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	public int getCurrPosittion() {
		return currPosittion;
	}

	/***************************************************************************
	 * 设置excel表格数据从第几行开始，默认值,跳过标题行，从第二行开始 该值大于0
	 */
	public void setCurrPosittion(int currPosittion) {
		this.currPosittion = currPosittion - 1;
	}

	public int getColStartPosittion() {
		return colStartPosittion;
	}

	/***************************************************************************
	 * 设置excel表格数据从第几列开始，默认值,从第1列开始 该值大于0
	 */
	public void setColStartPosittion(int colStartPosittion) {
		this.colStartPosittion = colStartPosittion - 1;
	}

}
