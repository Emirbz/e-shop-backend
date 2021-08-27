package com.shop.myShop.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Size {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    private SizeName name;

    private Integer quantity;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    private Product product;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SizeName getName() {
        return name;
    }

    public void setName(SizeName name) {
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

enum SizeName {
    XS("XS"), S("S"), M("M"), L("L"), XL("XL"), XXL("XXL"), XXXL("XXXL"), THREE_EIGHT("48"), THREE_NINE("39"),
    FOURTY("40"), FOUR_ONE("41"), FOUR_TWO("42"), FOUR_THREE("43"), FOUR_FOUR("44"), FOUR_FIVE("45");
    private String size;

    SizeName(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return size;
    }
}