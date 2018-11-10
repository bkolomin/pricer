package ru.bkolomin.priceLoader.Service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bkolomin.priceLoader.Models.PriceItem;
import ru.bkolomin.priceLoader.repository.PriceRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PriceService {

    private PriceRepository priceRepository;

    @Autowired
    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    private String getStringValue(Row row, Map<String, Integer[]> settings, String name){

        String result = "";

        Integer[] cellSettings = settings.get(name);

        if(cellSettings == null){
            return "";
        }


        for(int i = 0; i < cellSettings.length; i++){

            Cell cell = row.getCell(cellSettings[i] - 1);

            if(cell == null || cell.getCellType() == CellType.BLANK){

            }else {

                result = result + cell.toString() + ";";

            }

        }

        return result;

    }

    private Double getPrice(Row row, Map<String, Integer[]> settings, String name){

        Double result = 0d;

        Integer[] cellSettings = settings.get(name);

        if(cellSettings == null){
            return 0d;
        }


        for(int i = 0; i < cellSettings.length; i++){

            Cell cell = row.getCell(cellSettings[i] - 1);

            if(cell == null || cell.getCellType() != CellType.NUMERIC){

            }else {

                result = cell.getNumericCellValue();

            }

        }

        return result;

    }

    public List<PriceItem> getPriceItems(String fileName){

        ArrayList<PriceItem> list = new ArrayList<>();

        Workbook workbook;

        try {
            workbook = WorkbookFactory.create(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return list;
        }


        Map<String, Integer[]> settings = new HashMap<>();

        settings.put("comment", new Integer[]{1, 2, 3, 4});
        settings.put("code",	new Integer[]{5, 6, 7});
        settings.put("name", 	new Integer[]{8});
        settings.put("price",	new Integer[]{11});
        settings.put("stock", 	new Integer[]{12});


        for(Sheet sheet: workbook) {
            System.out.println("=> " + sheet.getSheetName());


            for (Row row: sheet) {

                Integer rowNumber = row.getRowNum();
                String comment = getStringValue(row, settings, "comment");
                String code = getStringValue(row, settings, "code");
                String name = getStringValue(row, settings, "name");
                Double price = getPrice(row, settings, "price");
                String stock = getStringValue(row, settings, "stock");

                if(!name.isEmpty() && price != 0 ) {

                    PriceItem priceItem = new PriceItem("Верисел", rowNumber, comment, code, name, price, stock);

                    list.add(priceItem);

                }

            }

        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;

    }

    public void loadAllFiles(){

        List<PriceItem> list = getPriceItems("D:\\_Share\\_pricer\\Верисел.xls");


        priceRepository.deleteAll("Верисел");

        for(PriceItem priceItem: list){

            priceRepository.save(priceItem);

        }

    }

}
