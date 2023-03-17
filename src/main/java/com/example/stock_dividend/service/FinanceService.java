package com.example.stock_dividend.service;

import com.example.stock_dividend.constants.CacheKey;
import com.example.stock_dividend.model.Company;
import com.example.stock_dividend.model.Dividend;
import com.example.stock_dividend.model.ScrapedResult;
import com.example.stock_dividend.persist.CompanyRepository;
import com.example.stock_dividend.persist.DividendRepository;
import com.example.stock_dividend.persist.entity.CompanyEntity;
import com.example.stock_dividend.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName){
        log.info("Search company: " + companyName);
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow( () -> new RuntimeException("Company name does not exist: " + companyName));
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());
        List<Dividend> dividends = dividendEntities.stream()
                .map(e-> new Dividend(e.getDate(), e.getDividend()))
                .collect(Collectors.toList());
        return new ScrapedResult(new Company(company.getTicker(), company.getName())
        , dividends);
    }

}
