package com.example.taxinvoice.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class QuotationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String catNo;
    private String description;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "quotation_id")
    private Quotation quotation;

    public QuotationItem() {}

    public QuotationItem(Long id, String catNo, String description, int quantity, BigDecimal unitPrice, BigDecimal total, Quotation quotation) {
        this.id = id;
        this.catNo = catNo;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = total;
        this.quotation = quotation;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatNo() {
        return catNo;
    }

    public void setCatNo(String catNo) {
        this.catNo = catNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }
}
