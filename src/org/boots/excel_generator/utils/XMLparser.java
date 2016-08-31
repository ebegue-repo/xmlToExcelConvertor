package org.boots.excel_generator.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLparser {
	
	private File xmlFile = null;
	private Document doc = null;
	private int rowNumber = 0;
	
	public XMLparser(String fileName, String absolutePath){
		String completePath = absolutePath + "/" + fileName + ".xml";
		this.xmlFile = new File(completePath);
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			this.doc = dBuilder.parse(this.xmlFile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("XML File not found");
			System.exit(0);
		}
	}
	
	public String[] getRow(){
		String number = "";
        String scope = "";
        String faultHandler = "";
        String severity = "";
        String logLevel = "";
        String httpCode = "";
        String faultID = "";
        String data = "";
        String logCode = "";
        String logMessage = "";
        String stades = "";
        NamedNodeMap attributes = null;
		
		NodeList nodeList = doc.getElementsByTagName("statuslist");
        Node node = nodeList.item(0);
        Element element = (Element) node;
        Node elementItem = element.getElementsByTagName("status").item(this.rowNumber);
        	
        if(elementItem != null){
        	// Element attributes
        	attributes = elementItem.getAttributes();
        	
        	// number
        	number = attributes.getNamedItem("number").getNodeValue();
        	
        	while(number.equals("0")){
        		// Increments rowNumber till the first e* error number value
        		this.rowNumber++;
        		attributes = element.getElementsByTagName("status").item(this.rowNumber).getAttributes();
	        	number = attributes.getNamedItem("number").getNodeValue();
        	}
        	
        	// Scope & faultHandler
        	stades = attributes.getNamedItem("stades").getNodeValue();
        	String[] completeStades = stades.split(". ", 2);
        	scope = completeStades[0];
        	faultHandler = completeStades[1];
        	
        	// Severity
        	logLevel = attributes.getNamedItem("loglevel").getNodeValue();
        	
        	if(logLevel.equals("e") || logLevel.equals("x")){
        		severity = "Error";
        	}else{
        		if(logLevel.equals("i")){
            		severity = "Info";
            	}else{
            		if(logLevel.equals("w")){
                		severity = "Warning";
                	}
            	}
        	}
        	
        	// HTTP code
        	httpCode = attributes.getNamedItem("httpcode").getNodeValue();
        	
        	if(httpCode.equals("x") || httpCode.equals("500")){
        		httpCode = "500 - Internal server error";
        	}else{
        		if(httpCode.equals("400")){
        			httpCode = "400 - Bad Request";
        		}
        	}
        	
        	// Returned data
        	faultID = attributes.getNamedItem("faultid").getNodeValue();
        	
        	if(faultID.equals("1") || faultID.equals("2") ||faultID.equals("4")){
        		data = "SOAP fault";
        	}else{
        		data = "-";
        	}
        	
        	// Transaction log code
        	logCode = attributes.getNamedItem("logdes").getNodeValue();
        	
        	//Transaction log message
        	logMessage = "-";
        	
        	this.rowNumber++;
        	return new String[]{scope, faultHandler, severity, httpCode, data, logCode, logMessage};
        }else{
        	return null;
        }
	}
}
