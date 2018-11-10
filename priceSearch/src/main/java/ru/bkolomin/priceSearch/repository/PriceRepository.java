package ru.bkolomin.priceSearch.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.bkolomin.priceSearch.models.PriceItem;

import java.util.ArrayList;
import java.util.List;

//@Transactional
@Repository
public class PriceRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final BeanPropertyRowMapper<PriceItem> ROW_MAPPER = BeanPropertyRowMapper.newInstance(PriceItem.class);

    @Autowired
    public PriceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer getRowsCount(){

        String sql = "SELECT count(*) FROM price;";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

        return count;

    }

    public void deleteAll(String supplier){

        jdbcTemplate.update("DELETE FROM price WHERE supplier=(?)", supplier);
    }

    public void save(PriceItem priceItem){

        jdbcTemplate.update("INSERT INTO price (supplier, comment, code, name, price, stock) VALUES (?, ?, ?, ?, ?, ?)",
                priceItem.getSupplier(), priceItem.getComment(), priceItem.getCode(), priceItem.getName(), priceItem.getPrice(), priceItem.getStock());

    }

    public List<PriceItem> findByName(String name){

        String[] words = name.toUpperCase().split(" ");

        String condition = "";

        for(int i = 0; i < words.length; i++){

            condition = condition + (condition.length() == 0?"":" AND ") + "(price.name LIKE '%' || UPPER(?) || '%')";

        }


        String sql = "SELECT * FROM price WHERE ? LIMIT 100;".replace("?", condition);

        List<PriceItem> list = jdbcTemplate.query(sql, ROW_MAPPER, words);

        return list;

    }

}
