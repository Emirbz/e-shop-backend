package com.shop.myShop.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Picture {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String name;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    private Product product;
    private String url;

    public Picture(String filename, Long productId) {
        this.name = filename;
        this.url = "http://localhost/e-shop-backend/uploads/" + filename;
        this.product = new Product();
        this.product.setId(productId);
    }

    public Picture(String filename, String url) {
        this.name = filename;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Picture() {
        super();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
