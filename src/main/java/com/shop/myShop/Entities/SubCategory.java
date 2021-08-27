package com.shop.myShop.Entities;

import javax.persistence.*;

@Entity
public class SubCategory {


    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    @OneToOne(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Picture picture;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Category category;

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
