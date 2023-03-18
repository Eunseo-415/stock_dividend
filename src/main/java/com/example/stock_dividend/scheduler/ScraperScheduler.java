package com.example.stock_dividend.scheduler;

import com.example.stock_dividend.constants.CacheKey;
import com.example.stock_dividend.model.Company;
import com.example.stock_dividend.model.ScrapedResult;
import com.example.stock_dividend.persist.CompanyRepository;
import com.example.stock_dividend.persist.DividendRepository;
import com.example.stock_dividend.persist.entity.CompanyEntity;
import com.example.stock_dividend.persist.entity.DividendEntity;
import com.example.stock_dividend.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@EnableCaching
public class ScraperScheduler {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;


    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling(){
        log.info("Scraping scheduler is started");
        List<CompanyEntity> companyEntities = this.companyRepository.findAll();

        for(var company: companyEntities){
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(new Company(company.getTicker(), company.getName()));

            scrapedResult.getDividends().stream().map(e -> new DividendEntity(company.getId(),e))
                    .forEach(e -> {
                        boolean exist = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if(!exist){
                            this.dividendRepository.save(e);
                            log.info("insert new dividend: " + e.toString());
                        }
                    });

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }
}
