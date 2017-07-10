package com.conference.util.excel;



import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @className ExcelCellStyle
 * @time 2013-4-24下午4:19:55
 * @author yaoyt
 * @version
 * @param <T>
 */
public class ExcelCellStyle {
	/**
	 * 表头
	 */
	private CellStyle cellStyleHead;
	/**
	 * 合计文字
	 */
	private CellStyle cellStyleSumText;
	/**
	 * 合计数字
	 */
	private CellStyle cellStyleSumNumber;
	/**
	 * 数据文字left
	 */
	private CellStyle cellStyleDataText;
	
	private CellStyle cellStyleDataNumberImport;
	
	/**
	 * 数据数字right
	 */
	private CellStyle cellStyleDataNumber;
	/**
	 * 数据可输入（文字）
	 */
	private CellStyle cellStyleEnableText;
	/**
	 * 数据可输入（数字）
	 */
	private CellStyle cellStyleEnableNumber;
	/**
	 * 数据不可输入（数字）
	 */
	private CellStyle cellStyleDisableNumber;
	/**
	 * 数据不可输入（文字）
	 */
	private CellStyle cellStyleDisableText;
	/**
	 * 指标用，粗体、左对齐
	 */
	private CellStyle cellStyleBoldLeft;
	/**
	 * 指标用，正常样式、右对齐
	 */
	private CellStyle cellStyleNormalRight;
	
	/**
	 * SAP部门导出表头
	 */
//	private static CellStyle CellStyleSaphead;
	
	/**
	 * 整个excel
	 */
	private Workbook workbook;
	
	private DataFormat format;
	/**
	 * ExcelCellStyle 
	 * @param wb
	 */
	public ExcelCellStyle(Workbook wb) {
		workbook = wb;
		format = wb.createDataFormat();
		cellStyleHead = crateHeadCell();
		cellStyleSumText = crateSumTextCell();
		cellStyleSumNumber = crateSumNumberCell();
		cellStyleDataText = crateDataTextCell();
		cellStyleDataNumber = crateDataNumberCell();
		cellStyleEnableText = crateEnableTextCell();
		cellStyleEnableNumber = crateEnableNumberCell();
		cellStyleDisableNumber = crateDisableNumberCell();
		cellStyleDisableText = crateDisableTextCell();
		
		
		cellStyleBoldLeft = crateBoldLeftCell();
		cellStyleNormalRight = crateNormalRightCell();
		
		cellStyleDataNumberImport = createImportDataNumberCell();
	}
	

	
	private CellStyle createImportDataNumberCell(){
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置背景色
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		// 设置背景填充(背景色必须)
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_DASHED);		
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		//设置单元格样式为数字
		//cellStyle.setDataFormat(format.getFormat("@"));
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
		return cellStyle;
	}
	
	/**
	 * ExcelCellStyle 
	 * @return
	 */
	private CellStyle crateNormalRightCell() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置背景色
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		// 设置背景填充(背景色必须)
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_DASHED);		
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}


	/**
	 * ExcelCellStyle 
	 * @return
	 */
	private CellStyle crateBoldLeftCell() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置背景色
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		// 设置背景填充(背景色必须)
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_DASHED);		
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}



	/**
	 * 表头
	 * 
	 * @author yaoyt
	 * @time 2013-4-24下午4:19:47
	 * @return
	 */
	private CellStyle crateHeadCell() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.WHITE.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置背景色
		cellStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
		// 设置背景填充(背景色必须)
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_THIN);		
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}
	
	/**
	 * 合计文字
	 * 
	 * @author yaoyt
	 * @time 2013-4-24下午4:19:47
	 * @return
	 */
	private CellStyle crateSumTextCell() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置背景色
		cellStyle.setFillForegroundColor(HSSFColor.LEMON_CHIFFON.index);
		// 设置背景填充(背景色必须)
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_THIN);		
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}
	
	/**
	 * 合计数字
	 * 
	 * @author yaoyt
	 * @time 2013-4-24下午4:19:47
	 * @return
	 */
	private CellStyle crateSumNumberCell() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置背景色
		cellStyle.setFillForegroundColor(HSSFColor.LEMON_CHIFFON.index);
		// 设置背景填充(背景色必须)
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_THIN);		
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}
	
	/**
	 * 数据文字left
	 * 
	 * @author yaoyt
	 * @time 2013-4-24下午4:19:47
	 * @return
	 */
	private CellStyle crateDataTextCell() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置背景色
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		// 设置背景填充(背景色必须)
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_DASHED);		
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		//设置单元格样式为文本
		cellStyle.setDataFormat(format.getFormat("@"));
		return cellStyle;
	}
	
	/**
	 * 数据数字right
	 * 
	 * @author yaoyt
	 * @time 2013-4-24下午4:19:47
	 * @return
	 */
	private CellStyle crateDataNumberCell() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置背景色
		//cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		// 设置背景填充(背景色必须)
		//cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_DASHED);
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}
	
	/**
	 * 数据可输入（文字）
	 * 
	 * @author yaoyt
	 * @time 2013-4-24下午4:19:47
	 * @return
	 */
	private CellStyle crateEnableTextCell() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置背景色
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		// 设置背景填充(背景色必须)
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_DASHED);
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}
	
	/**
	 * 数据可输入（数字）
	 * @author yaoyt
	 * @time 2013-4-24下午4:19:47
	 * @return
	 */
	private CellStyle crateEnableNumberCell() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置背景色
		cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		// 设置背景填充(背景色必须)
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_DASHED);
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}
	
 
	/**
	 * 数据不可输入（数字）
	 * 
	 * @author yaoyt
	 * @time 2013-4-24下午4:19:47
	 * @return
	 */
	private CellStyle crateDisableNumberCell() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置背景色
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		// 设置背景填充(背景色必须)
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_DASHED);
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}
	/**
	 * 数据不可输入（文字）
	 * 
	 * @author yaoyt
	 * @time 2013-4-24下午4:19:47
	 * @return
	 */
	private CellStyle crateDisableTextCell() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置背景色
		cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		// 设置背景填充(背景色必须)
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_DASHED);
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(true);
		
		return cellStyle;
	}
	
	/**
	 * @author yaoyt 
	 * @time 2013-4-24下午4:19:41
	 * @param cellStyle
	 * @param broderStyle
	 * @param borderColor
	 */
	private void setCellBorder(CellStyle cellStyle, short broderStyle) {
		// 设置边框样式
		cellStyle.setBorderTop(broderStyle);
		cellStyle.setBorderBottom(broderStyle);
		cellStyle.setBorderLeft(broderStyle);
		cellStyle.setBorderRight(broderStyle);

		// 设置边框颜色
		//cellStyle.setTopBorderColor(borderColor);
		//cellStyle.setBorderBottom(borderColor);
		//cellStyle.setBorderLeft(borderColor);
		//cellStyle.setBorderRight(borderColor);
	}
	
	/**
	 * 删除字符串内容中的HTML标签
	 * @param htmlStr
	 * @return 处理后的字符串
	 * @author HuangMeng 20130425--10:26
	 */
	/*public static String delHTMLTag(String htmlStr){ 
		if(htmlStr != null){
			String regExScript="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
	        String regExStyle="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
	        String regExHtml="<[^>]+>"; //定义HTML标签的正则表达式 
	        
	        Pattern pScript=Pattern.compile(regExScript,Pattern.CASE_INSENSITIVE); 
	        Matcher mScript=pScript.matcher(htmlStr); 
	        htmlStr=mScript.replaceAll(""); //过滤script标签 
			Pattern pStyle=Pattern.compile(regExStyle,Pattern.CASE_INSENSITIVE); 
	        Matcher mStyle=pStyle.matcher(htmlStr); 
	        htmlStr=mStyle.replaceAll(""); //过滤style标签 
	        
	        Pattern p_html=Pattern.compile(regExHtml,Pattern.CASE_INSENSITIVE); 
	        Matcher m_html=p_html.matcher(htmlStr); 
	        htmlStr=m_html.replaceAll(""); //过滤html标签 

	       return htmlStr.trim(); //返回文本字符串 
		}
        return "";
   }*/



	/**
	 * SAP部门导出表头
	 * @author yangwei
	 * @time 2014.8.27 18:15
	 */
	public CellStyle getCellStyleSaphead() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.BLACK.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_THIN);		
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}

	/**
	 * TODO 创建文本格式单元格
	 * @author Zhaozhh
	 * @time 2015-7-28 下午3:44:34
	 * @param 
	 * @return
	 */
	public CellStyle crateTextStyleCell() {
		// 创建列样式
		CellStyle cellStyle = workbook.createCellStyle();
		// 创建字体
		Font font = workbook.createFont();
		// 设置字体颜色
		font.setColor(HSSFColor.GREY_80_PERCENT.index);
		// 设置粗体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 设置字体大小
		font.setFontHeightInPoints((short)9);
		// 设置字体行高
		//font.setFontHeight((short)21);
		// 设置不换行
		cellStyle.setWrapText(false);
		// 设置字体
		cellStyle.setFont(font);
		// 设置背景色
		cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		// 设置背景填充(背景色必须)
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置边框样式
		setCellBorder(cellStyle, CellStyle.BORDER_DASHED);		
		// 设置对齐
		cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setLocked(false);
		//设置单元格样式为文本
		cellStyle.setDataFormat(format.getFormat("@"));
		return cellStyle;
	}
	
	public Workbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public DataFormat getFormat() {
		return format;
	}

	public void setFormat(DataFormat format) {
		this.format = format;
	}

	public CellStyle getCellStyleHead() {
		return cellStyleHead;
	}

	public void setCellStyleHead(CellStyle cellStyleHead) {
		this.cellStyleHead = cellStyleHead;
	}

	public CellStyle getCellStyleSumText() {
		return cellStyleSumText;
	}

	public void setCellStyleSumText(CellStyle cellStyleSumText) {
		this.cellStyleSumText = cellStyleSumText;
	}

	public CellStyle getCellStyleSumNumber() {
		return cellStyleSumNumber;
	}

	public void setCellStyleSumNumber(CellStyle cellStyleSumNumber) {
		this.cellStyleSumNumber = cellStyleSumNumber;
	}

	public CellStyle getCellStyleDataText() {
		return cellStyleDataText;
	}

	public void setCellStyleDataText(CellStyle cellStyleDataText) {
		this.cellStyleDataText = cellStyleDataText;
	}

	public CellStyle getCellStyleDataNumber() {
		return cellStyleDataNumber;
	}

	public void setCellStyleDataNumber(CellStyle cellStyleDataNumber) {
		this.cellStyleDataNumber = cellStyleDataNumber;
	}

	public CellStyle getCellStyleDataNumberImport() {
		return cellStyleDataNumberImport;
	}

	public void setCellStyleDataNumberImport(CellStyle cellStyleDataNumberImport) {
		this.cellStyleDataNumberImport = cellStyleDataNumberImport;
	}

	public CellStyle getCellStyleEnableText() {
		return cellStyleEnableText;
	}

	public void setCellStyleEnableText(CellStyle cellStyleEnableText) {
		this.cellStyleEnableText = cellStyleEnableText;
	}

	public CellStyle getCellStyleEnableNumber() {
		return cellStyleEnableNumber;
	}

	public void setCellStyleEnableNumber(CellStyle cellStyleEnableNumber) {
		this.cellStyleEnableNumber = cellStyleEnableNumber;
	}

	public CellStyle getCellStyleDisableNumber() {
		return cellStyleDisableNumber;
	}

	public void setCellStyleDisableNumber(CellStyle cellStyleDisableNumber) {
		this.cellStyleDisableNumber = cellStyleDisableNumber;
	}

	public CellStyle getCellStyleDisableText() {
		return cellStyleDisableText;
	}

	public void setCellStyleDisableText(CellStyle cellStyleDisableText) {
		this.cellStyleDisableText = cellStyleDisableText;
	}

	public CellStyle getCellStyleBoldLeft() {
		return cellStyleBoldLeft;
	}

	public void setCellStyleBoldLeft(CellStyle cellStyleBoldLeft) {
		this.cellStyleBoldLeft = cellStyleBoldLeft;
	}

	public CellStyle getCellStyleNormalRight() {
		return cellStyleNormalRight;
	}

	public void setCellStyleNormalRight(CellStyle cellStyleNormalRight) {
		this.cellStyleNormalRight = cellStyleNormalRight;
	}
	
}
