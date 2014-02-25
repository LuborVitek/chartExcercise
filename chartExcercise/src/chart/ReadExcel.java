package chart;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReadExcel {

	public static LinkedHashMap<String, Double> readFile(String inputFile) throws IOException {
		LinkedHashMap<String, Double> data = new LinkedHashMap<String, Double>();
		
		File inputWorkbook = new File(inputFile);
		Workbook workBook;
		try {
			workBook = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = workBook.getSheet(0);

			for (int row = 0; row < sheet.getRows(); row++) {
				Cell firstCell = sheet.getCell(0, row);
				Cell secondCell = sheet.getCell(1, row);
					
				if (firstCell.getType() == CellType.LABEL && secondCell.getType() == CellType.NUMBER){
					String secondCellContent = secondCell.getContents().replace(",", ".");
					data.put(firstCell.getContents(), Double.parseDouble(secondCellContent));
				}
			}
		} catch (BiffException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static void main(String[] args) throws IOException {
		LinkedHashMap<String, Double> data = ReadExcel.readFile("data/piechart-data.xls");
		System.out.println(data);
	}

}