package com.conference.util.excel;



import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.conference.admin.model.FctZb;
import com.conference.admin.model.RdcProductAgentDetail;
import com.jfinal.plugin.activerecord.Record;

/**
 * 
 * @time   2017年7月26日08:55:24
 * @author zuoqb
 * @todo   新闻评价等级导出模板
 */
public class ExportUseLevelTemplate extends AbstractExcelExportTemplate<RdcProductAgentDetail>{

	private List<Record> list;//数据
	private List<Record> useTypes;//使用方式
	private String title;
	private static final int pre=3;
	
	public ExportUseLevelTemplate(List<Record> list,List<Record> useTypes,String title) {
		this.list = list;
		this.useTypes=useTypes;
		this.title=title;
	}

	@Override
	public String[] getSheetNames() {
		return new String[]{ title };
	}

	@Override
	protected void buildTitle(int sheetIndex) {
		//设置单元格宽度
		int[] columnWidths = new int[pre+useTypes.size()*3]; 
		columnWidths[0]=2500;
		columnWidths[1]=3000;
		columnWidths[2]=4500;
		for(int x=0;x<useTypes.size()*FctZb.levels.length;x++){
			columnWidths[pre+x]=3000;
		}
		Sheet sheet = getSheet(sheetIndex);
		for (int i = 0; i < columnWidths.length; i++) {
			sheet.setColumnWidth(i, columnWidths[i]);
		};
	}
	
	@SuppressWarnings("unused")
	@Override
	protected void buildBody(int sheetIndex) {
		ExcelCellStyle cellStyle = new ExcelCellStyle(workbook);
		Sheet sheet = getSheet(sheetIndex);
		//设置表头
		String[] tit1=new String[pre+useTypes.size()*FctZb.levels.length];
		tit1[0]="序号";
		tit1[1]="时间";
		tit1[2]="部门(发稿人)";
		for(int x=0;x<useTypes.size()*FctZb.levels.length;x++){
			tit1[pre+x]="评价等级";
		}
		String[] tit2=new String[pre+useTypes.size()*FctZb.levels.length];
		tit2[0]="序号";
		tit2[1]="时间";
		tit2[2]="部门(发稿人)";
		for(int x=0;x<useTypes.size()*FctZb.levels.length;x++){
			tit2[pre+x]=FctZb.levels[(x/useTypes.size())];
		}
		String[] tit3=new String[pre+useTypes.size()*FctZb.levels.length];
		tit3[0]="序号";
		tit3[1]="时间";
		tit3[2]="部门(发稿人)";
		int index=3;
		for(String level:FctZb.levels){
			for(int x=0;x<useTypes.size();x++){
				tit3[index]=useTypes.get(x).getStr("use_type_id");
				index++;
			};
		};
		
		String[][] titles = new String[][]{tit1,tit2,tit3};
		for (int i = 0; i < titles.length; i++) {
			Row row = sheet.createRow(i);
			for (int j = 0; j < titles[i].length; j++) {
				createStyledCell(row, j, titles[i][j], cellStyle.getCellStyleHead());
			}
		}
		int size=3+1+FctZb.levels.length+useTypes.size()*FctZb.levels.length;
		int[][] mergeCells = new int[size][];
		mergeCells[0]=new int[]{0,2,0,0};
		mergeCells[1]=new int[]{0,2,1,1};
		mergeCells[2]=new int[]{0,2,2,2};
		mergeCells[3]=new int[]{0,0,3,useTypes.size()*FctZb.levels.length+3-1};
		//评价等级
		for(int x=0;x<FctZb.levels.length;x++){
			mergeCells[4+x]=new int[]{1,1,3+useTypes.size()*x,3+useTypes.size()*(x+1)-1};
		};
		//使用方式
		int userRange=0;
		for(String level:FctZb.levels){
			for(int x=0;x<useTypes.size();x++){
				mergeCells[4+FctZb.levels.length+userRange]=new int[]{2,2,3+userRange,3+userRange};
				userRange++;
			}
		}
		//合并表头单元格 行行列列
		for (int i = 0; i < mergeCells.length; i++) {
			CellRangeAddress range = new CellRangeAddress(mergeCells[i][0],
					mergeCells[i][1], mergeCells[i][2], mergeCells[i][3]);
			sheet.addMergedRegion(range);
		}
		
		
		//第四行开始添加数据
		int currentXuHao = 0;	//当前序号
		int modelSize = 3;	//合并前每一组数据包含3条记录
		for (int i = 0; i < list.size(); i++) {
			Row rowBody = sheet.createRow(i+modelSize); 
			//相同序号出现四次，第五次时，序号+1，出现次数置0
			currentXuHao++;
			createStyledCell(rowBody, 0, currentXuHao + "", cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 1, list.get(i).get("date")+"", cellStyle.getCellStyleDataText());
			createStyledCell(rowBody, 2, list.get(i).get("deptname")+"", cellStyle.getCellStyleDataText());
			int cIndex=3;
			for(String level:FctZb.levels){
				for(Record r:useTypes){
					createStyledCell(rowBody, cIndex, list.get(i).get(level+r.getStr("use_type_id"))+"", cellStyle.getCellStyleDataText());
					cIndex++;
				};
			};
		
		}
	}
	
	@Override
	public String[][] getTitles() {
		return null;
	}

}
