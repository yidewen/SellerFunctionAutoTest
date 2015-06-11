package com.ymatou.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelOperator {
	
	public final static String filePath = "E:\\workspace\\SellerAutoTestFunction\\res\\AotuTestCaseList.xlsx";
	private int sheetNumber;
	
	public int getSheetNumber() {
		return sheetNumber;
	}


	public void setSheetNumber(int sheetNumber) {
		this.sheetNumber = sheetNumber;
	}

	public LinkedHashMap<String, List<List<String>>> generateAllTestCase(){
		LinkedHashMap<String, List<List<String>>> hMap = new LinkedHashMap<String, List<List<String>>>();
		int number = 0;
		FileInputStream is = null;
		XSSFWorkbook wb = null;
		String testCaseTitle="";
		try {
			//将文件载入文件流
			is = new FileInputStream(filePath);
			//创建workbook对象实例
			wb = new XSSFWorkbook(is);
			//获得sheet的数量
			number = wb.getNumberOfSheets();
			System.out.println("当前工作表的sheet的数量是： " + number);
			//循环遍历sheet
			for (int i = 0; i <= number-2; i++) {
				List<List<String>> steps=new LinkedList<>();
				XSSFSheet sheet = wb.getSheetAt(i);
				testCaseTitle = sheet.getSheetName();
				System.out.println("当前的sheet名是： " + testCaseTitle);
				int rowsCount = sheet.getLastRowNum();
				System.out.println(testCaseTitle + "有" + rowsCount + "行（步骤）");
				//循环遍历sheet中所有row
				for (int j = 0; j <= rowsCount; j++) {	
					XSSFRow row = sheet.getRow(j);
					//System.out.println("当前是第 "+ j + "行。它的内容是： " + row.toString());
					int countsOfCellInRow = row.getLastCellNum();
					//System.out.println("该行有 " + countsOfCellInRow + "个单元格：");
					List<String> step = new LinkedList<>();
					//循环遍历row中的所有cell
					for (int k = 0; k < countsOfCellInRow; k++) {
						XSSFCell cell = row.getCell(k);
						String contentsOfCell = getCellStringValue(cell);
						//System.out.println("这是该行的第 " + k +1  + "单元格，且内容为： " + contentsOfCell);
						step.add(contentsOfCell);
					}
					steps.add(step);
					step = new LinkedList<>();
				}
				System.out.println("当前放入的是" + testCaseTitle + " 以及它的步骤" + steps);
				hMap.put(testCaseTitle, steps);
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
		return hMap;
	}
	
	public List<String> returnAllSheetName(){
		List<String> names = new LinkedList<>();
		try {
			FileInputStream is = new FileInputStream(filePath);
			XSSFWorkbook wb = new XSSFWorkbook(is);
			int countsOfSheets = wb.getNumberOfSheets();
			for (int i = 0; i <= countsOfSheets - 2; i++) {
				String sheetName = wb.getSheetAt(i).getSheetName();
				names.add(sheetName);
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
		return names;
	}
	
	public String getCellStringValue(XSSFCell cell){
		String cellContent="";
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			cellContent = cell.getStringCellValue();
			if (cellContent.trim().equals("") || cellContent.trim().length()<=0) {
				cellContent = "";
			}			
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			cellContent = String.valueOf(cell.getNumericCellValue());
			break;
		case XSSFCell.CELL_TYPE_FORMULA:
			cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
			cellContent = String.valueOf(cell.getNumericCellValue());
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			cellContent = "";
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			break;
		case XSSFCell.CELL_TYPE_ERROR:
			break;
		default:
			break;
		}
		return cellContent;
	}
	
	public List<String> readOneRow(int rowNumber){
		FileInputStream is = null;
		XSSFWorkbook wb = null;
		List<String> rowList = new LinkedList<String>();
		try {
			is = new FileInputStream(filePath);
			wb = new XSSFWorkbook(is);
			XSSFSheet sheet = wb.getSheet("测试用例");
			XSSFRow row = sheet.getRow(rowNumber);
			int sizeOfRow = row.getLastCellNum();
			for (int i = 0; i <= sizeOfRow - 1; i++) {
				XSSFCell cell = row.getCell(i);
				//System.out.println(cell.getStringCellValue());
				rowList.add(cell.getStringCellValue());
			}				
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(rowList.toString());
		return rowList;
	}

}
