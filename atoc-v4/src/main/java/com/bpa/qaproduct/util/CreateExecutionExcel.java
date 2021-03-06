package com.bpa.qaproduct.util;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import com.bpa.qaproduct.entity.ExecutionResult;
import com.bpa.qaproduct.entity.ExecutionResultDetail;

public class CreateExecutionExcel {

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
	 public void createExecutionExcel(String file,ExecutionResult executionResult) {
	 
	  try{
	 
	   Workbook wb = new XSSFWorkbook();
	   Sheet sheet = wb.createSheet("Execution Report");
	   
	   
	 
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
	   rowIndex = insertHeaderInfo(sheet, rowIndex,executionResult);
	   rowIndex = insertDetailInfo(sheet, rowIndex,executionResult);
	 
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
	private int insertHeaderInfo(Sheet sheet, int index,ExecutionResult executionResult){
	 
	  int rowIndex = 0;
	  
	  Row row = null;
	  Cell c = null;
	 
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(3);
	  c.setCellValue("Test Suite Details");
	  c.setCellStyle(csTop);
	  c.setCellStyle(csBold);
	  c = row.createCell(1);
	  c.setCellStyle(csTop);
	 
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Project Name:");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getTestSuiteExecution().getTestSuite().getProject().getProjectName());
	  c.setCellStyle(csRight);
	  
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Test Suite Name:");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getTestSuiteExecution().getTestSuite().getSuiteName());
	  c.setCellStyle(csRight);
	  
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Execution Name:");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getTestExecutionName());
	  c.setCellStyle(csRight);
	  
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Start Time:");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getStartTime().toString());
	  c.setCellStyle(csRight);
	  
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("End Time:");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getEndTime().toString());
	  c.setCellStyle(csRight);
	  
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(3);
	  c.setCellValue("Test Execution Details");
	  c.setCellStyle(csTop);
	  c.setCellStyle(csBold);
	  c = row.createCell(1);
	  c.setCellStyle(csTop);
	 
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Total Tests:");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getTotal());
	  c.setCellStyle(csRight);
	  
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Total Passed:");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getPassed());
	  c.setCellStyle(csRight);
	  
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Total Failed");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getFailed());
	  c.setCellStyle(csRight);
	  
	  rowIndex++;
	  row = sheet.createRow(rowIndex);
	  c = row.createCell(0);
	  c.setCellValue("Total Skipped");
	  c.setCellStyle(csLeft);
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue(executionResult.getSkipped());
	  c.setCellStyle(csRight);
	  
	 return rowIndex;
	 
	 }

	 private int insertDetailInfo(Sheet sheet, int index,ExecutionResult executionResult){
	  Row row = null;
	  Cell c = null;
	 		
	  Set<ExecutionResultDetail> executionResultDetailSet = executionResult.getExecutionResultDetail();
	  
	  List<ExecutionResultDetail> passedList = new ArrayList();
	  List<ExecutionResultDetail> faileList = new ArrayList();
	  List<ExecutionResultDetail> skippedList = new ArrayList();
	  
	  for(ExecutionResultDetail exDetail : executionResultDetailSet){
		  logger.info("Execution Status:::::::::::"+exDetail.getStatus());
			if(exDetail.getStatus() == "PASS" || exDetail.getStatus().equalsIgnoreCase("PASS")){
				passedList.add(exDetail);
			}
			else if(exDetail.getStatus() == "FAIL" || exDetail.getStatus().equalsIgnoreCase("FAIL")){
				faileList.add(exDetail);
			}
			else if(exDetail.getStatus() == "SKIP" || exDetail.getStatus().equalsIgnoreCase("SKIP")){
				skippedList.add(exDetail);
			}
			
	  }
	  
	  index = index + 1;
	  row = sheet.createRow(index++);
	  c = row.createCell(3);
	  c.setCellValue("Passed Details");
	  c.setCellStyle(csTop);
	  c.setCellStyle(csBold);
	  c = row.createCell(1);
	  c.setCellStyle(csTop);
	  
	  row = sheet.createRow(index++);
	  c = row.createCell(1);
	  c.setCellValue("Method Name");
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue("Start Time");
	  c.setCellStyle(csBold);
	  c = row.createCell(3);
	  c.setCellValue("End Time");
	  c.setCellStyle(csBold);
	  c = row.createCell(4);
	  c.setCellValue("Signature");
	  c.setCellStyle(csBold);
	  
	  for(ExecutionResultDetail exDetail : passedList){
				
					   row = sheet.createRow(index++);
					   c = row.createCell(1);
					   c.setCellValue(exDetail.getTestMethodName());
					   c.setCellStyle(cs);
					   c = row.createCell(2);
					   c.setCellValue(exDetail.getStartedAt().toString());
					   c.setCellStyle(cs);
					   c = row.createCell(3);
					   c.setCellValue(exDetail.getFinishedAt().toString());
					   c.setCellStyle(cs);
					   c = row.createCell(4);
					   c.setCellValue(exDetail.getSignature());
					   c.setCellStyle(cs);
					 
			
	  }
	  row = sheet.createRow(index++);
	  c = row.createCell(3);
	  c.setCellValue("Failed Details");
	  c.setCellStyle(csTop);
	  c.setCellStyle(csBold);
	  c = row.createCell(1);
	  c.setCellStyle(csTop);
		
	  row = sheet.createRow(index++);
	  c = row.createCell(1);
	  c.setCellValue("Method Name");
	  c.setCellStyle(csBold);
	  c = row.createCell(2);
	  c.setCellValue("Start Time");
	  c.setCellStyle(csBold);
	  c = row.createCell(3);
	  c.setCellValue("End Time");
	  c.setCellStyle(csBold);
	  c = row.createCell(4);
	  c.setCellValue("Signature");
	  c.setCellStyle(csBold);
	  
	  for(ExecutionResultDetail exDetail : faileList){
		
		  			   row = sheet.createRow(index++);
					   c = row.createCell(1);
					   c.setCellValue(exDetail.getTestMethodName());
					   c.setCellStyle(cs);
					   c = row.createCell(2);
					   c.setCellValue(exDetail.getStartedAt().toString());
					   c.setCellStyle(cs);
					   c = row.createCell(3);
					   c.setCellValue(exDetail.getFinishedAt().toString());
					   c.setCellStyle(cs);
					   c = row.createCell(4);
					   c.setCellValue(exDetail.getSignature());
					   c.setCellStyle(cs);
		}
	  
	  	row = sheet.createRow(index++);
	  	c = row.createCell(3);
	  	c.setCellValue("Skipped Details");
	  	c.setCellStyle(csTop);
	  	c.setCellStyle(csBold);
	  	c = row.createCell(1);
	  	c.setCellStyle(csTop);
		  
	  	row = sheet.createRow(index++);
	  	c = row.createCell(1);
	  	c.setCellValue("Method Name");
	  	c.setCellStyle(csBold);
	  	c = row.createCell(2);
	  	c.setCellValue("Start Time");
	  	c.setCellStyle(csBold);
	  	c = row.createCell(3);
	  	c.setCellValue("End Time");
	  	c.setCellStyle(csBold);
	  	c = row.createCell(4);
	  	c.setCellValue("Signature");
	  	c.setCellStyle(csBold);
	  	
		for(ExecutionResultDetail exDetail : skippedList){
			 
					   row = sheet.createRow(index++);
					   c = row.createCell(1);
					   c.setCellValue(exDetail.getTestMethodName());
					   c.setCellStyle(cs);
					   c = row.createCell(2);
					   c.setCellValue(exDetail.getStartedAt().toString());
					   c.setCellStyle(cs);
					   c = row.createCell(3);
					   c.setCellValue(exDetail.getFinishedAt().toString());
					   c.setCellStyle(cs);
					   c = row.createCell(4);
					   c.setCellValue(exDetail.getSignature());
					   c.setCellStyle(cs);
		}	
		return index;
	 }
}
