package com.example.taxinvoice.controller;

import com.example.taxinvoice.entity.NumberToWordsConverter;
import com.example.taxinvoice.entity.PurchaseOrderItemDTO;
import com.example.taxinvoice.entity.PurchaseOrderRequestDTO;
import com.example.taxinvoice.service.PurchaseOrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-order")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generatePurchaseOrder(@RequestBody PurchaseOrderRequestDTO requestDTO) {
        try {
            // ✅ Get authenticated user's email
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // Extract email from token

            // ✅ Calculate totals
            BigDecimal netTotal = requestDTO.getItems().stream()
                    .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal gst = netTotal.multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal grandTotal = netTotal.add(gst);
            String amountWords = NumberToWordsConverter.convert(grandTotal.intValue());

            // ✅ Call the correct method
            File generatedFile = purchaseOrderService.generateWordFileAndSaveToDB(
                    userEmail, requestDTO.getPoNumber(), requestDTO.getDate(), requestDTO.getToAddress(),
                    requestDTO.getGstNumber(), requestDTO.getItems(), netTotal, gst, grandTotal, amountWords
            );

            return ResponseEntity.ok("File saved at: " + generatedFile.getAbsolutePath());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
