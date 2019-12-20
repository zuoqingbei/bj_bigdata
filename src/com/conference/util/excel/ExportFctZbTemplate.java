package com.conference.util.excel;



import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.conference.admin.model.RdcProductAgentDetail;
import com.conference.util.sys.SqlUtil;
import com.jfinal.plugin.activerecord.Record;

/**
 * 
 * @time   2017年7月18日09:41:47
 * @author zuoqb
 * @todo   素材分析导出模板
 */
public class ExportFctZbTemplate extends AbstractExcelExportTemplate<RdcProductAgentDetail>{

	private List<Record> list;
	private String title;
	
	public ExportFctZbTemplate(List<Record> list,String title) {
		this.list = list;
		this.title=title;
	}

	@Override
	public String[] getSheetNames() {
		return new String[]{ title };
	}

	@Override
	protected void buildTitle(int sheetIndex) {
		//设置单元格宽度
		int[] columnWidths = new int[]{2500,4500, 4500, 4500, 7500, 4500,4500,3000, 4500, 4500}; 
		Sheet sheet = getSheet(sheetIndex);
		for (int i = 0; i < columnWidths.length; i++) {
			sheet.setColumnWidth(i, columnWidths[i]);
		}
	}
	
	@Override
	protected void buildBody(int sheetIndex) {
		ExcelCellStyle cellStyle = new ExcelCellStyle(workbook);
		Sheet sheet = getSheet(sheetIndex);
		//设置表头
		String[][] titles = new String[][]{
				{"序号","时间", "部门", "发稿人", "新闻标题","使用方式", "评价级别", "是否批示" ,"使用单位", "前方记者"}
			};
		for (int i = 0; i < titles.length; i++) {
			Row row = sheet.createRow(i);
			for (int j = 0; j < titles[i].length; j++) {
				createStyledCell(row, j, titles[i][j], cellStyle.getCellStyleHead());
			}
		}
		//第四行开始添加数据
		int sameXuHaoNum = 0;	//相同序号出现次数
		int currentXuHao = 1;	//当前序号
		int modelSize = 1;	//合并前每一组数据包含3条记录
		for (int i = 0; i < list.size(); i++) {
			Row rowBody = sheet.createRow(i+modelSize); 
			//序号不好合并，单独处理
			sameXuHaoNum++;
			//相同序号出现四次，第五次时，序号+1，出现次数置0
			if (sameXuHaoNum == modelSize + 1) {
				sameXuHaoNum = 1;
				currentXuHao++;
			}
			createStyledCell(rowBody, 0, currentXuHao + "", cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 1, SqlUtil.dealNull(list.get(i).get("date")+""), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 2, SqlUtil.dealNull(list.get(i).get("deptName")+""), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 3, SqlUtil.dealNull(list.get(i).get("creatorName")+""), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 4, SqlUtil.dealNull(list.get(i).get("zb_name")+""), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 5, SqlUtil.dealNull(list.get(i).get("useType")+""), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 6, SqlUtil.dealNull(list.get(i).get("useLevel")+""), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 7, SqlUtil.dealNull(list.get(i).get("hasPiShi")+""), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 8, SqlUtil.dealNull(list.get(i).get("office")+""), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 9, SqlUtil.dealNull(list.get(i).get("souceCodeName")+""), cellStyle.getCellStyleDataText());
		}
	}
	
	@Override
	public String[][] getTitles() {
		return null;
	}

}
