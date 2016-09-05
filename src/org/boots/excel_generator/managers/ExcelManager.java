package org.boots.excel_generator.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelManager {
	
	private final static int COMPONENT_SCOPE_COLUMN = 0;
    private final static int FAULT_HANDLER_COLUMN = 1;
    private final static int SEVERITY_COLUMN = 2;
    private final static int HTTP_RETURN_CODE_COLUMN = 3;
    private final static int RETURNED_DATA_COLUMN = 4;
    private final static int TRANSACTION_LOG_CODE_COLUMN = 5;
    private final static int TRANSACTION_LOG_MESSAGE_COLUMN = 6;
    
    private final static short HEADER_HEIGHT = (short) 600;
    private final static int INITIAL_ROW_NUMBER = 3;
	
	private String completePath = "";
	private String fileName = "";
	private String sheetName = "";
	private File file = null;
	private FileOutputStream fileOut = null;
	private XSSFWorkbook workbook = null;
	private Sheet sheet = null;
	private int rowNumber = INITIAL_ROW_NUMBER;
	private CellStyle defaultCellStyle = null;
	
	public ExcelManager(String fileName, String absolutePath, String sheetName){
		this.fileName = fileName;
		this.completePath = absolutePath + "/" + this.fileName + ".xlsx";
		this.sheetName = sheetName;
	}
	
	public void generateEmptyExcel(){
		try {
			this.file = new File(this.completePath);
			
			if(!this.file.exists()){
				this.file.createNewFile();
			}
			
			this.fileOut = new FileOutputStream(this.completePath);

		} catch (FileNotFoundException e) {
			System.out.println("XML File not found");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("XML File not found");
			System.exit(0);
		}
	}
	
	
	public void initExcel(){
		this.workbook = new XSSFWorkbook();

		// Header style
        CellStyle headerStyle = this.workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(boldFont);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());
        headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        headerStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
        headerStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
        headerStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        
        // Default style
        this.defaultCellStyle = this.workbook.createCellStyle();
        Font defaultFont = workbook.createFont();
        defaultFont.setFontHeightInPoints((short) 12);
        this.defaultCellStyle.setFont(defaultFont);
        this.defaultCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        this.defaultCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        this.defaultCellStyle.setBorderTop(CellStyle.BORDER_THIN);
        this.defaultCellStyle.setBorderRight(CellStyle.BORDER_THIN);
        this.defaultCellStyle.setBorderLeft(CellStyle.BORDER_THIN);

        // New sheet
        this.sheet = this.workbook.createSheet();
        
        // Sheet name
        workbook.setSheetName(workbook.getSheetIndex(sheet), this.sheetName);
        
        // Header columns
        Row row = sheet.createRow(this.rowNumber);
        Cell cell = row.createCell(COMPONENT_SCOPE_COLUMN);
        cell.setCellValue("Component/Scope");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(FAULT_HANDLER_COLUMN);
        cell.setCellValue("Fault handler");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(SEVERITY_COLUMN);
        cell.setCellValue("Severity");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(HTTP_RETURN_CODE_COLUMN);
        cell.setCellValue("HTTP return code");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(RETURNED_DATA_COLUMN);
        cell.setCellValue("Returned data");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(TRANSACTION_LOG_CODE_COLUMN);
        cell.setCellValue("Transaction log code");
        cell.setCellStyle(headerStyle);

        cell = row.createCell(TRANSACTION_LOG_MESSAGE_COLUMN);
        cell.setCellValue("Transaction log message");
        cell.setCellStyle(headerStyle);
        
        // Header height
        this.sheet.getRow(this.rowNumber).setHeight(HEADER_HEIGHT);
        
        // Auto width column
        for(int i=0; i < this.sheet.getRow(this.rowNumber).getPhysicalNumberOfCells(); i++){
        	this.sheet.autoSizeColumn(i);
        }
        
        this.rowNumber++;
	}
	
	public void fillRow(String[] data){
		Row row = this.sheet.createRow(this.rowNumber);
		
		for(int i = 0; i < data.length; i++){
			Cell cell = row.createCell(i);
			cell.setCellValue(data[i]);
	        cell.setCellStyle(this.defaultCellStyle);
	        this.sheet.autoSizeColumn(i);
		}
		
		this.rowNumber++;
	}
	
	public void finishExcel(){
		try {
			this.workbook.write(this.fileOut);
	        this.workbook.close();
	        this.fileOut.close();
	        System.out.println("Excel file successfully created on " + this.completePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}