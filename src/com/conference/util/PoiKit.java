package com.conference.util;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.base.Preconditions;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

public class PoiKit {

    public static final int HEADER_ROW = 1;
    public static final int DEFAULT_CELL_WIDTH = 8000;
    private static final String DEFAULT_SHEET_NAME = "new sheet";
    private static final int MAX_ROWS = 65536;

    /**
	 * 样式列表
	 */
	private static Map<String, CellStyle> styles;
	
    /**
     * 
     * @param sheetName
     *            sheet名称
     * @param cellWidth
     *            设置单元格宽度
     * @param headerRow
     *            设置头列占的行数
     * @param headers
     *            头列值
     * @param columns
     *            列key(即 list<Map<String ,Ojbect>> 中 map的key)
     * @param list
     *            数据
     * @return
     */

    public static HSSFWorkbook export(String sheetName, int cellWidth, int headerRow, String[] headers,
            String[] columns, List<?> list, int columnNum, boolean hasHeaders) {
        Preconditions.checkNotNull(headers, "headers can not be null");
        Preconditions.checkNotNull(columns, "columns can not be null");
//        Preconditions.checkArgument(cellWidth <= 0, "cellWidth < 0");
        if (StringKit.isBlank(sheetName)) {
            sheetName = DEFAULT_SHEET_NAME;
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        HSSFRow row = null;
        HSSFCell cell = null;
        setCellWidth(sheet, cellWidth, columnNum);
        styles = createStyles(wb);
        if (hasHeaders) {
            row = sheet.createRow(0);
            if (headerRow <= 0) {
                headerRow = HEADER_ROW;
            }
            headerRow = Math.min(headerRow, MAX_ROWS);
            for (int h = 0, lenH = headers.length; h < lenH; h++) {
                Region region = new Region(0, (short) h, (short) headerRow - 1, (short) h);// 合并从第rowFrom行columnFrom列
                sheet.addMergedRegion(region);// 到rowTo行columnTo的区域
                // 得到所有区域
                sheet.getNumMergedRegions();
                sheet.setColumnWidth(h, cellWidth);
                cell = row.createCell(h);
                cell.setCellValue(headers[h]);
                cell.setCellStyle(styles.get("header"));
            }
        }
        if (list.size() == 0) {
            return wb;
        }
        for (int i = 0, len = list.size(); i < len; i++) {
            row = sheet.createRow(i + headerRow);
            Object obj = list.get(i);
            if (obj == null) {
                continue;
            }
            if (obj instanceof Map) {
                processAsMap(columns, row, obj);
            } else if (obj instanceof Model) {
                processAsModel(columns, row, obj);
            } else if (obj instanceof Record) {
                processAsRecord(columns, row, obj);
            }
        }
        return wb;
    }

    private static void processAsRecord(String[] columns, HSSFRow row, Object obj) {
        HSSFCell cell;
        Record record = (Record) obj;
        Map<String, Object> map = record.getColumns();
        if (columns.length == 0) {// 未设置显示列，默认全部
            record.getColumns();
            Set<String> keys = map.keySet();
            int columnIndex = 0;
            for (String key : keys) {
                cell = row.createCell(columnIndex);
                cell.setCellValue(record.get(key)==null?"":record.get(key) + "");
                columnIndex++;
            }
        } else {
            for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
                cell = row.createCell(j);
                cell.setCellValue(map.get(columns[j])==null?"":map.get(columns[j]) + "");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void processAsMap(String[] columns, HSSFRow row, Object obj) {
        HSSFCell cell;
        Map<String, Object> map = (Map<String, Object>) obj;
        if (columns.length == 0) {// 未设置显示列，默认全部
            Set<String> keys = map.keySet();
            int columnIndex = 0;
            for (String key : keys) {
                cell = row.createCell(columnIndex);
                cell.setCellValue(map.get(key) + "");
                columnIndex++;
            }
        } else {
            for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
                cell = row.createCell(j);
                cell.setCellValue(map.get(columns[j]) + "");
            }
        }
    }

    private static void processAsModel(String[] columns, HSSFRow row, Object obj) {
        HSSFCell cell;
        Model<?> model = (Model<?>) obj;
        Set<Entry<String, Object>> entries = model._getAttrsEntrySet();
        if (columns.length == 0) {// 未设置显示列，默认全部
            int columnIndex = 0;
            for (Entry<String, Object> entry : entries) {
                cell = row.createCell(columnIndex);
                cell.setCellValue(entry.getValue() + "");
                columnIndex++;
            }
        } else {
            for (int j = 0, lenJ = columns.length; j < lenJ; j++) {
                cell = row.createCell(j);
                cell.setCellValue(model.get(columns[j]) + "");
            }
        }
    }

    /**
     * 设置单元格宽度
     * 
     * @param sheet
     * @param cellWidth
     * @param columnNum
     */
    private static void setCellWidth(HSSFSheet sheet, int cellWidth, int columnNum) {
        for (int i = 0; i < columnNum; i++) {
//            sheet.setColumnWidth(i, cellWidth);
            sheet.autoSizeColumn(i);
        }

    }

    /**
     * @param String
     *            sheetName sheet名称
     * @param int headerRow 设置头列占的行数
     * @param String
     *            [] headers 头列值
     * @param String
     *            [] columns 列key(即 list<Map<String ,Ojbect>> 中 map的key)
     * @param List
     *            <Map<String, Object>> list 数据
     * @param int cellWidth 设置单元格宽度
     * @return
     */
    public static HSSFWorkbook export(String sheetName, int headerRow, String[] headers, String[] columns,
            List<Object> list, int cellWidth) {
        boolean hasHeaders = false;
        int columnNum = Math.max(0, columns.length);
        if (headers.length > 0) {
            hasHeaders = true;
            columnNum = headers.length;
        }
        return export(sheetName, cellWidth, headerRow, headers, columns, list, columnNum, hasHeaders);

    }
    
    
    
    
    /**
	 * 创建表格样式
	 * @param wb 工作薄对象
	 * @return 样式列表
	 */
	private static Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font titleFont = wb.createFont();
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 16);
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(titleFont);
		styles.put("title", style);

		style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
		Font dataFont = wb.createFont();
		dataFont.setFontName("Arial");
		dataFont.setFontHeightInPoints((short) 10);
		style.setFont(dataFont);
		styles.put("data", style);
		
		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_LEFT);
		styles.put("data1", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_CENTER);
		styles.put("data2", style);

		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		styles.put("data3", style);
		
		style = wb.createCellStyle();
		style.cloneStyleFrom(styles.get("data"));
//		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = wb.createFont();
		headerFont.setFontName("Arial");
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(headerFont);
		styles.put("header", style);
		
		return styles;
	}
}

