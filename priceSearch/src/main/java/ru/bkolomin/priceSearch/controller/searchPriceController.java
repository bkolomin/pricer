package ru.bkolomin.priceSearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bkolomin.priceSearch.models.PriceItem;
import ru.bkolomin.priceSearch.service.SearchPriceService;

import java.util.ArrayList;

@Controller
public class searchPriceController {

    private SearchPriceService searchPriceService;

    @Autowired
    public searchPriceController(SearchPriceService searchPriceService) {
        this.searchPriceService = searchPriceService;
    }

    @GetMapping("/priceSearch")
    public String priceSearch(@RequestParam(name="search", required=false, defaultValue = "") String searchString, Model model) {

        ArrayList<PriceItem> items = (ArrayList<PriceItem>)searchPriceService.findByName(searchString);

        model.addAttribute("items", items);

        model.addAttribute("currentSearchString", searchString);

        return "priceSearch";
    }

}

