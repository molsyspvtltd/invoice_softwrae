package com.example.taxinvoice.repository;

import com.example.taxinvoice.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
}
