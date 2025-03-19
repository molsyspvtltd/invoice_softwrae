package com.example.taxinvoice.repository;

import com.example.taxinvoice.entity.DocumentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface DocumentDetailsRepository extends JpaRepository<DocumentDetails, Long> {
    }


