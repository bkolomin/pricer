package ru.bkolomin.priceSearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bkolomin.priceSearch.models.PriceItem;
import ru.bkolomin.priceSearch.repository.PriceRepository;

import java.util.List;

@Service
public class SearchPriceService {

    private PriceRepository priceRepository;

    @Autowired
    public SearchPriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public List<PriceItem> findByName(String name){

        return this.priceRepository.findByName(name);

    }

}
