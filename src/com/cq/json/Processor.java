package com.cq.json;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class Processor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String repoPath = "";
		String excelPath = "testcase.xlsm";
		
		ExcelAdapter ea = new ExcelAdapter();
		File excelFile = new File(excelPath);
		try {
			Sheet sheet = ea.getSheet(excelFile, "Instructions");
			Row headerRow = sheet.getRow(0);
			Cell cell = headerRow.getCell(1);
			String content = getCellValue(cell);
			String orgContent = cell.getStringCellValue();
			System.out.println(content);
			System.out.println(orgContent);
			System.out.println(cell.getCellType());
			
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getCellValue(Cell cell){
		String cellValue = "";
		if(cell != null){
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_BOOLEAN:
					if( cell.getBooleanCellValue()){
						cellValue = "TRUE";
					} else {
						cellValue = "FALSE";
					}							
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if(DateUtil.isCellDateFormatted(cell)) {
						double dv = cell.getNumericCellValue();
						if(DateUtil.isValidExcelDate(dv)) {
							Date cellDate = DateUtil.getJavaDate(dv);
							SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
							String sCellDate = dateFormatter.format(cellDate);
							cell.setCellType(Cell.CELL_TYPE_STRING);
							cell.setCellValue(sCellDate);	
							cellValue = getCellValue(cell);
						}
					}else{
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cellValue = cell.getStringCellValue();
						//cellValue = Double.toString(cell.getNumericCellValue());
					}
					break;
				case  Cell.CELL_TYPE_STRING:
					cellValue = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_BLANK:
					break;
				case Cell.CELL_TYPE_ERROR:
					cellValue =  Byte.toString(cell.getErrorCellValue());
					break;
			}
			return cellValue;
		}else{
			return null;
		}

	}
}
