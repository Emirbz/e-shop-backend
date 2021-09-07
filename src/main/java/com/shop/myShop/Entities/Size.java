package com.shop.myShop.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Size {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    @OneToMany(mappedBy = "size", cascade = CascadeType.MERGE, orphanRemoval = true)
    @JsonBackReference
    private Set<ProductSize> products = new HashSet<>();


    public Size() {

    }

    public Size(String filename, Long productId) {
        this.name = filename;
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

    public Set<ProductSize> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductSize> products) {
        this.products = products;
    }

}
