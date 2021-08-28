package com.shop.myShop.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class SubCategory {


    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Picture picture;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "subCategory", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
