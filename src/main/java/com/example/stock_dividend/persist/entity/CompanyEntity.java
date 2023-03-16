package com.example.stock_dividend.persist.entity;

import com.example.stock_dividend.model.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "COMPANY")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String ticker;
    private String name;

    public CompanyEntity(Company company){
        this.name = company.getName();
        this.ticker = company.getTicker();
    }
}
