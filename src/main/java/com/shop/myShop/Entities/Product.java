package com.shop.myShop.Entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Product {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Size> sizes;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdded;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Collection collection;

    private Double price;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Picture> pictures;

    @Transient
    private Double newPrice;

    public Product() {
        this.dateAdded = new Date();
    }


    public Product(String name, String description, List<Size> sizes, Gender gender, Collection collection, Category category, Double price, ProductStatus status, List<Picture> pictures) {
        this.name = name;
        this.description = description;
        this.sizes = sizes;
        this.gender = gender;
        this.collection = collection;
        this.category = category;
        this.price = price;
        this.status = status;
        this.pictures = pictures;
        this.dateAdded = new Date();
//        category.addProduct(this);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Double getNewPrice() {
        return newPrice;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public void setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
    }

}
