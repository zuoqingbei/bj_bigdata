package com.conference.util.excel;



import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * @author WangXuzheng
 *
 */
public abstract class AbstractExcelExportTemplate<T> implements ExcelExportTemplate<T> {
	/**
	 * 榛樿琛ㄦ牸瀹藉害
	 */
	private static final int DEFAULT_COLUMN_WIDTH = 7000;
	/**
	 * excel鏂囦欢瀵硅薄
	 */
	protected Workbook  workbook;
	/**
	 * excel sheet鍒楄〃
	 */
	protected List<Sheet> sheets = new ArrayList<Sheet>();
	/**
	 * 鏍囬鏍�
	 */
	protected String[][] titles;
	protected CellStyle captionRowSytle;
	/**
	 * 榛樿鏍囬琛屾牱寮�
	 */
	protected CellStyle titleRowStyle;
	/**
	 * 榛樿鍐呭琛屾牱寮�
	 */
	protected CellStyle bodyRowStyle;
	/**
	 * 鍚勪釜sheet鏄惁鍖呭惈鎶ご,key:sheet鍧愭爣锛寁alue:鍖呭惈true,鍚﹀垯false
	 */
	protected Map<Integer, Boolean> hasCaptionMap = new HashMap<Integer, Boolean>();
	/**
	 * 榛樿鍗曞厓鏍煎搴�
	 */
	protected int columnWidth = DEFAULT_COLUMN_WIDTH;
	/**
	 * 鍙傛暟鍒楄〃
	 */
	protected T parameters;

	/* (non-Javadoc)
	 * @see com.haier.openplatform.excel.ExcelExportService#doExport(java.io.OutputStream)
	 */
	@Override
	public void doExport(OutputStream outputStream,T parameters) throws IOException {
		String[] sheetNames = this.getSheetNames();
		Validate.notEmpty(sheetNames);
		Validate.notNull(outputStream);
		this.parameters = parameters;
		this.workbook = new SXSSFWorkbook(getRowAccessWindowSize());
		this.titles = this.getTitles();
		this.captionRowSytle = crateCaptionCellStyle();
		this.titleRowStyle = crateTitleCellStyle();
		this.bodyRowStyle = crateBodyCellStyle();
		this.afterCreateWorkBook();
		
		for (int i = 0; i < sheetNames.length; i++) {
			Sheet sheet = workbook.createSheet(sheetNames[i]);
			this.sheets.add(sheet);
			afterBuildSheet(i);
			buildCaption(i);
			buildTitle(i);
			afterBuildTitle(i);
			buildBody(i);
			afterBuildBody(i);
		}
		this.workbook.write(outputStream);
	}

	/**
	 * 鍒涘缓鍗曞厓鏍�
	 * @param row
	 * @param index
	 * @param cellValue
	 * @param cellStyle
	 */
	protected void createStyledCell(Row row,int index,String cellValue,CellStyle cellStyle){
		Cell cell = row.createCell(index);
		cell.setCellValue(cellValue);
		cell.setCellStyle(cellStyle);
	}
	
	/**
	 * 鍦ㄥ垱寤哄畬姣旽SSFWorkBook瀵硅薄鍜屾牱寮忓璞″悗浣滅殑澶勭悊鎿嶄綔锛岄�甯哥敤鏉ュ榛樿鐨勬牱寮忚繘琛岄噸鏂板畾涔�
	 */
	protected void afterCreateWorkBook(){
	}
	/**
	 * 鑾峰彇excel鎶ご鏍峰紡
	 * @return
	 */
	protected CellStyle crateCaptionCellStyle() {
		Font font = workbook.createFont();
		font.setColor(Font.COLOR_NORMAL);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(false);
		font.setFontHeight((short)250);
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		return cellStyle;
	}
	
	/**
	 * 鑾峰彇excel琛ㄥご鏍峰紡
	 * @return
	 */
	protected CellStyle crateTitleCellStyle() {
		Font font = workbook.createFont();
		font.setColor(Font.COLOR_NORMAL);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(false);
		font.setFontHeight((short)250);
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		short border = 1;
		setCellBorder(cellStyle,border,border,border,border);
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		return cellStyle;
	}
	
	/**
	 * 璁剧疆鍗曞厓鏍肩殑border
	 * @param cellStyle
	 * @param top
	 * @param bottom
	 * @param left
	 * @param right
	 */
	protected void setCellBorder(CellStyle cellStyle,short top,short bottom,short left,short right){
		cellStyle.setBorderBottom(bottom);
		cellStyle.setBorderLeft(left);
		cellStyle.setBorderRight(right);
		cellStyle.setBorderTop(top);
	}
	
	/**
	 * 鑾峰彇excel鍐呭鏍峰紡
	 * @return
	 */
	protected CellStyle crateBodyCellStyle() {
		Font font = workbook.createFont();
		//font.setColor(HSSFColor.BLUE_GREY.index);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(false);
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		short border = 1;
		setCellBorder(cellStyle,border,border,border,border);
		return cellStyle;
	}

	/**
	 * 鑾峰彇绗琻涓猠xcel sheet
	 * @param sheetIndex
	 * @return
	 */
	protected Sheet getSheet(int sheetIndex) {
		return this.sheets.get(sheetIndex);
	}

	/**
	 * 鍒涘缓sheet瀹屾瘯鍚庡仛鐨勬搷浣�
	 * @param sheetIndex
	 */
	protected void afterBuildSheet(int sheetIndex) {
	}
	
	/**
	 * 鍦╯heet鐨勭涓�鎻掑叆鏍囬
	 * @param sheetIndex
	 */
	protected void buildCaption(int sheetIndex){
		Sheet sheet = getSheet(sheetIndex);
		String[] captions = this.getCaptions(); 
		hasCaptionMap.put(sheetIndex, false);
		if(captions != null && captions.length >=sheetIndex +1){
			String caption = captions[sheetIndex];
			if(StringUtils.isNotBlank(caption)){
				Row row = sheet.createRow(0);
				int lastColumn = calculateLastColumn(sheetIndex);
				CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, lastColumn);
				sheet.addMergedRegion(cellRangeAddress);
				createStyledCell(row, 0, caption, this.captionRowSytle);
				hasCaptionMap.put(sheetIndex, true);
			}
		}
	}
	
	/**
	 * 璁＄畻鏈�悗涓�垪鏁版嵁鏁版嵁鐨�
	 * @param sheetIndex
	 * @return
	 */
	protected int calculateLastColumn(int sheetIndex){
		if(this.titles != null && sheetIndex <= this.titles.length -1 && this.titles[sheetIndex] != null){
			return this.titles[sheetIndex].length - 1;
		}else{
			return 1;
		}
	}

	/**
	 * 鍒涘缓sheet涓暟鎹殑鏍囬
	 * @param sheetIndex
	 */
	protected void buildTitle(int sheetIndex){
		if(this.titles.length < sheetIndex + 1){
			return;
		}
		String[] ts = this.titles[sheetIndex];
		if(ts == null){
			return;
		}
		Sheet sheet = this.getSheet(sheetIndex);
		int titleStartIndex = this.getTitleStartIndex(sheetIndex);
		Row rowTitle = sheet.createRow(titleStartIndex); 
		for(int i = 0; i < ts.length; i++){
			sheet.setColumnWidth(i, columnWidth);
			createStyledCell(rowTitle, i, ts[i], this.titleRowStyle);
		}
	}
	
	/**
	 * 鑾峰彇鍚勪釜sheet鍐呭閮ㄥ垎璧峰琛宨ndex,榛樿涓轰粠绗竴琛屽紑濮�
	 * @param sheetIndex sheet鐨刬ndex
	 * @return
	 */
	protected int getBodyStartIndex(int sheetIndex){
		int captionRow = getTitleStartIndex(sheetIndex);;
		int titleRow = 0;
		if(this.titles != null && this.titles.length >= sheetIndex + 1){
			if(titles[sheetIndex] != null && titles[sheetIndex].length >0){
				titleRow = 1;
			}
		}
		return captionRow + titleRow;
	}

	protected int getTitleStartIndex(int sheetIndex){
		return this.hasCaptionMap.get(sheetIndex) ? 1 : 0;
	}
	/**
	 * 鍒涘缓sheet涓暟鎹殑鏍囬涔嬪悗鍋氱殑鎿嶄綔
	 * @param sheetIndex
	 */
	protected void afterBuildTitle(int sheetIndex) {
	}

	/**
	 * 鍒涘缓sheet涓暟鎹殑鍐呭
	 * @param sheetIndex
	 */
	protected abstract void buildBody(int sheetIndex);

	/**
	 * 鍒涘缓sheet涓暟鎹殑鍐呭涔嬪悗鍋氱殑鎿嶄綔
	 * @param sheetIndex
	 */
	protected void afterBuildBody(int sheetIndex) {
	}

	@Override
	public String[] getCaptions() {
		return new String[]{};
	}

	@Override
	public int getRowAccessWindowSize() {
		return 200;
	}
}
