package com.conference.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;

/**
 * 根据文献首页生成图片
 *
 * @author Chengzl
 * @date 2014-5-5 下午04:15:47 
 * @version 1.0
 *
 */
public class PDF2ThumbRenderer {
	
	 /**
	  * 把PDF文件中的某一页转换为PNG图片
	  * @param pdfPath PDF文件所在的完整路径
	  * @param pageNumber 要转换的PDF页码
	  * @param pngSavePath 要保存PNG图片的完整路径
	  * @return 0 转换成功
	  *         1 输入的PDF页码不在有效范围内
	  *         2 转换出错
	  */
	 public static int pdf2PngByPath(String pdfPath,int pageNumber,String pngSavePath)   {
		
	      File file = new File(pdfPath); 
	      return pdf2PngByFile(file, pageNumber, pngSavePath);
	}
	 
	 
	 public static int pdf2PngByFile(File file,int pageNumber,String pngSavePath)   {
		 return pdf2PngByFile(file, pageNumber, pngSavePath,160,180);
	 }
	 
	 /**
	  * 
	  * @author Chengzl
	  * @date 2014-5-6上午09:37:27
	  * @param file   pdf File 文件
	  * @param pageNumber 要转换的PDF页码
	  * @param pngSavePath要保存PNG图片的完整路径
	  * @return 0 转换成功
	  *         1 输入的PDF页码不在有效范围内
	  *         2 转换出错
	  * @return
	  */
	 public static int pdf2PngByFile(File file,int pageNumber,String pngSavePath,int width,int height)   {
		 int rscode=0;
	     int pagen= pageNumber; 
		 PDFFile pdffile=null; 
//	       set up the PDF reading  
	      try{ 
	       RandomAccessFile raf = new RandomAccessFile(file, "r"); 
	       FileChannel channel = raf.getChannel(); 
	       ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()); 
	       pdffile = new PDFFile(buf); 
	      }catch(Exception e){
	       rscode=2;
	      e.printStackTrace();
	      } 
	     
	     //该pdf文档的总页数  
	      //System.out.println("该pdf文档的页数"+pdffile.getNumPages());
	     
	      if(pagen<1 || pagen>pdffile.getNumPages()){  //无效的页码
	       rscode=1;
	         return rscode; 
	      }
	     
	      //设置将第pagen页生成png图片  
	      PDFPage page = pdffile.getPage(pagen); 
	     
//	       创建并配置图片对象create and configure a graphics object  
//	      int width = (int)page.getBBox().getWidth(); 
//	      int height =(int)page.getBBox().getHeight(); 
//	      int width = 160; 
//	      int height =180;
	      BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
	      Graphics2D g2 = img.createGraphics(); 
	      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//	       绘制图片,红色背景 
	      PDFRenderer renderer = new PDFRenderer(page, g2,new Rectangle(0, 0, width, height), null,Color.WHITE); 
	      try{ 
	         page.waitForFinish(); 
	      }catch(Exception e){
	      rscode=2;
	         e.printStackTrace(); 
	      } 
	     
	      renderer.run(); 
	      g2.dispose(); 
	     
	      //输出图片
	      try{ 
	        ImageIO.write(img, "png", new File(pngSavePath)); 
	      }catch(Exception e){
	       rscode=2;
	       e.printStackTrace(); 
	      }
	      return rscode;
	 }
	 
	 public static void main(String[] args) {
		 PDF2ThumbRenderer.pdf2PngByFile(new File("d:/整体计划.pdf"),1,"d:/1.png",500,500);
	}

}
