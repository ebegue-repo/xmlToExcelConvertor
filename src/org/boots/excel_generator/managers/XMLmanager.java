package org.boots.excel_generator.managers;

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

import orgs.boots.excel_generator.beans.Fault;

public class XMLmanager {
	
	private File xmlFile = null;
	private Document doc = null;
	private int rowNumber = 0;
	private Fault[] faultList = null;
	
	public XMLmanager(String fileName, String absolutePath){
		String completePath = absolutePath + "/" + fileName + ".xml";
		this.xmlFile = new File(completePath);
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			this.doc = dBuilder.parse(this.xmlFile);
			
			// Getting fault list
			this.getFaultList();
			
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
        Node faultID_node = null;
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
        	faultID_node = attributes.getNamedItem("faultid");
        	
        	// There are old statusList files using the "soapfaultid" tag instead of "faultid"
        	if(faultID_node == null){
        		faultID_node = attributes.getNamedItem("soapfaultid");
        	}
        	
        	faultID = faultID_node.getNodeValue();
        	data = this.getFaultValue(Integer.parseInt(faultID));
        	
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
	
	private void getFaultList(){
		NamedNodeMap attributes = null;
		String id = "";
		String format = "";
		
		NodeList faultNodeList = doc.getElementsByTagName("faultlist");
        Node node = faultNodeList.item(0);
        
        // Elements
        Element element = (Element) node;
        NodeList faultElement = element.getElementsByTagName("fault");
        
        int numElements = faultElement.getLength(); 
        this.faultList = new Fault[numElements];
        Fault fault = null;
        
        for(int i = 0; i < numElements; i++){
        	attributes = faultElement.item(i).getAttributes();
        	id = attributes.getNamedItem("id").getNodeValue();
        	format = attributes.getNamedItem("format").getNodeValue();
        	
        	// Saving the fault item
        	fault = new Fault();
        	fault.setFormat(format);
        	fault.setId(Integer.parseInt(id));
        	this.faultList[i] = fault;
        }
	}
	
	private String getFaultValue(int id){
		
		int i = 0;
		Fault fault = null;
		String format = "";
		boolean success = false;
		
		while((i < this.faultList.length) && !success){
			fault = this.faultList[i];
			if(fault.getId() == id){
				success = true;
			}
			i++;
		}
		
		if(success){
			format = fault.getFormat();
			
			if(format.equals("-")){
				return "-";
			}else{
				if(format.equals("s")){
					return "SOAP fault";
				}else{
					if(format.equals("j")){
						return "JSON fault";
					}else{
						if(format.equals("x")){
							return "XML fault";
						}else{
							if(format.equals("h")){
								return "Fault in HTTP headers";
							}else{
								System.out.println("Undefined fault format");
								return "?";
							}
						}
					}
				}
			}
		}else{
			System.out.println("Undefined fault ID");
			return "?";
		}
	}
}
