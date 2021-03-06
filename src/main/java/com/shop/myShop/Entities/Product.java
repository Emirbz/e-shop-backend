package com.shop.myShop.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "product")
public class Product {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private Set<ProductSize> sizes = new HashSet<>();

    @Temporal(TemporalType.DATE)
    private Date dateAdded;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany()
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Category> categories = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Collection collection;

    private Double price;

    private String status;

    @Column(name = "is_drop")
    private boolean drop = false;


    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Picture> pictures;

    @Transient
    private Sale sale;

    public Product() {
        this.dateAdded = new Date();
    }

    public Product(String name, String description, Set<ProductSize> sizes, Gender gender, Collection collection, Set<Category> categories, Double price, String status, boolean drop, Set<Picture> pictures) {
        this.name = name;
        this.description = description;
        this.sizes = sizes;
        this.gender = gender;
        this.collection = collection;
        this.categories = categories;
        this.price = price;
        this.status = status;
        this.drop = drop;
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

    public boolean isDrop() {
        return drop;
    }

    public void setDrop(boolean drop) {
        this.drop = drop;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ProductSize> getSizes() {
        return sizes;
    }

    public void setSizes(Set<ProductSize> sizes) {
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public void addSize(Size size, int quantity) {
        ProductSize postSize = new ProductSize(this, size, quantity);
        if (!sizes.contains(postSize)) {
            sizes.add(postSize);
            size.getProducts().add(postSize);
        }
    }

    public void removeSize(Size size) {
        for (Iterator<ProductSize> iterator = sizes.iterator();
             iterator.hasNext(); ) {
            ProductSize productSize = iterator.next();

            if (productSize.getProduct().equals(this) &&
                    productSize.getSize().equals(size)) {
                iterator.remove();
                productSize.getProduct().getSizes().remove(productSize);
                productSize.setProduct(null);
                productSize.setSize(null);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Product post = (Product) o;
        return Objects.equals(name, post.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
