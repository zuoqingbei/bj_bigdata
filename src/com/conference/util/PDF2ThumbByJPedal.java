package com.conference.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;

/**
 * 
 *
 * @author amen  
 * @date 2014-5-13 下午05:09:36 
 * @version 1.0
 *
 */
public class PDF2ThumbByJPedal {
	

	
	/**
	 * paf2png
	 * @author amen
	 * @date 2014-5-13下午05:09:41
	 * 把PDF文件中的某一页转换为PNG图片
	 * @param pdfPath PDF文件所在的完整路径
	 * @param pageNumber 要转换的PDF页码
	 * @param pngSavePath 要保存PNG图片的完整路径
	 * @return 0 转换成功
	 *         1 输入的PDF页码不在有效范围内
	 *         2 转换出错
	 */
	public static int pdf2img(String file_path,int page_number,String out_path){
		int rscode=0;
		PdfDecoder decode_pdf = new PdfDecoder(true);
		try {
			decode_pdf.openPdfFile(file_path);
		} catch (PdfException e) {
			// TODO Auto-generated catch block
			rscode = 2;
		}
	
		decode_pdf.setExtractionMode(0, 0.25f);
		FontMappings.setFontReplacements();
		
		if(page_number > decode_pdf.getPageCount() || page_number < 1){
			rscode = 1;
			
		}else{
			
			try {
				
				BufferedImage img_to_save = decode_pdf.getPageAsImage(page_number);
			
				BufferedImage img = img_to_save;
				int w = 160;
				int h = 180;

				img_to_save = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
				
				Graphics2D g2 = img_to_save.createGraphics();
				g2.setPaint(Color.WHITE);
				g2.fillRect(0, 0, w, h);
				g2.drawImage(img, 0, 0, null);
			    decode_pdf.getObjectStore().saveStoredImage(out_path, img_to_save, true, true, "png");
	            decode_pdf.flushObjectValues(true);

			} catch (PdfException e1) {
				// TODO Auto-generated catch block
				rscode =  2;
			}
			
		}
		
		decode_pdf.closePdfFile();
		return rscode;
		
	}
}
