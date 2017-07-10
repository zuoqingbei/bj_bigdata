package com.conference.util.excel;



import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.conference.admin.model.RdcProductAgentDetail;

/**
 * 
 * @className ExportRdcProductAgentDetailTemplate.java
 * @time   2016-6-1 下午1:22:14
 * @author zuoqb
 * @todo   产品代表明细导出模板
 */
public class ExportRdcProductAgentDetailTemplate extends AbstractExcelExportTemplate<RdcProductAgentDetail>{

	private List<RdcProductAgentDetail> list;
	private String title;
	
	public ExportRdcProductAgentDetailTemplate(List<RdcProductAgentDetail> list,String title) {
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
		int[] columnWidths = new int[]{4500, 4500, 2500, 2500, 2500,2500, 2500, 2500}; 
		Sheet sheet = getSheet(sheetIndex);
		for (int i = 0; i < columnWidths.length; i++) {
			sheet.setColumnWidth(i, columnWidths[i]);
		}
	}
	
	@Override
	protected void buildBody(int sheetIndex) {
		ExcelCellStyle cellStyle = new ExcelCellStyle(workbook);
		Sheet sheet = getSheet(sheetIndex);
		//冻结行列
		sheet.createFreezePane(11, 3);
		//设置表头
		String[][] titles = new String[][]{
				{"时间", "部门", "使用方式", "使用方式","使用方式", "使用方式", "使用方式", "使用方式" },
				{"时间", "部门", "APP", "APP", "APP", "IOS", "IOS", "IOS"},
				{"时间", "部门", "优", "良", "中","优", "良", "中"}
			};
		for (int i = 0; i < titles.length; i++) {
			Row row = sheet.createRow(i);
			for (int j = 0; j < titles[i].length; j++) {
				createStyledCell(row, j, titles[i][j], cellStyle.getCellStyleHead());
			}
		}
		//合并表头单元格 行行列列
		int[][] mergeCells = new int[][]{
			{0,2,0,0},{0,2,1,1},{0,0,2,7},{1,1,2,4},{1,1,5,7}
		};
		for (int i = 0; i < mergeCells.length; i++) {
			CellRangeAddress range = new CellRangeAddress(mergeCells[i][0], mergeCells[i][1], mergeCells[i][2], mergeCells[i][3]);
			sheet.addMergedRegion(range);
		}
		//第四行开始添加数据
		int sameXuHaoNum = 0;	//相同序号出现次数
		int currentXuHao = 1;	//当前序号
		int modelSize = 3;	//合并前每一组数据包含3条记录
		for (int i = 0; i < list.size(); i++) {
			Row rowBody = sheet.createRow(i+3); 
			//序号不好合并，单独处理
			sameXuHaoNum++;
			//相同序号出现四次，第五次时，序号+1，出现次数置0
			if (sameXuHaoNum == modelSize + 1) {
				sameXuHaoNum = 1;
				currentXuHao++;
			}
			createStyledCell(rowBody, 0, currentXuHao + "", cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 1, list.get(i).getTime(), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 2, list.get(i).getDept(), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 3, list.get(i).getAppYx(), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 4, list.get(i).getAppLh(), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 5, list.get(i).getAppNor(), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 6, list.get(i).getIosYx(), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 7, list.get(i).getIosLh(), cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 7, list.get(i).getIosNor(), cellStyle.getCellStyleDataText());
			
		}
		//合并数据
		int modelNum = list.size()/modelSize;	//分组
		for (int i = 0; i < modelNum; i++) {
			//合并时间
			CellRangeAddress cellRangeAddress0 = new CellRangeAddress(3+i*modelSize, 
					3+i*modelSize+(modelSize-1), 0, 0);
			sheet.addMergedRegion(cellRangeAddress0);
			//合并部门
			CellRangeAddress cellRangeAddress1 = new CellRangeAddress(3+i*modelSize, 
					3+i*modelSize+(modelSize-1), 1, 1);
			sheet.addMergedRegion(cellRangeAddress1);
		
		}
	}
	
	@Override
	public String[][] getTitles() {
		return null;
	}

}
