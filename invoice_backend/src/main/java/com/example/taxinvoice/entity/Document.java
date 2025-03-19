package com.example.taxinvoice.entity;//package com.example.taxinvoice.entity;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "documents")
//public class Document {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String documentType; // Tax Invoice, Quotation, Purchase Order
//    private String catalogNumber;
//    private String description;
//    private int quantity;
//    private double pricePerQuantity;
//    private double totalPrice;
//
//    public Document() {}
//
//    public Document(String documentType, String catalogNumber, String description, int quantity, double pricePerQuantity) {
//        this.documentType = documentType;
//        this.catalogNumber = catalogNumber;
//        this.description = description;
//        this.quantity = quantity;
//        this.pricePerQuantity = pricePerQuantity;
//        this.totalPrice = quantity * pricePerQuantity;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getDocumentType() {
//        return documentType;
//    }
//
//    public void setDocumentType(String documentType) {
//        this.documentType = documentType;
//    }
//
//    public String getCatalogNumber() {
//        return catalogNumber;
//    }
//
//    public void setCatalogNumber(String catalogNumber) {
//        this.catalogNumber = catalogNumber;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public double getPricePerQuantity() {
//        return pricePerQuantity;
//    }
//
//    public void setPricePerQuantity(double pricePerQuantity) {
//        this.pricePerQuantity = pricePerQuantity;
//    }
//
//    public double getTotalPrice() {
//        return totalPrice;
//    }
//}

import jakarta.persistence.*;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String filePath;

    @OneToOne(mappedBy = "document", cascade = CascadeType.ALL)
    private DocumentDetails documentDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DocumentDetails getDocumentDetails() {
        return documentDetails;
    }

    public void setDocumentDetails(DocumentDetails documentDetails) {
        this.documentDetails = documentDetails;
    }
}
