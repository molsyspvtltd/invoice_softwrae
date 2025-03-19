//package com.example.taxinvoice.util;
//
//import com.lowagie.text.*;
//import com.lowagie.text.pdf.*;
//import org.springframework.stereotype.Component;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//@Component
//public class PDFGenerator {
//
//    public byte[] generateDocumentPDF(List<com.example.taxinvoice.entity.Document> documents) throws IOException {
//        Document pdfDocument = new Document(PageSize.A4, 50, 50, 120, 70); // Adjust margins to fit header/footer
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        try {
//            PdfWriter writer = PdfWriter.getInstance(pdfDocument, out);
//            writer.setPageEvent(new HeaderFooterHandler());
//
//            pdfDocument.open();
//
//            // Title
//            Paragraph title = new Paragraph("TAX INVOICE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
//            title.setAlignment(Element.ALIGN_CENTER);
//            pdfDocument.add(title);
//
//            pdfDocument.add(new Paragraph("Dated: 24/04/2024 Ref: NolsS_ABC2014-2015_01", FontFactory.getFont(FontFactory.HELVETICA, 10)));
//            pdfDocument.add(Chunk.NEWLINE);
//
//            // Recipient Details
//            pdfDocument.add(new Paragraph("To,\nHead,\nDepartment of Biotechnology,\nCochin University of Science and Technology,\nUniversity Road, South Kalamassery,\nKalamassery, Kochi, Kerala 682022\nGSTIN: 23BBBLC0888J1ZJ", FontFactory.getFont(FontFactory.HELVETICA, 10)));
//            pdfDocument.add(Chunk.NEWLINE);
//
//            // Table
//            PdfPTable table = new PdfPTable(3);
//            table.setWidthPercentage(100);
//            table.setSpacingBefore(10f);
//            table.setWidths(new float[]{4, 2, 3});
//
//            addTableHeader(table, "Description Of Services", "Qty", "Price (INR)");
//            for (com.example.taxinvoice.entity.Document doc : documents) {
//                addTableRow(table, doc.getDescription(), String.valueOf(doc.getQuantity()), String.valueOf(doc.getTotalPrice()));
//            }
//
//            pdfDocument.add(table);
//            pdfDocument.add(Chunk.NEWLINE);
//
//            // Totals
//            pdfDocument.add(new Paragraph("Net Total: 10,593.00", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
//            pdfDocument.add(new Paragraph("GST @ 18%: 1,906.74", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
//            pdfDocument.add(new Paragraph("Grand Total (Round off): 12,500.00", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
//            pdfDocument.add(new Paragraph("Amount (Words): Rupees Twelve Thousand and Five Hundred Only", FontFactory.getFont(FontFactory.HELVETICA, 10)));
//
//            // Terms & Conditions
//            pdfDocument.add(new Paragraph("Terms & Conditions", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
//            pdfDocument.add(new Paragraph("1) 100% payment along with Raw data and Purchase Order.\n2) Payment through cheque/Draft in favor of 'Molsys Private Limited' or via NEFT/RTGS/IMPS.\n3) No other bank charge should be deducted.\n4) Interest of 1.5% per month on overdue payments.\n5) Include invoice number with payment.\n6) Data storage is chargeable.", FontFactory.getFont(FontFactory.HELVETICA, 9)));
//
//            // Signature
//            pdfDocument.add(new Paragraph("Yours sincerely,\nAuthorized Signature\nMolsys Pvt. Ltd., Yelahanka, Bangalore-65", FontFactory.getFont(FontFactory.HELVETICA, 10)));
//
//            pdfDocument.close();
//        } catch (DocumentException e) {
//            throw new IOException("Error generating PDF", e);
//        }
//        return out.toByteArray();
//    }
//
//    private void addTableHeader(PdfPTable table, String col1, String col2, String col3) {
//        table.addCell(createHeaderCell(col1));
//        table.addCell(createHeaderCell(col2));
//        table.addCell(createHeaderCell(col3));
//    }
//
//    private void addTableRow(PdfPTable table, String col1, String col2, String col3) {
//        table.addCell(new PdfPCell(new Phrase(col1)));
//        table.addCell(new PdfPCell(new Phrase(col2)));
//        table.addCell(new PdfPCell(new Phrase(col3)));
//    }
//
//    private PdfPCell createHeaderCell(String text) {
//        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
//        cell.setBackgroundColor(new GrayColor(0.75f));
//        return cell;
//    }
//
//    // Custom header & footer handler class
//    class HeaderFooterHandler extends PdfPageEventHelper {
//        private Image headerImage;
//        private Image footerImage;
//
//        public HeaderFooterHandler() {
//            try {
//                // Load header & footer images
//                headerImage = Image.getInstance("src/main/resources/static/header.png");
//                footerImage = Image.getInstance("src/main/resources/static/footer.png");
//
//                headerImage.scaleToFit(500, 100);
//                footerImage.scaleToFit(500, 100);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onEndPage(PdfWriter writer, Document document) {
//            PdfContentByte cb = writer.getDirectContent();
//            try {
//                if (headerImage != null) {
//                    headerImage.setAbsolutePosition(document.leftMargin(), document.top() + 10);
//                    cb.addImage(headerImage);
//                }
//
//                if (footerImage != null) {
//                    footerImage.setAbsolutePosition(document.leftMargin(), document.bottom() - 50);
//                    cb.addImage(footerImage);
//                }
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
