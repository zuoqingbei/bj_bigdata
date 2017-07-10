package com.conference.util;
import com.converter.docConverter.DocConverter;
import com.converter.pdfConverter.JacobPDFConverter;
import com.converter.pdfConverter.PDFConverter;
import com.converter.swfConverter.SWFConverter;
import com.converter.swfConverter.SWFToolsSWFConverter;

/**
 * 
 */

/**
 * @author YangTao
 * @date 2012-10-11
 * @version 1.0
 */
public class Converter {
	
	private static PDFConverter pdfConverter = null;
	private static SWFConverter swfConverter = null;
	private static DocConverter converter = null;
	
	static{
		pdfConverter = new JacobPDFConverter();
		swfConverter = new SWFToolsSWFConverter();
		converter = new DocConverter(pdfConverter,swfConverter);
	}
	
	/**
	 * word转pdf
	 * @author wangjinjin
	 * @date 2014-11-27上午11:16:56
	 * @param swftoolsPath
	 * @param inputFile
	 */
	public static void convertWORD2PDF(String inputFile){
		pdfConverter.convert2PDF(inputFile);
	}
	
	public static void convertWORD2PDF(String inputFile,String outputFile){
		pdfConverter.convert2PDF(inputFile,outputFile);
	}
	/**
	 * @Desc pdf转swf
	 * @author YangTao
	 * @Date 2013-1-17
	 * @param swftoolsPath
	 * @param inputFile
	 */
	public static void convertPDF2SWF(String swftoolsPath,String inputFile){
		swfConverter.convert2SWF(swftoolsPath,inputFile);
	}
	
	/**
	 * @Desc office(word,excel,ppt)转swf
	 * @author YangTao
	 * @Date 2013-1-17
	 * @param swftoolsPath
	 * @param inputFile
	 */
	public static void convertOFFICE2SWF(String swftoolsPath,String inputFile,String outPutFile){
		converter.convert(swftoolsPath, inputFile,outPutFile);
	}
	
	
	/**
	 * @Desc pdf转swf
	 * @author YangTao
	 * @Date 2013-1-17
	 * @param swftoolsPath
	 * @param inputFile
	 */
	public static void convertPDF2SWF(String swftoolsPath,String inputFile,String outPutFile){
		swfConverter.convert2SWF(swftoolsPath,inputFile,outPutFile);
	}
	
	/**
	 * @Desc office(word,excel,ppt)转swf
	 * @author YangTao
	 * @Date 2013-1-17
	 * @param swftoolsPath
	 * @param inputFile
	 */
	public static void convertOFFICE2SWF(String swftoolsPath,String inputFile){
		converter.convert(swftoolsPath, inputFile);
	}
	  
	public static void main(String[] args) throws Exception {   
//		converter.c
//		System.out.println(System.getProperty("java.library.path"));
//	//        Office2Pdf one=new Office2Pdf();   
//	//        one.createPDF("E:\\test.docx","E:\\test.pdf"); 
//		String swftoolsPath = "D:\\SWFTools\\pdf2swf.exe";
////		PDFConverter pdfConverter = new JacobPDFConverter();
////		SWFConverter swfConverter = new SWFToolsSWFConverter();
////		DocConverter converter = new DocConverter(pdfConverter,swfConverter);
//		String txtFile = "D:\\test\\txtTest.txt";
//		String docFile = "E:\\11.pdf";
//		String xlsFile = "D:\\test\\xlsTest.xlsx";
//		String pptFile = "E:\\test.pptx";
//	//	converter.convert(txtFile);
//	//	converter.convert(docFile);
//	//	converter.convert(xlsFile);
//	//	converter.convert(pptFile);
//		
//	//	swfConverter.convert2SWF("E:\\test.pdf");
//		
//		Converter.convertPDF2SWF(swftoolsPath, "E:\\1.pdf","E:\\2.swf");
//		Office2Pdf one=new Office2Pdf();   
//		        one.createPDF("E:\\test.docx","E:\\test.pdf"); 
		Converter.convertWORD2PDF("d:\\111.docx", "d:\\222.pdf");
//		 Office2Pdf one=new Office2Pdf();   
//			        one.createPDF("E:\\test.docx","E:\\test.pdf");
}}
