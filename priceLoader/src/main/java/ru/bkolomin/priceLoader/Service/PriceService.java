package ru.bkolomin.priceLoader.service;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bkolomin.priceLoader.models.PriceItem;
import ru.bkolomin.priceLoader.PriceLoaderApplication;
import ru.bkolomin.priceLoader.repository.PriceRepository;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PriceService {//

    static Logger logger = LoggerFactory.getLogger(PriceLoaderApplication.class);

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

                result = result + (result.isEmpty()?"":"; ") + cell.toString();

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

    private Map<String, Integer[]> getSupplierSettings(String supplier) {

        Map<String, Integer[]> settings = new HashMap<>();

        switch(supplier){

            case "Верисел":
                settings.put("comment", new Integer[]{1, 2, 3, 4});
                settings.put("code", new Integer[]{5, 6, 7});
                settings.put("name", new Integer[]{8});
                settings.put("price", new Integer[]{11});
                settings.put("stock", new Integer[]{12});
                break;
            case "Статен":
                settings.put("comment", new Integer[]{4, 5, 8});
                settings.put("code", new Integer[]{1, 7});
                settings.put("name", new Integer[]{2});
                settings.put("price", new Integer[]{14});
                settings.put("stock", new Integer[]{18});
                break;
            default:
                logger.error("ERROR!!!! setting not found for supplier ?", supplier);
                break;
        }

        return settings;

    }

    public List<PriceItem> getPriceItems(String supplier, String fileName){

        long start = System.currentTimeMillis();

        ArrayList<PriceItem> list = new ArrayList<>();

        Workbook workbook;

        try {
            workbook = WorkbookFactory.create(new File(fileName));
        } catch (IOException e) {
            logger.error("ERROR!!!" + e.getMessage());
            return list;
        }



        Map<String, Integer[]> settings = getSupplierSettings(supplier);


        for(Sheet sheet: workbook) {

            for (Row row: sheet) {

                Integer rowNumber = row.getRowNum();
                String comment = getStringValue(row, settings, "comment");
                String code = getStringValue(row, settings, "code");
                String name = getStringValue(row, settings, "name");
                Double price = getPrice(row, settings, "price");
                String stock = getStringValue(row, settings, "stock");

                if(!name.isEmpty() && price != 0 ) {

                    PriceItem priceItem = new PriceItem(supplier, rowNumber, comment, code, name, price, stock);

                    list.add(priceItem);

                }

            }

        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.out.println("  Excel read: total time taken = " + (end - start) + " ms");


        return list;

    }

    public void loadAllFiles(){


        File f = new File("D:\\_Share\\_pricer\\");
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
                                               public boolean accept(File dir, String name) {
                                                   return name.endsWith("xls") || name.endsWith("xlsx");
                                               }
                                           });

        for(File file: matchingFiles){

            logger.error("file: " + file.getAbsolutePath());


            String supplier = FilenameUtils.removeExtension(file.getName());


            List<PriceItem> list = getPriceItems(supplier, file.getAbsolutePath());



            logger.error("  supplier: " + supplier);

            logger.error("  rows count before delete: " + priceRepository.getRowsCount());

            priceRepository.deleteAll(supplier);

            logger.error("  rows count after delete: " + priceRepository.getRowsCount());


            priceRepository.saveAll(list);


            logger.error("  rows count after file parse: " + priceRepository.getRowsCount());

        }

    }

}
