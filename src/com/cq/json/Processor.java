package com.cq.json;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.Keymap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.json.JSONArray;
import org.json.JSONObject;

public class Processor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String repoPath = "repo.xml";
		String excelPath = "testcase.xlsm";
		
		ExcelAdapter ea = new ExcelAdapter();
		File excelFile = new File(excelPath);
		File repoFile = new File(repoPath);

		try {
			Sheet sheet = ea.getSheet(excelFile, "Instructions");
			Row headerRow = sheet.getRow(0);
			int nFirstRow = sheet.getFirstRowNum();
			int nLastRow = sheet.getLastRowNum();
			
			int nFirstCol = headerRow.getFirstCellNum();
			int nLastCol = headerRow.getLastCellNum();
			
			System.out.println(String.format("%d:%d", nFirstRow, nLastRow));
			Cell specialCell = sheet.getRow(2).getCell(5);
			String specialContent = getCellValue(specialCell);
			System.out.println(specialContent.length());
			System.out.println(String.format("The Cell content:%s, Cell Type:%d", getCellValue(specialCell), specialCell.getCellType()));
			int n = 0;
			
			for(Row r : sheet){
				++n;
				if (1 == n)
					continue;
				
				System.out.println(String.format("第%d行输出:", n));
				System.out.println(String.format("row count:%d - %d", r.getFirstCellNum(), r.getLastCellNum()));
				for(int i = nFirstCol; i < nLastCol; ++i){
					Cell c = r.getCell(i);
					if (null == c)
						System.out.print("E - ");
					else
						System.out.print(c.getCellType() + " - ");
				}

				System.out.println("");
				for(int i = nFirstCol; i < nLastCol; ++i){
					Cell c = r.getCell(i);
					if (null == c){
						System.out.print("!!EMPTY!! - ");
					}
					else{
						String cellContent = getCellValue(c);
						System.out.print((cellContent.isEmpty() ? "!!EMPTY!!" : cellContent) + " - ");
					}
				}
				System.out.println("");
			}
			JSONArray sheetJSON = FileJSONConvertor.excel2JSON(excelFile);
			System.out.println("");
			System.out.println(sheetJSON);
			
			JSONObject repoJSON = FileJSONConvertor.repo2JSON(repoFile);
			System.out.println(repoJSON);
			
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
