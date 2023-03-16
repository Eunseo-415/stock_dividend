package com.example.stock_dividend.scraper;

import com.example.stock_dividend.model.Company;
import com.example.stock_dividend.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
