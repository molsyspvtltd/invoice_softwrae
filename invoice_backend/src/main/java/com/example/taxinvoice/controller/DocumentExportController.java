//package com.example.taxinvoice.controller;
//
//import com.example.taxinvoice.entity.Document;
//import com.example.taxinvoice.service.DocumentService;
//import com.example.taxinvoice.util.PDFGenerator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/export")
//public class DocumentExportController {
//
//    @Autowired
//    private DocumentService documentService;
//
//    @Autowired
//    private PDFGenerator pdfGenerator;
//
//    @GetMapping("/pdf")
//    public ResponseEntity<byte[]> exportDocumentsAsPDF() {
//        try {
//            List<Document> documents = documentService.getAllDocuments();
//
//            // Handle case where there are no documents
//            if (documents.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
//            }
//
//            byte[] pdfContent = pdfGenerator.generateDocumentPDF(documents);
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documents.pdf")
//                    .contentType(MediaType.APPLICATION_PDF)
//                    .body(pdfContent);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//}
