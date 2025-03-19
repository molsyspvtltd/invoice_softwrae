package com.example.taxinvoice.controller;

import com.example.taxinvoice.entity.Document;
import com.example.taxinvoice.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestParam String email, @RequestParam String type,
                                           @RequestBody Map<String, String> placeholders) throws Exception {
        File file = documentService.generateDocument(email, type, placeholders);
        return ResponseEntity.ok(file.getAbsolutePath());
    }
}
