package com.bpa.qaproduct.util;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
 



import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bpa.qaproduct.controller.ExecutionResultController;
import com.bpa.qaproduct.entity.ExecutionResult;
import com.bpa.qaproduct.entity.ExecutionResultDetail;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
public class CreateExcel {


	 private CellStyle cs = null;
	 private CellStyle csBold = null;
	 private CellStyle csTop = null;
	 private CellStyle csRight = null;
	 private CellStyle csBottom = null;
	 private CellStyle csLeft = null;
	 private CellStyle csTopLeft = null;
	 private CellStyle csTopRight = null;
	 private CellStyle csBottomLeft = null;
	 private CellStyle csBottomRight = null;
	 
	 protected final Log logger = LogFactory
				.getLog(CreateExcel.class);
	 public void createExecutionReport_Excel(String file,ExecutionResult executionResult,ExecutionResult executionResult_side) {
	 
	  try{
	 
	   Workbook wb = new XSSFWorkbook();
	   Sheet sheet = wb.createSheet("Comparison Report");
	 
	   //Setup some styles that we need for the Cells
	   setCellStyles(wb);
	 
	   //Get current Date and Time
	   Date date = new Date(System.currentTimeMillis());
	   DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
	 
	   //Set Column Widths
	   sheet.setColumnWidth(0, 2500); 
	   sheet.setColumnWidth(1, 2500);
	   sheet.setColumnWidth(2, 6000);
	   sheet.setColumnWidth(3, 10000);
	   sheet.setColumnWidth(4, 3000);
	 
	   //Setup the Page margins - Left, Right, Top and Bottom
	   sheet.setMargin(Sheet.LeftMargin, 0.25);
	   sheet.setMargin(Sheet.RightMargin, 0.25);
	   sheet.setMargin(Sheet.TopMargin, 0.75);
	   sheet.setMargin(Sheet.BottomMargin, 0.75);
	 
	   //Setup the Header and Footer Margins
	   sheet.setMargin(Sheet.HeaderMargin, 0.25);
	   sheet.setMargin(Sheet.FooterMargin, 0.25);
	    
	   //If you are using HSSFWorkbook() instead of XSSFWorkbook()
	   //HSSFPrintSetup ps = (HSSFPrintSetup) sheet.getPrintSetup();
	   //ps.setHeaderMargin((double) .25);
	   //ps.setFooterMargin((double) .25);
	 
	   //Set Header Information 
	   Header header = sheet.getHeader();
	   header.setLeft("*** ORIGINAL COPY ***");
	   header.setCenter(HSSFHeader.font("Arial", "Bold") +
	     HSSFHeader.fontSize((short) 14) + "SAMPLE ORDER");
	   header.setRight(df.format(date));
	 
	   //Set Footer Information with Page Numbers
	   Footer footer = sheet.getFooter();
	   footer.setRight( "Page " + HeaderFooter.page() + " of " + 
	     HeaderFooter.numPages() );
	 
	 
	   int rowIndex = 0;
	   rowIndex = insertHeaderInfo(sheet, rowIndex,executionResult ,executionResult_side);
	   rowIndex = insertDetailInfo(sheet, rowIndex,executionResult,executionResult_side);
	 
	   //Write the Excel file
	   FileOutputStream fileOut = null;
	   fileOut = new FileOutputStream(file);
	   wb.write(fileOut);
	   fileOut.close();
	 
	  }
	  catch (Exception e) {
	   System.out.println(e);
	  }
	 
	 }
	 
	 private void setCellStyles(Workbook wb) {
	 
	  //font size 10
	  Font f = wb.createFont();
	  f.setFontHeightInPoints((short) 14);
	 
	  //Simple style 
	  cs = wb.createCellStyle();
	  cs.setFont(f);
	 
	  //Bold Fond
	  Font bold = wb.createFont();
	  bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
	  bold.setFontHeightInPoints((short) 16);
	 
	  //Bold style 
	  csBold = wb.createCellStyle();
	 // csBold.setBorderBottom(CellStyle.BORDER_THIN);
	  csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	  csBold.setFont(bold);
	 
	  //Setup style for Top Border Line
	  csTop = wb.createCellStyle();
	 //csTop.setBorderTop(CellStyle.BORDER_THIN);
	  csTop.setTopBorderColor(IndexedColors.BLACK.getIndex());
	  csTop.setFont(f);
	 
	  //Setup style for Right Border Line
	  csRight = wb.createCellStyle();
	 // csRight.setBorderRight(CellStyle.BORDER_THIN);
	  csRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
	  csRight.setFont(f);
	 
	  //Setup style for Left Border Line
	  csLeft = wb.createCellStyle();
	  csLeft.setBorderLeft(CellStyle.BORDER_THIN);
	  csLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	  csLeft.setFont(f);
	 
	  //Setup style for Bottom/Left corner cell Border Lines
	  csBottomLeft = wb.createCellStyle();
	  csBottomLeft.setBorderBottom(CellStyle.BORDER_THIN);
	  csBottomLeft.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	  csBottomLeft.setBorderLeft(CellStyle.BORDER_THIN);
	  csBottomLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	  csBottomLeft.setFont(f);
	 
	  //Setup style for Bottom/Right corner cell Border Lines
	  csBottomRight = wb.createCellStyle();
	  csBottomRight.setBorderBottom(CellStyle.BORDER_THIN);
	  csBottomRight.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	  csBottomRight.setBorderRight(CellStyle.BORDER_THIN);
	  csBottomRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
	  csBottomRight.setFont(f);
	 
	 }
	 
	 /**
	 * @param sheet
	 * @param index
	 * @return
	 */
	private int insertHeaderInfo(Sheet sheet, int index,ExecutionResult executionResult,ExecutionResult executionResult_side){
	 
	  int rowIndex = index;
	  Row row = null;
	  Cell c = null;
	 
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(3);
	  c.setCellValue("Execution Comparison Report");
	  c.setCellStyle(csTop);
	  c.setCellStyle(csBold);
	  c = row.createCell(1);
	  c.setCellStyle(csTop);
	 
	  rowIndex = rowIndex + 3;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(2);
	  c.setCellValue("Execution 1 Details");
	    c.setCellStyle(cs);
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(3);
	  c.setCellValue("Execution 2 Details");
	  c.setCellStyle(csRight);
	  c.setCellStyle(csBold);
	
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Execution Name:");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getTestExecutionName());
	  c.setCellStyle(csRight);
	  c = row.createCell(3);
	  c.setCellValue(executionResult_side.getTestExecutionName());
	  c.setCellStyle(csRight);
	  
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Total Tests:");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getTotal());
	  c.setCellStyle(csRight);
	  c = row.createCell(3);
	  c.setCellValue(executionResult_side.getTotal());
	  c.setCellStyle(csRight);
	  
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Passed Tests:");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getPassed());
	  c.setCellStyle(csRight);
	  c = row.createCell(3);
	  c.setCellValue(executionResult_side.getPassed());
	  c.setCellStyle(csRight);
	  
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Failed Tests:");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getFailed());
	  c.setCellStyle(csRight);
	  c = row.createCell(3);
	  c.setCellValue(executionResult_side.getFailed());
	  c.setCellStyle(csRight);
	  
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Skipped Tests:");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getSkipped());
	  c.setCellStyle(csRight);
	  c = row.createCell(3);
	  c.setCellValue(executionResult_side.getSkipped());
	  c.setCellStyle(csRight);
	  
	  rowIndex = rowIndex + 3;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(1);
	  c.setCellValue("Method Name");
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue("Signature");
	  c.setCellStyle(csBold);
	  c = row.createCell(3);
	  c.setCellValue("Execution-1-Result");
	  c.setCellStyle(csBold);
	  c = row.createCell(4);
	  c.setCellValue("Execution-2-Result");
	  c.setCellStyle(csBold);
	 
	 return rowIndex;
	 
	 }

	 private int insertDetailInfo(Sheet sheet, int index,ExecutionResult executionResult,ExecutionResult executionResult_Side){
	 int i = 1;
	  int rowIndex = 0;
	  Row row = null;
	  Cell c = null;
	  Row row2 = null;
	  Cell c2 = null;
	  boolean isMatched = true; 
	  
	  Set<ExecutionResultDetail> executionResult_Side_set = executionResult_Side.getExecutionResultDetail();
	  for (ExecutionResultDetail exDetail : executionResult.getExecutionResultDetail()) {
			
			for (ExecutionResultDetail exDetail_Side : executionResult_Side_set) {
		  	        if(exDetail.getSignature() == exDetail_Side.getSignature() || 
		  	        		exDetail.getSignature().equals(exDetail_Side.getSignature())){
		   			 rowIndex = index + i;
					   row = sheet.createRow(rowIndex);
					   c = row.createCell(1);
					   c.setCellValue(exDetail.getTestMethodName());
					   c.setCellStyle(cs);
					   c = row.createCell(2);
					   c.setCellValue(exDetail.getSignature());
					   c.setCellStyle(cs);
					   c = row.createCell(3);
					   c.setCellValue(exDetail.getStatus());
					   c.setCellStyle(cs);
					   c = row.createCell(4);
					   c.setCellValue(exDetail_Side.getStatus());
					   c.setCellStyle(cs);
					   logger.info("### In If executionResultDetail_Side Status is :"+exDetail_Side.getStatus());
					   isMatched = false;
					   i++;;
					   //break;
		  	         }
		  	      exDetail_Side = null;
			}
		}
	  if(isMatched){
	  for (ExecutionResultDetail exDetail : executionResult.getExecutionResultDetail()) {
			
			for (ExecutionResultDetail exDetail_Side : executionResult_Side_set) {
		  	    if(!(exDetail.getSignature() == exDetail_Side.getSignature() || 
	  	        		exDetail.getSignature().equals(exDetail_Side.getSignature() ) ) ){
		   		   rowIndex = index + i;
				   row = sheet.createRow(rowIndex);
				   c = row.createCell(1);
				   c.setCellValue(exDetail.getTestMethodName());
				   c.setCellStyle(cs);
				   c = row.createCell(2);
				   c.setCellValue(exDetail.getSignature());
				   c.setCellStyle(cs);
				   c = row.createCell(3);
				   c.setCellValue(exDetail.getStatus());
				   c.setCellStyle(cs);
				   c = row.createCell(4);
				   c.setCellValue("");
				   c.setCellStyle(cs);
				   i++;
				   
				   rowIndex = index + i;
				   row2 = sheet.createRow(rowIndex);
				   c2 = row2.createCell(1);
				   c2.setCellValue(exDetail_Side.getTestMethodName());
				   c2.setCellStyle(cs);
				   c2 = row2.createCell(2);
				   c2.setCellValue(exDetail_Side.getSignature());
				   c2.setCellStyle(cs);
				   c2 = row2.createCell(3);
				   c2.setCellValue("");
				   c2.setCellStyle(cs);
				   c2 = row2.createCell(4);
				   c2.setCellValue(exDetail_Side.getStatus());
				   c2.setCellStyle(cs);
				   executionResult_Side_set.remove(exDetail_Side);
				   i++;
				   logger.info("### In Else First executionResultDetail_Side Status is :"+exDetail_Side.getStatus());
		  	         }
		  	exDetail_Side = null;
		  	    break;
		  	       
			}
		}
	  }
	  
	  
	  return rowIndex;
	 }
}
