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

        jdbcTemplate.update("INSERT INTO price (supplier, comment, scode, vcode, name, price, stock, priceDate) VALUES (?, ?, ?, ?, ?, ?)",
                priceItem.getSupplier(), priceItem.getComment(), priceItem.getScode(), priceItem.getVcode(), priceItem.getName(), priceItem.getPrice(), priceItem.getStock(), priceItem.getPriceDate());

    }

    public List<PriceItem> find(String searchString){

        String[] words = searchString.split(" ");

        String condition = "";

        for(int i = 0; i < words.length; i++){

            condition = condition + (condition.length() == 0?"":" AND ") + "(UPPER(CONCAT(price.name, ' ', price.vcode, ' ', price.scode)) LIKE '%' || UPPER(?) || '%')";

        }


        String sql = "SELECT * FROM price WHERE ? ORDER BY price.price LIMIT 100;".replace("?", condition);

        List<PriceItem> list = jdbcTemplate.query(sql, ROW_MAPPER, words);

        //List<PriceItem> list = jdbcTemplate.query("SELECT * FROM price WHERE UPPER(name) LIKE  '%' || UPPER(?) || '%' LIMIT 100;", ROW_MAPPER, "ЧЕРНЫЙ");

        return list;

    }

}
