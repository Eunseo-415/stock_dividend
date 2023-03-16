package com.example.stock_dividend.service;

import com.example.stock_dividend.model.Company;
import com.example.stock_dividend.model.Dividend;
import com.example.stock_dividend.model.ScrapedResult;
import com.example.stock_dividend.persist.CompanyRepository;
import com.example.stock_dividend.persist.DividendRepository;
import com.example.stock_dividend.persist.entity.CompanyEntity;
import com.example.stock_dividend.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    public ScrapedResult getDividendByCompanyName(String companyName){
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow( () -> new RuntimeException("Company name does not exist: " + companyName));
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());
        List<Dividend> dividends = dividendEntities.stream().map(e-> Dividend.builder()
                                        .dividend(e.getDividend())
                                        .date(e.getDate())
                                        .build()).collect(Collectors.toList());
        return new ScrapedResult(Company.builder()
                .ticker(company.getTicker())
                .name(company.getName())
                .build()
        , dividends);
    }

}
