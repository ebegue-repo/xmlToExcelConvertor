package org.boots.excel_generator.main;

import org.boots.excel_generator.managers.ExcelManager;
import org.boots.excel_generator.managers.XMLmanager;

public class ExcelGenerator {

	public static void main(String[] args) {
		String path = "";
		String xmlFileName = "";
		String excelFileName = "";
		String sheetName = "";
		
		if(args.length < 4){
			System.out.println("Invalid parameters. Call example:\n" + 
					           "java -jar errorsExcelGenerator.jar XMLFileName XMLFilePath EXCELfileName EXCELsheetName \n");
		}else{
			xmlFileName = args[0];
			path = args[1];
			excelFileName = args[2];
			sheetName = args[3];
			
			ExcelManager excelManager = new ExcelManager(excelFileName, path, sheetName);
			excelManager.generateEmptyExcel();
			excelManager.initExcel();
			
			XMLmanager parser = new XMLmanager(xmlFileName, path);
			String[] row = parser.getRow();
			
			while((row != null) && (row.length > 0)){
				excelManager.fillRow(row);
				row = parser.getRow();
			}
			
			excelManager.finishExcel();
		}
	}
}
