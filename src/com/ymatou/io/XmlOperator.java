package com.ymatou.io;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XmlOperator {
	
	//xmlÎÄ¼þÂ·¾¶
	private String filePath;
	private Document doc; 
	
	public XmlOperator(String filePath) {
		this.filePath = filePath;
		buildXMLDocument();
	}
	
	public void buildXMLDocument(){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(filePath));
		} catch (Exception e) {
			// TODO: handle exception
		}		
	}
	
	public String getElementValueByName(String name){
		String value = "";
		NodeList nl = doc.getElementsByTagName("param");
		for (int i = 0; i < nl.getLength(); i++) {
			value = doc.getElementsByTagName(name).item(i).getFirstChild().getNodeValue();
			//System.out.println(value);
		}
		return value;
	}
	
	public void setValeByName(String name,String number){
		if (name.equals("number")) {
			int value = (Integer.parseInt(getElementValueByName("number")) + 1);
			number = Integer.toString(value);
		}
		
		NodeList n = doc.getElementsByTagName("param");
		for (int i = 0; i < n.getLength(); i++) {
			doc.getElementsByTagName(name).item(i).getFirstChild().setNodeValue(number);
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(doc);
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			StreamResult result = new StreamResult(new FileOutputStream(filePath));
			transformer.transform(domSource, result);
		} catch (FileNotFoundException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
