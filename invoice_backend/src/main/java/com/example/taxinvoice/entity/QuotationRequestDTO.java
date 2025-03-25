package com.example.taxinvoice.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class QuotationRequestDTO {
    private String refNo;
    private LocalDate date;
    private String toAddress;
    private String gstNumber;
    private List<QuotationItemDTO> items;

    // Getters and Setters

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public List<QuotationItemDTO> getItems() {
        return items;
    }

    public void setItems(List<QuotationItemDTO> items) {
        this.items = items;
    }
}
