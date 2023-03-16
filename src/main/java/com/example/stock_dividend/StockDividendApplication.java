package com.example.stock_dividend;

import com.example.stock_dividend.model.Company;
import com.example.stock_dividend.scraper.YahooFinanceScraper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class StockDividendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockDividendApplication.class, args);

    }

}
