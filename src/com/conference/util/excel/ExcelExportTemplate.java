package com.conference.util.excel;



import java.io.OutputStream;

/**
 * 灏嗘暟鎹鍑哄埌excel鎺ュ彛瀹氫箟
 * @author WangXuzheng
 *
 */
public interface ExcelExportTemplate<T> {
	/**
	 * 灏嗘暟鎹鍑轰负excel
	 * @param outputStream 鏂囦欢杈撳嚭娴�
	 * @param parameters 鍙傛暟
	 */
	public void doExport(OutputStream outputStream,T parameters)throws Exception;
	/**
	 * 瑕佸垱寤虹殑excel鏂囦欢鐨剆heet鍚嶇О
	 * @return
	 */
	public String[] getSheetNames();
	
	/**
	 * 瑕佸垱寤虹殑excel琛ㄦ牸涓殑琛ㄥご鍐呭.
	 * list涓瓨鏀剧殑鏄涓猻heet鐨勮〃澶村唴瀹�
	 * @return
	 */
	public String[][] getTitles();
	
	/**
	 * 瑕佸垱寤虹殑excel琛ㄦ牸鐨勬瘡涓猻heet鐨勮〃澶�
	 * @return
	 */
	public String[] getCaptions();
	
	/**
	 * 鎺у埗鏂囦欢鍦ㄥ唴瀛樹腑鏈�鍗犵敤澶氬皯鏉�
	 * @return
	 */
	public int getRowAccessWindowSize();
}
