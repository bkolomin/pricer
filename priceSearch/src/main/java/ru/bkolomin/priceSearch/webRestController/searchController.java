package ru.bkolomin.priceSearch.webRestController;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bkolomin.priceSearch.models.PriceItem;
import ru.bkolomin.priceSearch.service.SearchPriceService;

@RestController
public class searchController {

        private SearchPriceService searchPriceService;

        @Autowired
        public searchController(SearchPriceService searchPriceService) {
            this.searchPriceService = searchPriceService;
        }

        @RequestMapping("/rest/priceSearch")
        public ArrayList<PriceItem> searchPrices(@RequestParam(value="search", defaultValue="") String searchString) {
            ArrayList<PriceItem> items = (ArrayList<PriceItem>)searchPriceService.find(searchString);

            return items;
        }

}
