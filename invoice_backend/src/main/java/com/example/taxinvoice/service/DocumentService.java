package com.example.taxinvoice.service;

import com.example.taxinvoice.entity.Document;
import com.example.taxinvoice.entity.DocumentDetails;
import com.example.taxinvoice.repository.DocumentDetailsRepository;
import com.example.taxinvoice.repository.DocumentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DocumentService {

    private static final String TEMPLATE_PATH = "src/main/resources/templates/";
    private static final String GENERATED_PATH = "generated/";

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentDetailsRepository documentDetailsRepository;

    public File generateDocument(String email, String type, Map<String, String> placeholders) throws Exception {
        File templateFile = new File(TEMPLATE_PATH + type + ".docx");

        // Ensure the 'generated' directory exists
        File generatedDir = new File(GENERATED_PATH);
        if (!generatedDir.exists()) {
            generatedDir.mkdirs();
        }

        // 🔹 Calculate dynamic placeholders before replacing text
        placeholders = calculateDynamicValues(placeholders);

        // Create a new file inside 'generated/' directory
        File newFile = new File(GENERATED_PATH + UUID.randomUUID() + ".docx");

        try (FileInputStream fis = new FileInputStream(templateFile);
             XWPFDocument document = new XWPFDocument(fis);
             FileOutputStream fos = new FileOutputStream(newFile)) {

            // 🔹 Replace placeholders in paragraphs
            for (XWPFParagraph p : document.getParagraphs()) {
                replacePlaceholdersInParagraph(p, placeholders);
            }

            // 🔹 Replace placeholders inside tables
            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            replacePlaceholdersInParagraph(p, placeholders);
                        }
                    }
                }
            }

            document.write(fos);
        } catch (IOException e) {
            throw new RuntimeException("Error while processing the document", e);
        }

        // 🔹 Save document details in the database
        Document doc = new Document();
        doc.setUserEmail(email);
        doc.setDocumentType(type);
        doc.setFilePath(newFile.getAbsolutePath());
        documentRepository.save(doc);

        DocumentDetails details = new DocumentDetails();
        details.setDocument(doc);
        details.setData(new ObjectMapper().writeValueAsString(placeholders));
        documentDetailsRepository.save(details);

        return newFile;
    }

    /**
     * 🔹 Helper method to replace placeholders in a paragraph.
     * This ensures that split placeholders are correctly merged and replaced.
     */
    private void replacePlaceholdersInParagraph(XWPFParagraph paragraph, Map<String, String> placeholders) {
        StringBuilder fullText = new StringBuilder();
        List<XWPFRun> runs = paragraph.getRuns();

        if (runs == null || runs.isEmpty()) return;

        for (XWPFRun run : runs) {
            fullText.append(run.getText(0) == null ? "" : run.getText(0));
        }

        String updatedText = fullText.toString();

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            updatedText = updatedText.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        if (!updatedText.equals(fullText.toString())) {
            for (int i = runs.size() - 1; i >= 0; i--) {
                paragraph.removeRun(i);
            }
            XWPFRun newRun = paragraph.createRun();
            newRun.setText(updatedText);
        }
    }

    /**
     * 🔹 Calculates dynamic placeholders before replacing them in the document.
     */
    private Map<String, String> calculateDynamicValues(Map<String, String> placeholders) {
        Map<String, String> updatedPlaceholders = new HashMap<>(placeholders);
        try {
            double netPrice = 0.0;
            List<String> unitPrices = new ArrayList<>();
            List<String> quantities = new ArrayList<>();
            List<String> totals = new ArrayList<>();

            // Check for a single item case
            if (updatedPlaceholders.containsKey("unit_price") && updatedPlaceholders.containsKey("quantity")) {
                double unitPrice = Double.parseDouble(updatedPlaceholders.getOrDefault("unit_price", "0"));
                double quantity = Double.parseDouble(updatedPlaceholders.getOrDefault("quantity", "0"));
                double totalPrice = unitPrice * quantity;

                updatedPlaceholders.put("total", String.format("%.2f", totalPrice));
                netPrice = totalPrice;

                unitPrices.add(String.format("%.2f", unitPrice));
                quantities.add(String.format("%.2f", quantity));
                totals.add(String.format("%.2f", totalPrice));
            }

            // Handle multiple indexed items
            for (int i = 1; ; i++) {
                String unitPriceKey = "unit_price" + i;
                String quantityKey = "quantity" + i;
                String totalPriceKey = "total" + i;

                if (!updatedPlaceholders.containsKey(unitPriceKey) || !updatedPlaceholders.containsKey(quantityKey)) {
                    break;
                }

                double unitPrice = Double.parseDouble(updatedPlaceholders.getOrDefault(unitPriceKey, "0"));
                double quantity = Double.parseDouble(updatedPlaceholders.getOrDefault(quantityKey, "0"));
                double totalPrice = unitPrice * quantity;

                updatedPlaceholders.put(totalPriceKey, String.format("%.2f", totalPrice));
                netPrice += totalPrice;

                unitPrices.add(String.format("%.2f", unitPrice));
                quantities.add(String.format("%.2f", quantity));
                totals.add(String.format("%.2f", totalPrice));
            }

            // Consolidate values for single/multiple items into placeholders
            updatedPlaceholders.put("unit_price", String.join(", ", unitPrices));
            updatedPlaceholders.put("quantity", String.join(", ", quantities));
            updatedPlaceholders.put("total", String.join(", ", totals));

            // Net Price, GST, and Grand Total calculations
            updatedPlaceholders.put("net", String.format("%.2f", netPrice));
            double gstPrice = netPrice * 0.18;
            updatedPlaceholders.put("gst", String.format("%.2f", gstPrice));
            double grandTotal = netPrice + gstPrice;
            updatedPlaceholders.put("grand", String.format("%.2f", grandTotal));

            // Convert Grand Total to Words
            updatedPlaceholders.put("amount_words", convertNumberToWords((int) grandTotal) + " only");

            // Insert Today's Date
            String todayDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            updatedPlaceholders.put("date", todayDate);

        } catch (NumberFormatException e) {
            throw new RuntimeException("Error in numeric calculations: " + e.getMessage(), e);
        }

        return updatedPlaceholders;
    }



    /**
     * 🔹 Converts numbers to words (supports numbers up to Crores).
     */
    private static final String[] units = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
            "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] tens = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    private static String convertNumberToWords(int num) {
        if (num == 0) return "Zero";
        if (num < 20) return units[num];
        if (num < 100) return tens[num / 10] + (num % 10 == 0 ? "" : " " + units[num % 10]);
        if (num < 1000) return units[num / 100] + " Hundred" + (num % 100 == 0 ? "" : " " + convertNumberToWords(num % 100));
        if (num < 100000) return convertNumberToWords(num / 1000) + " Thousand" + (num % 1000 == 0 ? "" : " " + convertNumberToWords(num % 1000));
        if (num < 10000000) return convertNumberToWords(num / 100000) + " Lakh" + (num % 100000 == 0 ? "" : " " + convertNumberToWords(num % 100000));
        return convertNumberToWords(num / 10000000) + " Crore" + (num % 10000000 == 0 ? "" : " " + convertNumberToWords(num % 10000000));
    }
}
