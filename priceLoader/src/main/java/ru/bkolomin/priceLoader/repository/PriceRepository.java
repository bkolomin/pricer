package ru.bkolomin.priceLoader.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.bkolomin.priceLoader.Models.PriceItem;

//@Transactional
@Repository
public class PriceRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PriceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer getRowsCount(){

        //SELECT count(*) FROM public.price;

        return 0;

    }

    public void deleteAll(String supplier){

        jdbcTemplate.update("DELETE FROM price WHERE supplier=(?)", supplier);
    }

    public void save(PriceItem priceItem){

        jdbcTemplate.update("INSERT INTO price (supplier, comment, code, name, price, stock) VALUES (?, ?, ?, ?, ?, ?)",
                priceItem.getSupplier(), priceItem.getComment(), priceItem.getCode(), priceItem.getName(), priceItem.getPrice(), priceItem.getStock());

    }

}
