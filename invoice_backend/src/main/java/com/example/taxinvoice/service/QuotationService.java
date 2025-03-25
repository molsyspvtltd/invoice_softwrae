package com.example.taxinvoice.service;

import com.example.taxinvoice.entity.QuotationItemDTO;
import com.example.taxinvoice.entity.User;
import com.example.taxinvoice.repository.QuotationRepository;
import com.example.taxinvoice.repository.UserRepository;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final UserRepository userRepository;

    public QuotationService(QuotationRepository quotationRepository, UserRepository userRepository) {
        this.quotationRepository = quotationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public File generateWordFileAndSaveToDB(String userEmail, String refNumber, LocalDate date, String toAddress,
                                            String gstNumber, List<QuotationItemDTO> items,
                                            BigDecimal netTotal, BigDecimal gst, BigDecimal grandTotal,
                                            String amountWords) throws IOException {

        // ✅ Fetch the authenticated user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Create "generated" folder if it doesn't exist
        String directoryPath = "generated";
        Files.createDirectories(Paths.get(directoryPath));

        // ✅ Define file path
        String fileName = directoryPath + "/Quotation_" + refNumber + "_" + System.currentTimeMillis() + ".docx";

        // ✅ Create a new Word document
        XWPFDocument document = new XWPFDocument();

        // ✅ Add Full-Width Header and Footer (with increased height)
        addFullWidthHeaderAndFooter(document, "header.png", "footer.png");

        // ✅ Add Title
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        setFont(titleRun, "Century Gothic", 18, false);
        titleRun.setText("Quotation");

        // ✅ Add Order Details
        XWPFParagraph details = document.createParagraph();
        details.setAlignment(ParagraphAlignment.LEFT);

// ✅ Ensure paragraph properties exist
        if (details.getCTP().getPPr() == null) {
            details.getCTP().addNewPPr();
        }

// ✅ Ensure tabs exist before adding a new tab stop
        if (details.getCTP().getPPr().getTabs() == null) {
            details.getCTP().getPPr().addNewTabs();
        }

// ✅ Add a right-aligned tab stop
        org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop tabStop =
                details.getCTP().getPPr().getTabs().addNewTab();
        tabStop.setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc.RIGHT);
        tabStop.setPos(9000); // Adjust as needed

        XWPFRun detailsRun = details.createRun();
        setFont(detailsRun, "Century Gothic", 11, false);

// ✅ Add details with explicit tabs and line breaks
        detailsRun.setText("Date: " + date.toString());
        detailsRun.addTab(); // Moves to the right tab stop
        detailsRun.setText("Ref No: " + refNumber);
        detailsRun.addBreak(); // ✅ Adds a new line
        detailsRun.setText("To: " + toAddress);
        detailsRun.addBreak();
        detailsRun.setText("GST: " + gstNumber);
        detailsRun.addBreak();
        detailsRun.addBreak();


        // ✅ Create a table with borders
        XWPFTable table = document.createTable();
        setTableBorders(table); // ✅ Set Borders
        table.setWidth("100%"); // ✅ Makes table use full width

        // ✅ Create table headers
        XWPFTableRow headerRow = table.getRow(0);
//        createTableCell(headerRow.getCell(0), "Sl No", true);
        createTableCell(headerRow.getCell(0), "Cat No.", true, 2000);
        createTableCell(headerRow.addNewTableCell(), "Description of Sevices", true,5000);
        createTableCell(headerRow.addNewTableCell(), "Qty", true,1500);
        createTableCell(headerRow.addNewTableCell(), "Price (INR) Per Sample", true,2500);
//        createTableCell(headerRow.addNewTableCell(), "Total (INR)", true);

        // ✅ Populate table with data
        int index = 1;
        for (QuotationItemDTO item : items) {
            XWPFTableRow row = table.createRow();
//            createTableCell(row.getCell(0), String.valueOf(index++), false);
            createTableCell(row.getCell(0), item.getCatNo(), false,2000);
            createTableCell(row.getCell(1), item.getDescription(), false,5000);
            createTableCell(row.getCell(2), String.valueOf(item.getQuantity()), false,1500);
            createTableCell(row.getCell(3), formatCurrency(item.getUnitPrice()), false,2500);
            BigDecimal total = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
//            createTableCell(row.getCell(5), formatCurrency(total), false);
        }

        // ✅ Add Net Total, GST, Grand Total in Table Rows
        XWPFTableRow netTotalRow = table.createRow();
        netTotalRow.getCell(0).setText(""); // Empty Cells
        createTableCell(netTotalRow.getCell(1), "Net Total", true,5000);
        netTotalRow.getCell(2).setText("");
        createTableCell(netTotalRow.getCell(3), formatCurrency(netTotal), false,2500);

        XWPFTableRow gstRow = table.createRow();
        gstRow.getCell(0).setText("");
        createTableCell(gstRow.getCell(1), "GST (18%)", true,5000);
        createTableCell(gstRow.getCell(3), formatCurrency(gst), false,2500);

        XWPFTableRow grandTotalRow = table.createRow();
        grandTotalRow.getCell(0).setText("");
        createTableCell(grandTotalRow.getCell(1), "Grand Total", true,5000);
        grandTotalRow.getCell(2).setText("");
        createTableCell(grandTotalRow.getCell(3), formatCurrency(grandTotal), false,2500);



        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                cell.setWidth("auto"); // ✅ Auto-adjusts each column
            }
        }


        XWPFParagraph amountWordsParagraph = document.createParagraph();
        amountWordsParagraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun amountWordsRun = amountWordsParagraph.createRun();
        setFont(amountWordsRun, "Century Gothic", 11, false);
        amountWordsRun.setText("Amount (Words): Rupees " + amountWords + " Only");
        amountWordsRun.addBreak();
        amountWordsRun.addBreak();

        XWPFParagraph staticDetails = document.createParagraph();
        staticDetails.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun staticRun = staticDetails.createRun();
        setFont(staticRun, "Century Gothic", 11, false);

        XWPFParagraph para1 = document.createParagraph();
        XWPFRun staticRun1 = para1.createRun();
        staticRun1.setBold(true);
        staticRun1.setUnderline(UnderlinePatterns.SINGLE);
        setFont(staticRun1, "Century Gothic", 9, true);
        XWPFParagraph para1Content = document.createParagraph();
        XWPFRun normalRun1 = para1Content.createRun();
        setFont(normalRun1, "Century Gothic", 9, false);

        XWPFParagraph para2 = document.createParagraph();
        XWPFRun staticRun2 = para2.createRun();
        staticRun2.setBold(true);
        staticRun2.setUnderline(UnderlinePatterns.SINGLE);
        setFont(staticRun2, "Century Gothic", 9, true);
        XWPFParagraph para2Content = document.createParagraph();
        XWPFRun normalRun2 = para2Content.createRun();
        setFont(normalRun2, "Century Gothic", 9, false);

        XWPFParagraph para3 = document.createParagraph();
        para3.setAlignment(ParagraphAlignment.CENTER); // Center alignment
        XWPFRun staticRun3 = para3.createRun();
        setFont(staticRun3, "Century Gothic", 15, false);
        staticRun3.setBold(true);
        staticRun3.setFontSize(15);
        XWPFParagraph para3Content = document.createParagraph();
        XWPFRun normalRun3 = para3Content.createRun();
        setFont(normalRun3, "Century Gothic", 9, false);

// Adding HSN Code & GSTIN
        staticRun.setText("HSN Code: 998143");
        staticRun.addBreak();
        staticRun.setText("GSTIN: 29AALCM9252C1Z7");
        staticRun.addBreak();
        staticRun.addBreak();

// ✅ Adding Sample Prerequisite
        staticRun1.setText("Sample Prerequisite:");
        staticRun1.addBreak();
        normalRun1.setText("RIN > 7");
        normalRun1.addBreak();
        normalRun1.setText("Concentration >150ng/ul Total Amount – 5 ug");
        normalRun1.addBreak();
        normalRun1.setText("OD – A260/280>1.8, A260/230>2 Gel Picture with clear band");
        normalRun1.addBreak();
        normalRun1.addBreak();

// ✅ Adding List of Deliverables
        staticRun2.setText("List of Deliverables:");
        staticRun2.addBreak();

        String[] deliverables = {
                "1. Individual Raw FASTQ FastQC report",
                "2. Raw FASTQ MultiQC report",
                "3. Individual processed FASTQ FastQC report",
                "4. Processed FASTQ MultiQC report",
                "5. Mapping statistics of individual samples with genome",
                "6. Gene / transcript absolute counts of individual samples",
                "7. FPKM normalized gene / transcript counts of individual samples",
                "8. The principal component analysis (PCA) plot generated in DEseq2 showing variation within and between groups",
                "9. Sample to Sample distance cluster plot based on their Euclidean distance using the regularized log-transformed count data",
                "10. The complete set of upregulated and down-regulated gene counts will be provided in .xls format.",
                "11_PLACEHOLDER",
                "12. Heatmap of 50 most variable genes / transcripts",
                "13. MA plot (based on suggested statistical test and LogFC)",
                "14. Volcano plot (based on suggested statistical test and LogFC)",
                "15. For organisms (if NCBI GeneID is available at NCBI gene2go list), GESA analysis will be performed against GO, KEGG, and Reactome database"
        };

        for (String item : deliverables) {
            if (item.equals("11_PLACEHOLDER")) {
                // Handle 11th item separately
                normalRun2.setText("11. Annotated DESeq2 result file with Chromosome Number, Gene name, Gene Description, Start and end position, count values of individual samples, unadjusted p-value, adjusted p-value, Fold change, log2foldchange.");
                normalRun2.addBreak();
                normalRun2.addBreak();
                normalRun2.setText("   NOTE: Annotated DESeq2 result will be filtered based on the following 6 cutoffs:");
                normalRun2.addBreak();
                normalRun2.addBreak();
                normalRun2.setText("   1. FDR <0.05 AND LOG2FC 2");
                normalRun2.addBreak();
                normalRun2.setText("   2. FDR <0.05 AND LOG2FC 1.5");
                normalRun2.addBreak();
                normalRun2.setText("   3. FDR <0.05 AND LOG2FC 1");
                normalRun2.addBreak();
                normalRun2.setText("   4. Uncorrected p-value <0.05 AND LOG2FC 2");
                normalRun2.addBreak();
                normalRun2.setText("   5. Uncorrected p-value <0.05 AND LOG2FC 1.5");
                normalRun2.addBreak();
                normalRun2.setText("   6. Uncorrected p-value <0.05 AND LOG2FC 1");
                normalRun2.addBreak();
                normalRun2.addBreak();

                normalRun2.setText("   Client needs to suggest one cut-off value, based on which downstream analysis will be performed.");
                normalRun2.addBreak();
            } else {
                // Normal items
                normalRun2.setText(item);
                normalRun2.addBreak();
            }
        }
        normalRun2.addBreak();

// ✅ Adding Terms & Conditions
        staticRun3.setText("Terms & Conditions:");
        staticRun3.addBreak();

        String[] terms = {
                "1) The validity of the quotation is for 45 days.",
                "2) Sample Shipping charges additional if arranged by Molsys.",
                "3) The Data Generation shall be completed in 45-60 days from the date when the sample qualifies DNA QC.",
                "4) The project shall be completed within 20 days for the primary analysis.",
                "5) Sample QC carried out at Molsys facility will be considered final. QC charges will be applicable for all samples which will be subjected for QC in spite of the QC status achieved. QC passed samples will be categorized in Grade (A, B & C) depending upon the OD (by nanodrop), concentration & quantity.",
                "6) Library preparation will be carried out only for those samples for which the client agrees and provides written/e-mail consent. Molsys will make a maximum of two attempts to prepare library from the agreed samples. In case the samples fail to generate libraries post two attempts, Molsys will ask the client to replace these samples and the replaced fresh samples shall be charged additionally.",
                "7) Library QC carried out at Molsys facility will be considered final. Molsys cannot be held responsible for non-performance of a library based on library QC report generated outside Molsys.",
                "8) Molsys will proceed with data generation only for those samples for which the client provides a written/e-mail consent. In case of a Grade ‘A’ sample fails to generate promised data, Molsys will take a maximum of one additional attempt to achieve the required data using the same library. In case the sample still fails to generate sufficient data, Molsys will ask the client to replace the concerned sample. The replaced samples will be considered and will be billed as additional sample. Molsys shall not be responsible for data loss due to poor sequencing quality due to poor quality samples (Grade ‘B’ & Grade ‘C’).",
                "9) Molsys expects the client cooperation in maintaining project timeline by reverting back within 3 working days (Saturday included) from the date a QC report (sample/library/data) has been shared in order to make sure that the promised timeline for the project is met. In case the client fails to respond within 3 days, Molsys cannot be held responsible in case any delay is observed from the promised timeline.",
                "10) In case any of the samples have to be repeated (as per client’s consent), these repeated samples will follow their own fresh timeline i.e., 45-60 days from the day sample passes QC.",
                "11) In case the client fails to provide consent on whether to proceed with the samples, Molsys will store the DNA samples free of cost for a maximum of 2 months. If the client wishes to store the samples any further than 2 months, the same can be done at a chargeable basis (charges – vial or tube – INR 100/per month, Plate – INR 500/per month). These charges will be billed independently from the concerned project and will have to be paid in full advance. Any failure in making this payment even after 2 reminders will result in the samples being discarded without any further intimation to the client.",
                "12) In case the client fails to provide consent on whether to proceed with the library, Molsys will store library free of cost for 45 days and on a chargeable basis for the next 45 days (charges – INR 200/library). These charges will be billed independently from the concerned project and will have to be paid in full advance.",
                "13) In case the client wishes to proceed with any library which is more than 45 days old, Molsys will not guarantee efficient data generation and the client will be responsible to bear the full charges of these samples independent from the final data quality/quantity achieved.",
                "14) Raw Data shall be shipped over cloud link or hard drive (user provided), only after receiving full payment.",
                "15) The link provided for data sharing will be active for 15 days only. If the client wishes the data to be stored further, the same can be availed on a chargeable basis as mentioned below:",
        };

// Add general terms before handling 16th separately
        for (String term : terms) {
            normalRun3.setText(term);
            normalRun3.addBreak();
        }

// Handle the 16th point separately to format subpoints properly
        normalRun3.setText("16) Data Storage charges applicable; billed separately each month:");
        normalRun3.addBreak();
        normalRun3.setText("   a. <15 Days – Free of charge");
        normalRun3.addBreak();
        normalRun3.setText("   b. >15 Days – INR USD 0.02/GB/Month");
        normalRun3.addBreak();
        normalRun3.addBreak();

// Continue remaining terms
        String[] remainingTerms = {
                "17) Molsys cannot be held responsible for any delay in project timeline due to unforeseen circumstances like non-availability of kits (essential for NGS work) in Indo-Pacific region, etc. In such situations, the same will be notified to the client with proper documented reason. We will expect full cooperation from clients in such adverse conditions.",
                "18) In case the client wishes to discontinue the project in situations (relevant to) explained in point 17, the same can be done after a waiting period of over 45 days from the last date of promised timeline. No storage fees for sample or libraries will be charged in such cases.",
                "19) In case the client wishes to discontinue the project after the samples have been processed for sample QC without achieving a written mutual consent between Molsys and client, Molsys will have complete authority to charge for the full compensation of the project.",
                "20) In case client wishes to terminate the project after fulfilling the conditions mentioned in point 18, Molsys will only charge for whatever extent the project has been completed.",
                "21) In case of termination of project, the client can request back the left-out quantity of samples after full financial settlement. The shipment charges for the same will have to be completely borne by the client.",
                "22) Molsys administers best possible practices and intends to provide best possible service within promised timeline to all our customers. Still, in case of any discrepancy between Molsys and the client, the same will have to be resolved by the judicial system based in Bangalore (Bengaluru), India only.",
                "23) Payment within 20 days of submitting invoice.",
                "24) 100% advance payment.",
                "25) The payment shall be through cheque/Draft in favor of 'Molsys Private Limited' or to the account through NEFT/RTGS/IMPS:",
                "   Bank Name: ICICI, Sahakarnagar branch",
                "   A/c Name: Molsys Private Limited",
                "   A/c No.: 060405002959 (Current A/c)",
                "   IFSC: ICIC0000604",
                "   GSTIN: 29AALCM9252C1Z7",
                "   SWIFT CODE: ICICINBBCTS"
        };

// Add remaining terms
        for (String term : remainingTerms) {
            normalRun3.setText(term);
            normalRun3.addBreak();
            normalRun3.addBreak();
        }


// ✅ Add Signature block
        XWPFParagraph signature = document.createParagraph();
        signature.setAlignment(ParagraphAlignment.LEFT);

        try {
            // ✅ Load image from resources folder
            InputStream sigImageStream = getClass().getClassLoader().getResourceAsStream("sign.png");

            if (sigImageStream == null) {
                throw new FileNotFoundException("Signature image not found in resources!");
            }

            // ✅ Add signature image
            XWPFRun imageRun = signature.createRun();
            imageRun.addPicture(sigImageStream, Document.PICTURE_TYPE_PNG, "sign.png", Units.toEMU(100), Units.toEMU(50)); // Adjust size
            sigImageStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

// ✅ Add Signature Text Below the Image
        XWPFRun signatureRun = signature.createRun();
        setFont(signatureRun, "Century Gothic", 11, true);
        signatureRun.addBreak(); // Ensure space after the image
        signatureRun.setText("Yours sincerely,");
        signatureRun.addBreak();
        signatureRun.setText("Authorized Signature,");
        signatureRun.addBreak();
        signatureRun.setText("Molsys Pvt. Ltd.");
        signatureRun.addBreak();
        signatureRun.setText("Yelahanka, Bangalore-65");

// ✅ Save the document
        File outputFile = new File(fileName);
        FileOutputStream fos = new FileOutputStream(outputFile);
        document.write(fos);
        fos.close();
        document.close();

        return outputFile;

    }


    private void setTableBorders(XWPFTable table) {
        table.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
        table.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
        table.setBottomBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
        table.setTopBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
        table.setLeftBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");
        table.setRightBorder(XWPFTable.XWPFBorderType.SINGLE, 4, 0, "000000");

        // ✅ Apply borders to each cell in the table
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                addBordersToCell(cell);
            }
        }
    }

    private void addBordersToCell(XWPFTableCell cell) {
        CTTcBorders borders = cell.getCTTc().addNewTcPr().addNewTcBorders();
        borders.addNewTop().setVal(STBorder.SINGLE);
        borders.addNewBottom().setVal(STBorder.SINGLE);
        borders.addNewLeft().setVal(STBorder.SINGLE);
        borders.addNewRight().setVal(STBorder.SINGLE);
    }

    private void addFullWidthHeaderAndFooter(XWPFDocument document, String headerImagePath, String footerImagePath) throws IOException {
        XWPFHeaderFooterPolicy policy = document.createHeaderFooterPolicy();

        // ✅ Add full-width header
        XWPFHeader header = policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);
        addFullWidthImage(header, headerImagePath, 80); // ✅ Increased height

        // ✅ Add full-width footer
        XWPFFooter footer = policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
        addFullWidthImage(footer, footerImagePath, 80); // ✅ Increased height

        addPageNumber(document);
    }

    private void addFullWidthImage(XWPFHeaderFooter headerFooter, String imagePath, int height) throws IOException {
        XWPFTable table = headerFooter.createTable(1, 1);
        table.setWidth("100%");
        XWPFTableCell cell = table.getRow(0).getCell(0);
        XWPFParagraph paragraph = cell.addParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun run = paragraph.createRun();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(imagePath)) {
            if (inputStream != null) {
                run.addPicture(inputStream, Document.PICTURE_TYPE_PNG, imagePath, Units.toEMU(500), Units.toEMU(60));
            } else {
                System.out.println("Image not found: " + imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTableCell(XWPFTableCell cell, String text, boolean isHeader,int width) {
        cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(width));
        XWPFParagraph paragraph = cell.addParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run = paragraph.createRun();
        setFont(run, "Century Gothic", 11, isHeader);
        run.setText(text);
    }

    private void setFont(XWPFRun run, String font, int size, boolean bold) {
        run.setFontFamily(font);
        run.setFontSize(size);
        run.setBold(bold);
    }

    private String formatCurrency(BigDecimal value) {
        return NumberFormat.getCurrencyInstance(new Locale("en", "IN")).format(value);
    }

    public void addPageNumber(XWPFDocument document) {
        XWPFFooter footer = document.createFooter(HeaderFooterType.DEFAULT);
        XWPFParagraph paragraph = footer.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.CENTER); // Center the page number

        CTP ctp = paragraph.getCTP();

        // ✅ Add "Page " text
        XWPFRun run1 = paragraph.createRun();
        run1.setFontSize(9);
        run1.setFontFamily("Century Gothic");
        run1.setText("Page ");

        // ✅ Add dynamic page number (X)
        CTSimpleField pageNumberField = ctp.addNewFldSimple();
        pageNumberField.setInstr("PAGE \\* MERGEFORMAT"); // Inserts current page number
        pageNumberField.setDirty(true);

        // ✅ Add " of " text
        XWPFRun run2 = paragraph.createRun();
        run2.setFontSize(9);
        run2.setFontFamily("Century Gothic");
        run2.setText(" of ");

        // ✅ Add total page count (Y)
        CTSimpleField totalPagesField = ctp.addNewFldSimple();
        totalPagesField.setInstr("NUMPAGES \\* MERGEFORMAT"); // Inserts total pages
        totalPagesField.setDirty(true);
    }

}
