package com.e_commerceapp.clothshops.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Blob;


//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

//    @Lob
//    private Blob image;

    private String downloadUrl;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    public Image() {
    }

    public Image(Long id, String fileName, String fileType, String downloadUrl, Product product) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
//        this.image = image;
        this.downloadUrl = downloadUrl;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
//
//    public Blob getImage() {
//        return image;
//    }
//
//    public void setImage(Blob image) {
//        this.image = image;
//    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
