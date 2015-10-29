package com.bpa.qaproduct.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bpa.qaproduct.controller.ExecutionResultController;
import com.bpa.qaproduct.entity.ExecutionResult;
import com.bpa.qaproduct.entity.ExecutionResultDetail;
import com.bpa.qaproduct.entity.TestSuiteExecution;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class CreatePDF {

	protected static final Log logger = LogFactory
			.getLog(CreatePDF.class);

	private static Font TIME_ROMAN = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.BOLD);
	private static Font TIME_ROMAN_NORMAL = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.NORMAL);
	private static Font TIME_ROMAN_SMALL = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	private static Font FONT_TIMES_16_BLUE = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, new BaseColor(20, 75, 149));
	private static Font FONT_TIMES_22_BLUE = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD, new BaseColor(20, 75, 149));
	
	private static float[] columnWidths = new float[] {20f, 40f, 20f,20f};
	private static float[] columnWidths_head = new float[] {20f, 40f, 40f};
	
	/**
	 * @param args
	 */
	public static Document createPDF(String file,ExecutionResult executionResult,ExecutionResult executionResult_side) {

		Document document = null;

		try {
			
			document = new Document(PageSize.A4.rotate());
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			
			Paragraph preface = new Paragraph();
			String logo_path = "webapps/atoc-v4/resources/images/atoc_logo.png";
			Image image = Image.getInstance(logo_path);
			image.setAlignment(Image.MIDDLE);
			PdfPTable tableEx = new PdfPTable(1);
			tableEx.setWidthPercentage(100);
			PdfPCell c123 = new PdfPCell();
			c123.setMinimumHeight(70);
			Paragraph p = new Paragraph();
			p.add(new Chunk(image,340f,-15.0f));
			
	          c123.setBorder(Rectangle.NO_BORDER);
	          c123.setVerticalAlignment(Element.ALIGN_MIDDLE);
	          c123.setBackgroundColor(new BaseColor(20, 75, 149));
	          
	          c123.addElement(p);
	          
	          tableEx.addCell(c123);                                     
	                  
			 
            document.add(tableEx);
            preface.setAlignment(Element.ALIGN_CENTER);
            preface.setFont(FONT_TIMES_22_BLUE);
            preface.add("Results Comparison"); 
            creteEmptyLine(preface, 1);
			preface.add(new Paragraph("Execution Comparison Report", FONT_TIMES_16_BLUE));
 			creteEmptyLine(preface, 1);
 			
 			document.add(preface);
 			creteEmptyLine(preface, 1);
 			
 			PdfPTable table = new PdfPTable(4);
 			table.setWidthPercentage(100);
 			table.getDefaultCell().setBorderWidth(0f);
 			
 			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
 			
 			PdfPCell cellOne = new PdfPCell(new Phrase(""));
 			cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
 			cellOne.setBorder(Rectangle.NO_BORDER);
 			table.addCell(cellOne);
 			
 			PdfPCell cellTwo = new PdfPCell(new Phrase(""));
 			cellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
 			cellTwo.setBorder(Rectangle.NO_BORDER);
 			table.addCell(cellTwo);
 			
 			PdfPCell cellThree = new PdfPCell(new Phrase("Execution-1 Details",TIME_ROMAN));
 			cellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
 			cellThree.setBorder(Rectangle.NO_BORDER);
 			table.addCell(cellThree);
 			
 			PdfPCell cellFour = new PdfPCell(new Phrase("Execution-2 Details",TIME_ROMAN));
 			cellFour.setHorizontalAlignment(Element.ALIGN_CENTER);
 			cellFour.setBorder(Rectangle.NO_BORDER);
 			table.addCell(cellFour);
 			
 			table.addCell(new Paragraph(0,"Execution Name",TIME_ROMAN));
 			table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			table.addCell(new Paragraph(0,executionResult.getTestExecutionName(),TIME_ROMAN_NORMAL));
 			table.addCell(new Paragraph(0,executionResult_side.getTestExecutionName(),TIME_ROMAN_NORMAL));
 			
 			
 			table.addCell(new Paragraph(0,"Total Tests",TIME_ROMAN));
 			table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			table.addCell(new Paragraph(0,""+executionResult.getTotal(),TIME_ROMAN_NORMAL));
 			table.addCell(new Paragraph(0,""+executionResult_side.getTotal(),TIME_ROMAN_NORMAL));
 			
 			table.addCell(new Paragraph(0,"Passed Tests",TIME_ROMAN));
 			table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			table.addCell(new Paragraph(0,""+executionResult.getPassed(),TIME_ROMAN_NORMAL));
 			table.addCell(new Paragraph(0,""+executionResult_side.getPassed(),TIME_ROMAN_NORMAL));
 			
 			table.addCell(new Paragraph(0,"Failed Tests",TIME_ROMAN));
 			table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			table.addCell(new Paragraph(0,""+executionResult.getFailed(),TIME_ROMAN_NORMAL));
 			table.addCell(new Paragraph(0,""+executionResult_side.getFailed(),TIME_ROMAN_NORMAL));
 			
 			table.addCell(new Paragraph(0,"Skipped Tests",TIME_ROMAN));
 			table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			table.addCell(new Paragraph(0,""+executionResult.getSkipped(),TIME_ROMAN_NORMAL));
 			table.addCell(new Paragraph(0,""+executionResult_side.getSkipped(),TIME_ROMAN_NORMAL));
 			
 			
 			document.add(table);
 			
 			creteEmptyLine(preface, 1);
 			createExecutionResultTable(document,executionResult,executionResult_side);
 			document.close();
 			

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;

	}

	private static void creteEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
	
public static Document createExecutionResultAttachment(String file,ExecutionResult executionResult) {
		
		/*logger.info("executionResult is :"
				+ executionResult.getExecutionResultId());
		logger.info("Execution result is :"
				+ executionResult.getTestSuiteExecution().getTestSuite().getProject().getProjectName()+ " : "
				+ executionResult.getTestSuiteExecution().getTestSuite().getSuiteName() + " : "
				+ executionResult.getStartTime() + " : "
				+ executionResult.getEndTime());
		logger.info("Execution report List");
		logger.info("Execution Passed"+executionResult.getPassed());
		logger.info("Execution Passed"+executionResult.getFailed());
		logger.info("Execution Passed"+executionResult.getSkipped());
		logger.info("Execution Result detail is"+executionResult.getExecutionResultDetails().size());
		*/
		Document document = new Document();
			try
			{
				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		         document.open();
		         Paragraph preface = new Paragraph();
					String logo_path = "webapps/atoc-v4/resources/images/atoc_logo.png";
					Image image = Image.getInstance(logo_path);
					PdfPTable table_headr = new PdfPTable(1);
					table_headr.setWidthPercentage(100);
					PdfPCell cell_headr = new PdfPCell();
					cell_headr.setMinimumHeight(100);
					Paragraph para = new Paragraph();
					para.add(new Chunk(image,0,0));
					
					cell_headr.setBorder(Rectangle.NO_BORDER);
					cell_headr.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell_headr.setBackgroundColor(new BaseColor(20, 75, 149));
			          
					cell_headr.addElement(para);
			          
					table_headr.addCell(cell_headr);                                     
			                  
					document.add(table_headr);
		         
		            preface.setAlignment(Element.ALIGN_CENTER);
		            preface.setFont(FONT_TIMES_22_BLUE);
		            preface.add("Test Execution Details"); 
		            
					preface.add(new Paragraph("Test Execution", FONT_TIMES_16_BLUE));
		 			creteEmptyLine(preface, 1);
		 			
		 			document.add(preface);
		 			creteEmptyLine(preface, 1);
		 			
		 			
		 			PdfPTable test_table_exec = new PdfPTable(3);
		 			test_table_exec.setWidthPercentage(100);
		 			test_table_exec.getDefaultCell().setBorderWidth(0f);
		 			
		 			PdfPCell cellOne = new PdfPCell(new Phrase(""));
		 			cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
		 			cellOne.setBorder(Rectangle.NO_BORDER);
		 			test_table_exec.addCell(cellOne);
		 			
		 			PdfPCell cellTwo = new PdfPCell(new Phrase(""));
		 			cellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
		 			cellTwo.setBorder(Rectangle.NO_BORDER);
		 			test_table_exec.addCell(cellTwo);
		 			
		 			PdfPCell cellThree = new PdfPCell(new Phrase(""));
		 			cellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
		 			cellThree.setBorder(Rectangle.NO_BORDER);
		 			test_table_exec.addCell(cellThree);
		 			
		 			test_table_exec.addCell(new Paragraph(0,"Execution Name",TIME_ROMAN));
		 			test_table_exec.addCell(new Paragraph(0,":",TIME_ROMAN));
		 			test_table_exec.addCell(new Paragraph(0,executionResult.getTestExecutionName(),TIME_ROMAN_NORMAL));
		 			
		 			test_table_exec.addCell(new Paragraph(0,"Project Name",TIME_ROMAN));
		 			test_table_exec.addCell(new Paragraph(0,":",TIME_ROMAN));
		 			test_table_exec.addCell(new Paragraph(0,executionResult.getTestSuiteExecution().getTestSuite().getProject().getProjectName(),TIME_ROMAN_NORMAL));
		 			
		 			test_table_exec.addCell(new Paragraph(0,"Start Time",TIME_ROMAN));
		 			test_table_exec.addCell(new Paragraph(0,":",TIME_ROMAN));
		 			test_table_exec.addCell(new Paragraph(0,""+executionResult.getStartTime(),TIME_ROMAN_NORMAL));
		 			
		 			test_table_exec.addCell(new Paragraph(0,"End Time",TIME_ROMAN));
		 			test_table_exec.addCell(new Paragraph(0,":",TIME_ROMAN));
		 			test_table_exec.addCell(new Paragraph(0,""+executionResult.getEndTime(),TIME_ROMAN_NORMAL));
		 			
		 			
		 			document.add(test_table_exec);
		 			Paragraph test_exec = new Paragraph();
		 			test_exec.add(new Paragraph("Test Execution Details", FONT_TIMES_16_BLUE));
		 			creteEmptyLine(test_exec, 1);
		 			
		 			document.add(test_exec);
		 			creteEmptyLine(test_exec, 1);
		 			
		 			PdfPTable table_exec = new PdfPTable(3);
		 			table_exec.setWidthPercentage(100);
		 			table_exec.getDefaultCell().setBorderWidth(0f);
		 			
		 			PdfPCell exec_cellOne = new PdfPCell(new Phrase(""));
		 			exec_cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
		 			exec_cellOne.setBorder(Rectangle.NO_BORDER);
		 			table_exec.addCell(exec_cellOne);
		 			
		 			PdfPCell exec_cellTwo = new PdfPCell(new Phrase(""));
		 			exec_cellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
		 			exec_cellTwo.setBorder(Rectangle.NO_BORDER);
		 			table_exec.addCell(exec_cellTwo);
		 			
		 			PdfPCell exec_cellThree = new PdfPCell(new Phrase(""));
		 			exec_cellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
		 			exec_cellThree.setBorder(Rectangle.NO_BORDER);
		 			table_exec.addCell(exec_cellThree);
		 			
		 			table_exec.addCell(new Paragraph(0,"Total Tests",TIME_ROMAN));
		 			table_exec.addCell(new Paragraph(0,":",TIME_ROMAN));
		 			table_exec.addCell(new Paragraph(0,""+executionResult.getTotal(),TIME_ROMAN_NORMAL));
		 			
		 			table_exec.addCell(new Paragraph(0,"Passed Tests",TIME_ROMAN));
		 			table_exec.addCell(new Paragraph(0,":",TIME_ROMAN));
		 			table_exec.addCell(new Paragraph(0,""+executionResult.getPassed(),TIME_ROMAN_NORMAL));
		 			
		 			table_exec.addCell(new Paragraph(0,"Failed Tests",TIME_ROMAN));
		 			table_exec.addCell(new Paragraph(0,":",TIME_ROMAN));
		 			table_exec.addCell(new Paragraph(0,""+executionResult.getSkipped(),TIME_ROMAN_NORMAL));
		 			
		 			table_exec.addCell(new Paragraph(0,"Skipped Tests",TIME_ROMAN));
		 			table_exec.addCell(new Paragraph(0,":",TIME_ROMAN));
		 			table_exec.addCell(new Paragraph(0,""+executionResult.getSkipped(),TIME_ROMAN_NORMAL));
		 			
		 			
		 			document.add(table_exec);
				       
				    Paragraph paragraph = new Paragraph();
					paragraph.add(new Paragraph("Passed Results", FONT_TIMES_16_BLUE));
					document.add(paragraph);
					creteEmptyLine(paragraph, 1);
					PdfPTable table3 = new PdfPTable(4);
				    
				    PdfPCell c1 = new PdfPCell(new Phrase("TEST NAME",TIME_ROMAN_SMALL));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setColspan (1); 
					c1.setPadding (5.0f);
				    c1.setBackgroundColor (new BaseColor (9, 151, 223));
				    table3.setSpacingBefore(30.0f); 
				    table3.setSpacingAfter(30.0f);		
					table3.addCell(c1);
			 
					PdfPCell c2 = new PdfPCell(new Phrase("START TIME",TIME_ROMAN_SMALL));
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);
					c2.setColspan (1); 
					c2.setPadding (5.0f);
				    c2.setBackgroundColor (new BaseColor (9, 151, 223));
					table3.addCell(c2);
					
					PdfPCell c3 = new PdfPCell(new Phrase("END TIME",TIME_ROMAN_SMALL));
					c3.setHorizontalAlignment(Element.ALIGN_CENTER);
					c3.setColspan (1); 
					c3.setPadding (5.0f);
				    c3.setBackgroundColor (new BaseColor (9, 151, 223));
					table3.addCell(c3);
			 
			 
					PdfPCell c4 = new PdfPCell(new Phrase("SIGNATURE",TIME_ROMAN_SMALL));
					c4.setHorizontalAlignment(Element.ALIGN_CENTER);
					c4.setColspan(1); 
					c4.setPadding (5.0f);
				    c4.setBackgroundColor (new BaseColor (9, 151, 223));
					table3.addCell(c4);
				    Set<ExecutionResultDetail> executionResultDetailSet = executionResult.getExecutionResultDetails();
					for(ExecutionResultDetail exDetail : executionResultDetailSet){
						
						//logger.info("Execution Result detail set loop");
						if(exDetail.getStatus() == "PASS" || exDetail.getStatus().equalsIgnoreCase("PASS")){
							// for table to repeat PASS 
							PdfPCell cell1 = null; PdfPCell cell2 = null; PdfPCell cell3 = null; PdfPCell cell4 = null;
							
								table3.setWidthPercentage(100);
								table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
								table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
								 cell1 = new PdfPCell(new Paragraph(exDetail.getTestMethodName()));
								 cell2 = new PdfPCell(new Paragraph(exDetail.getStartedAt().toString()));
								 cell3 = new PdfPCell(new Paragraph(exDetail.getFinishedAt().toString()));
						         cell4 = new PdfPCell(new Paragraph(exDetail.getSignature()));
								 table3.addCell(cell1);
								 table3.addCell(cell2);
								 table3.addCell(cell3);
								 table3.addCell(cell4);
							}
						
					}
					
					table3.setWidths(columnWidths);
					document.add(table3);
				    
					
					Paragraph failed_paragraph = new Paragraph();
					failed_paragraph.add(new Paragraph("Failed Results", FONT_TIMES_16_BLUE));
					document.add(failed_paragraph);
					creteEmptyLine(failed_paragraph, 1);
					PdfPTable failed_table = new PdfPTable(4);
					
					PdfPCell failed_col1 = new PdfPCell(new Phrase("TEST NAME",TIME_ROMAN_SMALL));
					failed_col1.setHorizontalAlignment(Element.ALIGN_CENTER);
					failed_col1.setColspan (1); 
					failed_col1.setPadding (5.0f);
					failed_col1.setBackgroundColor (new BaseColor (9, 151, 223));
					failed_table.setSpacingBefore(30.0f); 
					failed_table.setSpacingAfter(30.0f);		
					failed_table.addCell(failed_col1);
			 
					PdfPCell failed_col2 = new PdfPCell(new Phrase("START TIME",TIME_ROMAN_SMALL));
					failed_col2.setHorizontalAlignment(Element.ALIGN_CENTER);
					failed_col2.setColspan (1); 
					failed_col2.setPadding (5.0f);
					failed_col2.setBackgroundColor (new BaseColor (9, 151, 223));
					failed_table.addCell(failed_col2);
					
					PdfPCell failed_col3 = new PdfPCell(new Phrase("END TIME",TIME_ROMAN_SMALL));
					failed_col3.setHorizontalAlignment(Element.ALIGN_CENTER);
					failed_col3.setColspan (1); 
					failed_col3.setPadding (5.0f);
					failed_col3.setBackgroundColor (new BaseColor (9, 151, 223));
				    failed_table.addCell(failed_col3);
			 
			 
					PdfPCell failed_col4 = new PdfPCell(new Phrase("SIGNATURE",TIME_ROMAN_SMALL));
					failed_col4.setHorizontalAlignment(Element.ALIGN_CENTER);
					failed_col4.setColspan(1); 
					failed_col4.setPadding (5.0f);
					failed_col4.setBackgroundColor (new BaseColor (9, 151, 223));
				    failed_table.addCell(failed_col4);
				    for(ExecutionResultDetail exDetail : executionResultDetailSet){
						if(exDetail.getStatus() == "FAIL" || exDetail.getStatus().equalsIgnoreCase("FAIL")){
					PdfPCell failed_cell1 = null; PdfPCell failed_cell2 = null; PdfPCell failed_cell3 = null; PdfPCell failed_cell4 = null;
				
					failed_table.setWidthPercentage(100);
					failed_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					failed_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
					failed_cell1 = new PdfPCell(new Paragraph(exDetail.getTestMethodName()));
					failed_cell2 = new PdfPCell(new Paragraph(exDetail.getStartedAt().toString()));
					failed_cell3 = new PdfPCell(new Paragraph(exDetail.getFinishedAt().toString()));
					failed_cell4 = new PdfPCell(new Paragraph(exDetail.getSignature()));
				         failed_table.addCell(failed_cell1);
				         failed_table.addCell(failed_cell2);
				         failed_table.addCell(failed_cell3);
				         failed_table.addCell(failed_cell4);
					}
				    }
					//table.setKeepTogether(true);
					failed_table.setWidths(columnWidths);
					document.add(failed_table);
				    

					Paragraph skipped_paragraph = new Paragraph();
					skipped_paragraph.add(new Paragraph("Skipped Results", FONT_TIMES_16_BLUE));
					document.add(skipped_paragraph);
					creteEmptyLine(skipped_paragraph, 1);
					PdfPTable skipped_table = new PdfPTable(4);
					
					PdfPCell skipped_col1 = new PdfPCell(new Phrase("TEST NAME",TIME_ROMAN_SMALL));
					skipped_col1.setHorizontalAlignment(Element.ALIGN_CENTER);
					skipped_col1.setColspan (1); 
					skipped_col1.setPadding (5.0f);
					skipped_col1.setBackgroundColor (new BaseColor (9, 151, 223));
					skipped_table.setSpacingBefore(30.0f); 
					skipped_table.setSpacingAfter(30.0f);		
					skipped_table.addCell(skipped_col1);
			 
					PdfPCell skipped_col2 = new PdfPCell(new Phrase("START TIME",TIME_ROMAN_SMALL));
					skipped_col2.setHorizontalAlignment(Element.ALIGN_CENTER);
					skipped_col2.setColspan (1); 
					skipped_col2.setPadding (5.0f);
					skipped_col2.setBackgroundColor (new BaseColor (9, 151, 223));
					skipped_table.addCell(skipped_col2);
					
					PdfPCell skipped_col3 = new PdfPCell(new Phrase("END TIME",TIME_ROMAN_SMALL));
					skipped_col3.setHorizontalAlignment(Element.ALIGN_CENTER);
					skipped_col3.setColspan (1); 
					skipped_col3.setPadding (5.0f);
					skipped_col3.setBackgroundColor (new BaseColor (9, 151, 223));
					skipped_table.addCell(skipped_col3);
			 
			 
					PdfPCell skipped_col4 = new PdfPCell(new Phrase("SIGNATURE",TIME_ROMAN_SMALL));
					skipped_col4.setHorizontalAlignment(Element.ALIGN_CENTER);
					skipped_col4.setColspan(1); 
					skipped_col4.setPadding (5.0f);
					skipped_col4.setBackgroundColor (new BaseColor (9, 151, 223));
					skipped_table.addCell(skipped_col4);
					
					
					PdfPCell skipped_cell1 = null; PdfPCell skipped_cell2 = null; PdfPCell skipped_cell3 = null; PdfPCell skipped_cell4 = null;
					for(ExecutionResultDetail exDetail : executionResultDetailSet){
						if(exDetail.getStatus() == "SKIP" || exDetail.getStatus().equalsIgnoreCase("SKIP")){
					skipped_table.setWidthPercentage(100);
					skipped_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					skipped_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
					skipped_cell1 = new PdfPCell(new Paragraph(exDetail.getTestMethodName()));
					skipped_cell2 = new PdfPCell(new Paragraph(exDetail.getStartedAt().toString()));
					skipped_cell3 = new PdfPCell(new Paragraph(exDetail.getFinishedAt().toString()));
					skipped_cell4 = new PdfPCell(new Paragraph(exDetail.getSignature()));
					skipped_table.addCell(skipped_cell1);
					skipped_table.addCell(skipped_cell2);
					skipped_table.addCell(skipped_cell3);
					skipped_table.addCell(skipped_cell4);
					}
					}
					skipped_table.setWidths(columnWidths);
					document.add(skipped_table);
				    
		         document.close();
		         writer.close();
			}
			catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return document;
	}
	
	public static Document createExecutionResultPDF(String file,ExecutionResult executionResult) {

		
		Document document = null;

		try {
			
			document = new Document(PageSize.A4.rotate());
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			Paragraph preface = new Paragraph();
			String logo_path = "webapps/atoc-v4/resources/images/atoc_logo.png";
			Image image = Image.getInstance(logo_path);
			PdfPTable tableEx = new PdfPTable(1);
			tableEx.setWidthPercentage(100);
			PdfPCell c123 = new PdfPCell();
			c123.setMinimumHeight(70);
			Paragraph p = new Paragraph();
			p.add(new Chunk(image,340f,-15.0f));
			
	          c123.setBorder(Rectangle.NO_BORDER);
	          c123.setVerticalAlignment(Element.ALIGN_MIDDLE);
	          c123.setBackgroundColor(new BaseColor(20, 75, 149));
	          
	          c123.addElement(p);
	          
	          tableEx.addCell(c123);                                     
	                  
			 
            document.add(tableEx);
            preface.setAlignment(Element.ALIGN_CENTER);
            preface.setFont(FONT_TIMES_22_BLUE);
            preface.add("Execution Results"); 
            creteEmptyLine(preface, 1);
			
			
			preface.add(new Paragraph("Test Suite Details", FONT_TIMES_16_BLUE));
 			
 			
 			document.add(preface);
 			creteEmptyLine(preface, 1);
 			
 			PdfPTable table = new PdfPTable(3);
 			table.setWidthPercentage(100);
 			table.getDefaultCell().setBorderWidth(0f);
 			//table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
 			
 			PdfPCell cellOne = new PdfPCell(new Phrase(""));
 			cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
 			cellOne.setBorder(Rectangle.NO_BORDER);
 			table.addCell(cellOne);
 			
 			PdfPCell cellTwo = new PdfPCell(new Phrase(""));
 			cellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
 			cellTwo.setBorder(Rectangle.NO_BORDER);
 			table.addCell(cellTwo);
 			
 			PdfPCell cellThree = new PdfPCell(new Phrase(""));
 			cellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
 			cellThree.setBorder(Rectangle.NO_BORDER);
 			table.addCell(cellThree);
 			
 			table.addCell(new Paragraph(0,"Project Name",TIME_ROMAN));
 			table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			table.addCell(new Paragraph(0,executionResult.getTestSuiteExecution().getTestSuite().getProject().getProjectName(),TIME_ROMAN_NORMAL));
 			
 			table.addCell(new Paragraph(0,"Test Suite Name",TIME_ROMAN));
 			table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			table.addCell(new Paragraph(0,executionResult.getTestSuiteExecution().getTestSuite().getSuiteName(),TIME_ROMAN_NORMAL));
 			
 			table.addCell(new Paragraph(0,"Execution Name",TIME_ROMAN));
 			table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			table.addCell(new Paragraph(0,executionResult.getTestExecutionName(),TIME_ROMAN_NORMAL));
 			
 			table.addCell(new Paragraph(0,"Start Time",TIME_ROMAN));
 			table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			table.setHorizontalAlignment(Element.ALIGN_CENTER);
 			table.addCell(new Paragraph(0,""+executionResult.getStartTime(),TIME_ROMAN_NORMAL));
 			
 			table.addCell(new Paragraph(0,"End Time",TIME_ROMAN));
 			table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			table.setHorizontalAlignment(Element.ALIGN_CENTER);
 			table.addCell(new Paragraph(0,""+executionResult.getEndTime(),TIME_ROMAN_NORMAL));
 			
 			
			document.add(table);
			
			Paragraph exeDetail_paragraph = new Paragraph();
			exeDetail_paragraph.add(new Paragraph("Test Execution Details", FONT_TIMES_16_BLUE));
 			
 			document.add(exeDetail_paragraph);
 			creteEmptyLine(exeDetail_paragraph, 1);
 			
 			PdfPTable exec_table = new PdfPTable(3);
 			exec_table.setWidthPercentage(100);
 			exec_table.getDefaultCell().setBorderWidth(0f);
 			
 			PdfPCell exec_cellOne = new PdfPCell(new Phrase(""));
 			exec_cellOne.setHorizontalAlignment(Element.ALIGN_CENTER);
 			exec_cellOne.setBorder(Rectangle.NO_BORDER);
 			exec_table.addCell(exec_cellOne);
 			
 			PdfPCell exec_cellTwo = new PdfPCell(new Phrase(""));
 			exec_cellTwo.setHorizontalAlignment(Element.ALIGN_CENTER);
 			exec_cellTwo.setBorder(Rectangle.NO_BORDER);
 			exec_table.addCell(exec_cellTwo);
 			
 			PdfPCell exec_cellThree = new PdfPCell(new Phrase(""));
 			exec_cellThree.setHorizontalAlignment(Element.ALIGN_CENTER);
 			exec_cellThree.setBorder(Rectangle.NO_BORDER);
 			exec_table.addCell(exec_cellThree);
 			
 			
 			
 			exec_table.addCell(new Paragraph(0,"Total Tests",TIME_ROMAN));
 			exec_table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			exec_table.addCell(new Paragraph(0,""+executionResult.getTotal(),TIME_ROMAN_NORMAL));
 			
 			exec_table.addCell(new Paragraph(0,"Total Passed",TIME_ROMAN));
 			exec_table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			exec_table.addCell(new Paragraph(0,""+executionResult.getPassed(),TIME_ROMAN_NORMAL));
 			
 			exec_table.addCell(new Paragraph(0,"Total Failed",TIME_ROMAN));
 			exec_table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			exec_table.addCell(new Paragraph(0,""+executionResult.getFailed(),TIME_ROMAN_NORMAL));
 			
 			exec_table.addCell(new Paragraph(0,"Total Skipped",TIME_ROMAN));
 			exec_table.addCell(new Paragraph(0,":",TIME_ROMAN));
 			exec_table.addCell(new Paragraph(0,""+executionResult.getSkipped(),TIME_ROMAN_NORMAL));
 			
 			document.add(exec_table);
 		
			Paragraph paragraph = new Paragraph();
			paragraph.add(new Paragraph("Passed Results", FONT_TIMES_16_BLUE));
			document.add(paragraph);
			creteEmptyLine(exeDetail_paragraph, 1);
			PdfPTable table3 = new PdfPTable(4);
			
			PdfPCell c1 = new PdfPCell(new Phrase("TEST NAME",TIME_ROMAN_SMALL));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			c1.setColspan (1); 
			c1.setPadding (5.0f);
		    c1.setBackgroundColor (new BaseColor (9, 151, 223));
		    table3.setSpacingBefore(30.0f); 
		    table3.setSpacingAfter(30.0f);		
			table3.addCell(c1);
			
			PdfPCell c2 = new PdfPCell(new Phrase("SIGNATURE",TIME_ROMAN_SMALL));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			c2.setColspan(1); 
			c2.setPadding (5.0f);
		    c2.setBackgroundColor (new BaseColor (9, 151, 223));
			table3.addCell(c2);
	 
			PdfPCell c3 = new PdfPCell(new Phrase("START TIME",TIME_ROMAN_SMALL));
			c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			c3.setColspan (1); 
			c3.setPadding (5.0f);
		    c3.setBackgroundColor (new BaseColor (9, 151, 223));
			table3.addCell(c3);
			
			PdfPCell c4 = new PdfPCell(new Phrase("END TIME",TIME_ROMAN_SMALL));
			c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			c4.setColspan (1); 
			c4.setPadding (5.0f);
		    c4.setBackgroundColor (new BaseColor (9, 151, 223));
			table3.addCell(c4);
	 
	 		
			//table.setHeaderRows(1);
			Set<ExecutionResultDetail> executionResultDetailSet = executionResult.getExecutionResultDetail();
			for(ExecutionResultDetail exDetail : executionResultDetailSet){
				if(exDetail.getStatus() == "PASS" || exDetail.getStatus().equalsIgnoreCase("PASS")){
					// for table to repeat PASS 
					PdfPCell cell1 = null; PdfPCell cell2 = null; PdfPCell cell3 = null; PdfPCell cell4 = null;
					
						table3.setWidthPercentage(100);
						table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						table3.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
						 cell1 = new PdfPCell(new Paragraph(exDetail.getTestMethodName()));
						 cell2 = new PdfPCell(new Paragraph(exDetail.getSignature()));
						 cell3 = new PdfPCell(new Paragraph(exDetail.getStartedAt().toString()));
						 cell4 = new PdfPCell(new Paragraph(exDetail.getFinishedAt().toString()));
				         
						 table3.addCell(cell1);
						 table3.addCell(cell2);
						 table3.addCell(cell3);
						 table3.addCell(cell4);
					}
				
			}
			
			table3.setWidths(columnWidths);
			document.add(table3);
			
			Paragraph failed_paragraph = new Paragraph();
			failed_paragraph.add(new Paragraph("Failed Results", FONT_TIMES_16_BLUE));
			document.add(failed_paragraph);
			creteEmptyLine(failed_paragraph, 1);
			PdfPTable failed_table = new PdfPTable(4);
			
			PdfPCell failed_col1 = new PdfPCell(new Phrase("TEST NAME",TIME_ROMAN_SMALL));
			failed_col1.setHorizontalAlignment(Element.ALIGN_CENTER);
			failed_col1.setColspan (1); 
			failed_col1.setPadding (5.0f);
			failed_col1.setBackgroundColor (new BaseColor (9, 151, 223));
			failed_table.setSpacingBefore(30.0f); 
			failed_table.setSpacingAfter(30.0f);		
			failed_table.addCell(failed_col1);
	 
			PdfPCell failed_col2 = new PdfPCell(new Phrase("SIGNATURE",TIME_ROMAN_SMALL));
			failed_col2.setHorizontalAlignment(Element.ALIGN_CENTER);
			failed_col2.setColspan(1); 
			failed_col2.setPadding (5.0f);
			failed_col2.setBackgroundColor (new BaseColor (9, 151, 223));
		    failed_table.addCell(failed_col2);

			PdfPCell failed_col3 = new PdfPCell(new Phrase("START TIME",TIME_ROMAN_SMALL));
			failed_col3.setHorizontalAlignment(Element.ALIGN_CENTER);
			failed_col3.setColspan (1); 
			failed_col3.setPadding (5.0f);
			failed_col3.setBackgroundColor (new BaseColor (9, 151, 223));
			failed_table.addCell(failed_col3);
			
			PdfPCell failed_col4 = new PdfPCell(new Phrase("END TIME",TIME_ROMAN_SMALL));
			failed_col4.setHorizontalAlignment(Element.ALIGN_CENTER);
			failed_col4.setColspan (1); 
			failed_col4.setPadding (5.0f);
			failed_col4.setBackgroundColor (new BaseColor (9, 151, 223));
		    failed_table.addCell(failed_col4);
	 
	 		
		    for(ExecutionResultDetail exDetail : executionResultDetailSet){
				if(exDetail.getStatus() == "FAIL" || exDetail.getStatus().equalsIgnoreCase("FAIL")){
			PdfPCell failed_cell1 = null; PdfPCell failed_cell2 = null; PdfPCell failed_cell3 = null; PdfPCell failed_cell4 = null;
		
			failed_table.setWidthPercentage(100);
			failed_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			failed_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			failed_cell1 = new PdfPCell(new Paragraph(exDetail.getTestMethodName()));
			failed_cell2 = new PdfPCell(new Paragraph(exDetail.getSignature()));
			failed_cell3 = new PdfPCell(new Paragraph(exDetail.getStartedAt().toString()));
			failed_cell4 = new PdfPCell(new Paragraph(exDetail.getFinishedAt().toString()));
			
		         failed_table.addCell(failed_cell1);
		         failed_table.addCell(failed_cell2);
		         failed_table.addCell(failed_cell3);
		         failed_table.addCell(failed_cell4);
			}
		    }
			//table.setKeepTogether(true);
			failed_table.setWidths(columnWidths);
			document.add(failed_table);
		    
			
			Paragraph skipped_paragraph = new Paragraph();
			skipped_paragraph.add(new Paragraph("Skipped Results", FONT_TIMES_16_BLUE));
			document.add(skipped_paragraph);
			creteEmptyLine(skipped_paragraph, 1);
			PdfPTable skipped_table = new PdfPTable(4);
			
			PdfPCell skipped_col1 = new PdfPCell(new Phrase("TEST NAME",TIME_ROMAN_SMALL));
			skipped_col1.setHorizontalAlignment(Element.ALIGN_CENTER);
			skipped_col1.setColspan (1); 
			skipped_col1.setPadding (5.0f);
			skipped_col1.setBackgroundColor (new BaseColor (9, 151, 223));
			skipped_table.setSpacingBefore(30.0f); 
			skipped_table.setSpacingAfter(30.0f);		
			skipped_table.addCell(skipped_col1);
				

			 
			PdfPCell skipped_col2 = new PdfPCell(new Phrase("SIGNATURE",TIME_ROMAN_SMALL));
			skipped_col2.setHorizontalAlignment(Element.ALIGN_CENTER);
			skipped_col2.setColspan(1); 
			skipped_col2.setPadding (5.0f);
			skipped_col2.setBackgroundColor (new BaseColor (9, 151, 223));
			skipped_table.addCell(skipped_col2);
			
	 
			PdfPCell skipped_col3 = new PdfPCell(new Phrase("START TIME",TIME_ROMAN_SMALL));
			skipped_col3.setHorizontalAlignment(Element.ALIGN_CENTER);
			skipped_col3.setColspan (1); 
			skipped_col3.setPadding (5.0f);
			skipped_col3.setBackgroundColor (new BaseColor (9, 151, 223));
			skipped_table.addCell(skipped_col3);
			
			PdfPCell skipped_col4 = new PdfPCell(new Phrase("END TIME",TIME_ROMAN_SMALL));
			skipped_col4.setHorizontalAlignment(Element.ALIGN_CENTER);
			skipped_col4.setColspan (1); 
			skipped_col4.setPadding (5.0f);
			skipped_col4.setBackgroundColor (new BaseColor (9, 151, 223));
			skipped_table.addCell(skipped_col4);
	 
			
			PdfPCell skipped_cell1 = null; PdfPCell skipped_cell2 = null; PdfPCell skipped_cell3 = null; PdfPCell skipped_cell4 = null;
			for(ExecutionResultDetail exDetail : executionResultDetailSet){
				if(exDetail.getStatus() == "SKIP" || exDetail.getStatus().equalsIgnoreCase("SKIP")){
			skipped_table.setWidthPercentage(100);
			skipped_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			skipped_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			skipped_cell1 = new PdfPCell(new Paragraph(exDetail.getTestMethodName()));
			skipped_cell2 = new PdfPCell(new Paragraph(exDetail.getSignature()));
			skipped_cell3 = new PdfPCell(new Paragraph(exDetail.getStartedAt().toString()));
			skipped_cell4 = new PdfPCell(new Paragraph(exDetail.getFinishedAt().toString()));
			
			skipped_table.addCell(skipped_cell1);
			skipped_table.addCell(skipped_cell2);
			skipped_table.addCell(skipped_cell3);
			skipped_table.addCell(skipped_cell4);
			}
			}
			skipped_table.setWidths(columnWidths);
			document.add(skipped_table);
		
			creteEmptyLine(preface, 1);	
 			creteEmptyLine(exeDetail_paragraph, 1);
 			creteEmptyLine(paragraph, 1);
 			creteEmptyLine(failed_paragraph, 1);
 			creteEmptyLine(skipped_paragraph, 1);
			document.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;

	}
	
	private static void createExecutionResultTable(Document document,ExecutionResult executionResult,ExecutionResult executionResult_Side) throws DocumentException {
  		Paragraph paragraph = new Paragraph();
  		creteEmptyLine(paragraph, 2);
  		document.add(paragraph);
  		PdfPTable table = new PdfPTable(4);
  		
  		PdfPCell c1 = new PdfPCell(new Phrase("Method Name",TIME_ROMAN_SMALL));
  		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
  		c1.setColspan (1); 
  		c1.setPadding (5.0f);
  	    c1.setBackgroundColor (new BaseColor (9, 151, 223));
  	    table.setSpacingBefore(30.0f); 
  	    table.setSpacingAfter(30.0f);		
  		table.addCell(c1);
   
  		PdfPCell c2 = new PdfPCell(new Phrase("Signature",TIME_ROMAN_SMALL));
  		c2.setHorizontalAlignment(Element.ALIGN_CENTER);
  		c2.setColspan(1); 
  		c2.setPadding (5.0f);
  	    c2.setBackgroundColor (new BaseColor (9, 151, 223));
  		table.addCell(c2);
  		//table.setHeaderRows(1);
  		
  		PdfPCell c3 = new PdfPCell(new Phrase("Execution-1 Result",TIME_ROMAN_SMALL));
  		c3.setHorizontalAlignment(Element.ALIGN_CENTER);
  		c3.setColspan (1); 
  		c3.setPadding (5.0f);
  	    c3.setBackgroundColor (new BaseColor (9, 151, 223));
  		table.addCell(c3);
  		
  		PdfPCell c4 = new PdfPCell(new Phrase("Execution-2 Result",TIME_ROMAN_SMALL));
  		c4.setHorizontalAlignment(Element.ALIGN_CENTER);
  		c4.setColspan (1); 
  		c4.setPadding (5.0f);
  	    c4.setBackgroundColor (new BaseColor (9, 151, 223));
  		table.addCell(c4);
  		Set<ExecutionResultDetail> executionResult_Side_set = executionResult_Side.getExecutionResultDetail();
  		PdfPCell cell1 = null; PdfPCell cell2 = null; PdfPCell cell3 = null; PdfPCell cell4 = null;
  		boolean isMatched = true;
  		for (ExecutionResultDetail exDetail : executionResult.getExecutionResultDetail()) {
  			
  			table.setWidthPercentage(100);
  			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
  			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
  			
  			for (ExecutionResultDetail exDetail_Side : executionResult_Side_set) {
  		  	        if(exDetail.getSignature().contentEquals(exDetail_Side.getSignature())){
  		  	         cell1 = new PdfPCell(new Paragraph(exDetail.getTestMethodName()));
  		   			 cell2 = new PdfPCell(new Paragraph(exDetail.getSignature()));
  		   	         cell3 = new PdfPCell(new Paragraph(exDetail.getStatus()));
  		   	         cell4 = new PdfPCell(new Paragraph(exDetail_Side.getStatus()));
  		   	    logger.info("### In If First For Loop executionResultDetail_Side Status is :"+exDetail_Side.getStatus());
  		   			 table.addCell(cell1); table.addCell(cell2);
  		   			 table.addCell(cell3); table.addCell(cell4);
  		   		isMatched = false;
  		  	         }
  		  	  //  logger.info("is Deleted in Set Obj :"+executionResult_Side_set.remove(exDetail_Side));
  		  	    exDetail_Side = null;
  			}
  		}
  		
  		if(isMatched){
for (ExecutionResultDetail exDetail : executionResult.getExecutionResultDetail()) {
  			
  			table.setWidthPercentage(100);
  			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
  			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
  			
  			for (ExecutionResultDetail exDetail_Side : executionResult_Side_set) {
  				
  		  	    if(!(exDetail.getSignature().contentEquals(exDetail_Side.getSignature() ) ) ){
  		  	        PdfPCell cell5 = null; PdfPCell cell6 = null; PdfPCell cell7 = null; PdfPCell cell8 = null;
  		  	        cell1 = new PdfPCell(new Paragraph(exDetail.getTestMethodName()));
 		   			 cell2 = new PdfPCell(new Paragraph(exDetail.getSignature()));
 		   	         cell3 = new PdfPCell(new Paragraph(exDetail.getStatus()));
 		   	         cell4 = new PdfPCell(new Paragraph(""));
 		   	     logger.info("### In Else Second For Loop First executionResultDetail_Side Status is :"+exDetail_Side.getStatus());
		   			 table.addCell(cell1); table.addCell(cell2);
		   			 table.addCell(cell3); table.addCell(cell4);
		   			cell5 = new PdfPCell(new Paragraph(exDetail_Side.getTestMethodName()));
		   			 cell6 = new PdfPCell(new Paragraph(exDetail_Side.getSignature()));
		   	         cell7 = new PdfPCell(new Paragraph(""));
		   	         cell8 = new PdfPCell(new Paragraph(exDetail_Side.getStatus()));
 		   			 table.addCell(cell5); table.addCell(cell6);
 		   			 table.addCell(cell7); table.addCell(cell8);
 		   			executionResult_Side_set.remove(exDetail_Side);
 		   			//logger.info("is Deleted in Set Obj :"+executionResult_Side_set.remove(exDetail_Side));
  		  	         }
  		  	exDetail_Side = null;
  		  	    break;
  		  	       
  			}
  		}
  	}

  		//table.setKeepTogether(true);
          table.setWidths(columnWidths);
  		document.add(table);
  		
  	}
	
		public static Document createPDF_ExecutionResult(String file,ExecutionResult executionResult) {

			Document document = null;

			try {
				document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(file));
				document.open();
				
				ExecutionResultDetail executionResultDetail = new ExecutionResultDetail();
				
				
				
				Paragraph preface = new Paragraph();
				/*String logo_path = "/resources/images/bpa_logo.png";
				Image image = Image.getInstance(logo_path);
	            document.add(image);*/
				creteEmptyLine(preface, 1);
				creteEmptyLine(preface, 1);
				preface.add(new Paragraph("Execution Result", TIME_ROMAN));
				
				
				creteEmptyLine(preface, 1);
				preface.add(new Paragraph("Execution Result Details", TIME_ROMAN_SMALL));
				document.add(preface);
				
				 List unorderedList = new List(List.UNORDERED);
				 unorderedList.add(new ListItem("Project Name :"+executionResult.getTestSuiteExecution().getTestSuite().getProject().getProjectName()));
				 unorderedList.add(new ListItem("Test Suite Name :"+executionResult.getTestSuiteExecution().getTestSuite().getSuiteName()));
				 unorderedList.add(new ListItem("Execution Name :"+executionResult.getTestExecutionName()));
				 unorderedList.add(new ListItem("Total Results : "+executionResult.getTotal()));
				 unorderedList.add(new ListItem("Passed Results :"+executionResult.getPassed()));
				 unorderedList.add(new ListItem("Faile Results :"+executionResult.getFailed()));
				 unorderedList.add(new ListItem("Skipped Results :"+executionResult.getSkipped()));
		        document.add(unorderedList);
				
				creteEmptyLine(preface, 1);
				//createTable(document,executionResult);
				
				Paragraph paragraph = new Paragraph();
				creteEmptyLine(paragraph, 2);
				document.add(paragraph);
				PdfPTable table = new PdfPTable(3);
				
				PdfPCell c1 = new PdfPCell(new Phrase("TEST NAME",TIME_ROMAN_SMALL));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				c1.setColspan (1); 
				c1.setPadding (5.0f);
			    c1.setBackgroundColor (new BaseColor (140, 221, 8));
			    table.setSpacingBefore(30.0f); 
			    table.setSpacingAfter(30.0f);		
				table.addCell(c1);
		 
				PdfPCell c2 = new PdfPCell(new Phrase("START TIME",TIME_ROMAN_SMALL));
				c2.setHorizontalAlignment(Element.ALIGN_CENTER);
				c2.setColspan (1); 
				c2.setPadding (5.0f);
			    c2.setBackgroundColor (new BaseColor (140, 221, 8));
				table.addCell(c2);
		 
				PdfPCell c3 = new PdfPCell(new Phrase("SIGNATURE",TIME_ROMAN_SMALL));
				c3.setHorizontalAlignment(Element.ALIGN_CENTER);
				c3.setColspan(1); 
				c3.setPadding (5.0f);
			    c3.setBackgroundColor (new BaseColor (140, 221, 8));
				table.addCell(c3);
				//table.setHeaderRows(1);
				if(executionResultDetail.getStatus() == "PASS"){
				
				PdfPCell cell1 = null; PdfPCell cell2 = null; PdfPCell cell3 = null; PdfPCell cell4 = null;
				for (ExecutionResultDetail exDetail : executionResult.getExecutionResultDetail()) {
					table.setWidthPercentage(100);
					table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
					table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
					 cell1 = new PdfPCell(new Paragraph(exDetail.getTestMethodName()));
					 cell2 = new PdfPCell(new Paragraph(exDetail.getStartedAt().toString()));
					 cell3 = new PdfPCell(new Paragraph(exDetail.getFinishedAt().toString()));
			         cell4 = new PdfPCell(new Paragraph(exDetail.getSignature()));
					 table.addCell(cell1);
					 table.addCell(cell2);
					 table.addCell(cell3);
					 table.addCell(cell4);
				}
				//table.setKeepTogether(true);
		        table.setWidths(columnWidths);
				document.add(table);
				}
				
				if(executionResultDetail.getStatus() == "FAIL"){
					
					preface.add(new Paragraph("Failed", FONT_TIMES_16_BLUE));
					creteEmptyLine(preface, 1);
					
					PdfPCell cell1 = null; PdfPCell cell2 = null; PdfPCell cell3 = null; PdfPCell cell4 = null;
					for (ExecutionResultDetail exDetail : executionResult.getExecutionResultDetail()) {
						table.setWidthPercentage(100);
						table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell1 = new PdfPCell(new Paragraph(exDetail.getTestMethodName()));
						cell2 = new PdfPCell(new Paragraph(exDetail.getStartedAt().toString()));
						cell3 = new PdfPCell(new Paragraph(exDetail.getFinishedAt().toString()));
				        cell4 = new PdfPCell(new Paragraph(exDetail.getSignature()));
						 table.addCell(cell1);
						 table.addCell(cell2);
						 table.addCell(cell3);
						 table.addCell(cell4);
					}
					//table.setKeepTogether(true);
			        table.setWidths(columnWidths);
					document.add(table);
					}
				if(executionResultDetail.getStatus() == "SKIP"){
					
					preface.add(new Paragraph("Skipped", FONT_TIMES_16_BLUE));
					creteEmptyLine(preface, 1);
					
					PdfPCell cell1 = null; PdfPCell cell2 = null; PdfPCell cell3 = null; PdfPCell cell4 = null;
					for (ExecutionResultDetail exDetail : executionResult.getExecutionResultDetail()) {
						table.setWidthPercentage(100);
						table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
						table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell1 = new PdfPCell(new Paragraph(exDetail.getTestMethodName()));
						cell2 = new PdfPCell(new Paragraph(exDetail.getStartedAt().toString()));
						cell3 = new PdfPCell(new Paragraph(exDetail.getFinishedAt().toString()));
				        cell4 = new PdfPCell(new Paragraph(exDetail.getSignature()));
						 table.addCell(cell1);
						 table.addCell(cell2);
						 table.addCell(cell3);
						 table.addCell(cell4);
					}
					//table.setKeepTogether(true);
			        table.setWidths(columnWidths);
					document.add(table);
					}
				
				document.close();

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return document;

		}
}
