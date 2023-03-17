package com.example.stock_dividend.scraper;

import com.example.stock_dividend.constants.Month;
import com.example.stock_dividend.model.Company;
import com.example.stock_dividend.model.Dividend;
import com.example.stock_dividend.model.ScrapedResult;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper{
    private static final String STATISTIC_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86400; //60 * 60 * 24
    @Override
    public ScrapedResult scrap(Company company){
        var scrapeResult = new ScrapedResult();
        scrapeResult.setCompany(company);
        try{
            long end = System.currentTimeMillis() / 1000;
            String url = String.format(STATISTIC_URL, company.getTicker(), START_TIME, end);
            Connection connection = Jsoup.connect(url);
            Document document =  connection.get();
            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element table = parsingDivs.get(0);
            Element tbody = table.children().get(1);
            List<Dividend> dividends = new ArrayList<>();
            for (Element e : tbody.children()){
                String txt =  e.text();
                if(!txt.endsWith("Dividend")){
                    continue;
                }
                String[] splits = txt.split(" ");
                int month = Month.strToNum(splits[0]);
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);
                String dividend = splits[3];

                if(month < 0){
                    throw new RuntimeException("Unexpected Month value: " + splits[0]);
                }

                dividends.add(new Dividend(LocalDateTime.of(year, month, day, 0, 0),dividend ));
            }
            scrapeResult.setDividends(dividends);
        } catch (Exception e){
            // TODO
            System.out.println(e.getMessage());
        }

        return scrapeResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker){
        String url = String.format(SUMMARY_URL, ticker, ticker);
        try{
            Document document = Jsoup.connect(url).get();
            Element titleEle = document.getElementsByTag("h1").get(0);
            String title = titleEle.text().split(" - ")[1].trim();
            return  new Company(ticker, title);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
