
******************************
	ERRORS_EXCEL_GENERATOR
******************************

- Description: JAVA program that converts automatically an xml file (statusList.xml) into an excel file.

- Way to run:

	java -jar errorsExcelGenerator.jar xmlFileNameWithoutExtension XMLFilePath excelFileNameWithoutExtension sheetName
	
	The new excel file will be created on the same path as the xml file.

- Parameters:

	* XMLfileName: XML file to read without extension. If the name has spaces please write the name in quotation marks. 

	* XML_file_path: Something like C:/Users/Eduardo.

	* ExcelFileName: New excel file without extension. If the name has spaces please write the name in quotation marks.
	
	* SheetName: Excel sheet name.
	
- Notes:
	The returned data field depends on the fault format value:
	
	<!-- 
        Fault formats:
        + format="-" : Empty body (HTTP headers only)
        + format="s" : SOAP envelope fault
        + format="j" : JSON fault
        + format="x" : XML error file
        + format="h" : Empty body. All error info in HTTP headers.
   -->
   
   <faultlist>
      <fault id="0" format="-" detail="s" faultactor=""   faultcode="" />
      <fault id="1" format="s" detail="s" faultactor="IG" faultcode="Client" />
      <fault id="2" format="s" detail="s" faultactor="IG" faultcode="Server" />
      <fault id="3" format="-" detail="s" faultactor="IG" faultcode="Client. Http method not allowed" />
      <fault id="4" format="s" detail="e" faultactor="IG" faultcode="Client. Request body empty" />
   </faultlist>
   
   
