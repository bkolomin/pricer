package ru.bkolomin.priceLoader.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;

@Component
public class PriceService {//

    static Logger logger = LoggerFactory.getLogger(PriceLoaderApplication.class);

    private PriceRepository priceRepository;

    @Autowired
    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    private String getStringValue(Row row, Map<String, List<Integer>> settings, String name){

        String result = "";

        List<Integer> cellSettings = settings.get(name);

        if(cellSettings == null){
            return "";
        }


        for(int i = 0; i < cellSettings.size(); i++){

            Cell cell = row.getCell(cellSettings.get(i) - 1);

            if(cell == null || cell.getCellType() == CellType.BLANK) {

            }else if((name == "vcode" || name == "scode") && cell.getCellType() == CellType.NUMERIC){

                result = result + (result.isEmpty()?"":"; ") + String.format ("%d", (long)cell.getNumericCellValue());

            }else {

                result = result + (result.isEmpty()?"":"; ") + cell.toString();

            }

        }

        return result;

    }

    private Double getPrice(Row row, Map<String, List<Integer>> settings, String name){

        Double result = 0d;

        List<Integer> cellSettings = settings.get(name);

        if(cellSettings == null){
            return 0d;
        }


        for(int i = 0; i < cellSettings.size(); i++){

            Cell cell = row.getCell(cellSettings.get(i) - 1);

            if(cell == null || cell.getCellType() != CellType.NUMERIC){

            }else {

                result = cell.getNumericCellValue();

            }

        }

        return result;

    }

    public List<PriceItem> getPriceItems(String supplier, Map<String, Map<String, List<Integer>>> settings, String fileName){

        long start = System.currentTimeMillis();

        ArrayList<PriceItem> list = new ArrayList<>();

        Workbook workbook;

        try {
            workbook = WorkbookFactory.create(new File(fileName));
        } catch (IOException e) {
            logger.error("ERROR!!! ошибка открытия файла прайса: " + e.getMessage());
            return null;
        }



        Map<String, List<Integer>> supplierSettings = settings.get(supplier);

        if(supplierSettings == null)
            supplierSettings = settings.get("default");

        if(supplierSettings == null){
            logger.error("ERROR!!! не найдены настройки поставщика и настройки default");
            return null;
        }


        for(Sheet sheet: workbook) {

            for (Row row: sheet) {

                Integer rowNumber = row.getRowNum();
                String comment  = getStringValue(row,  supplierSettings, "comment");
                String scode    = getStringValue(row,  supplierSettings, "scode");
                String vcode    = getStringValue(row,  supplierSettings, "vcode");
                String name     = getStringValue(row,  supplierSettings, "name");
                Double price    = getPrice(row,        supplierSettings, "price");
                String stock    = getStringValue(row,  supplierSettings, "stock");

                if(!name.isEmpty() && price != 0 ) {

                    PriceItem priceItem = new PriceItem(supplier, rowNumber, comment, scode, vcode, name, price, stock, new Date());

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

        System.out.println("  excel read: total time taken = " + (end - start) + " ms");


        return list;

    }

    private Map<String, Map<String, List<Integer>>> readSettings(){

       Map<String, Map<String, List<Integer>>> settings = new HashMap<>();

       ObjectMapper mapper = new ObjectMapper();

       try {
            settings = mapper.readValue(new File("D:\\_Share\\_pricer\\settings\\settings.txt"), HashMap.class);
       } catch (IOException e) {
            e.printStackTrace();
            logger.error("  ERROR!!! ошибка при чтении настроек: " + e.getMessage());
            return null;
       }

       return settings;
    }

    public void loadAllFiles(){

        Map<String, Map<String,List<Integer>>> settings = readSettings();

        if(settings == null)
            return;



        File f = new File("D:\\_Share\\_pricer\\");
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
                                               public boolean accept(File dir, String name) {
                                                   return name.toUpperCase().endsWith("xls".toUpperCase()) || name.toUpperCase().endsWith("xlsx".toUpperCase());
                                               }
                                           });

        for(File file: matchingFiles){

            logger.error("file: " + file.getAbsolutePath());


            String supplier = FilenameUtils.removeExtension(file.getName());

            logger.error("  supplier: " + supplier);


            List<PriceItem> list = getPriceItems(supplier, settings, file.getAbsolutePath());

            if(list != null){

                priceRepository.deleteAll(supplier);

                priceRepository.saveAll(list);

                logger.error("  items added: " + list.size());

            }

        }

        logger.error("db size: " + priceRepository.getDBSize());


    }

}
