package com.shop.myShop.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Size {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    private Integer quantity;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;


    public Size(){

    }
    public Size(String filename, Long productId, Integer quantity) {
        this.name = filename;
        this.quantity = quantity;
        this.product = new Product();
        this.product.setId(productId);
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
