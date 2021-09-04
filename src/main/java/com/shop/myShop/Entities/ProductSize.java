package com.shop.myShop.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class ProductSize implements Serializable {

    @EmbeddedId
    private ProductSizeId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("productId")
    @JsonBackReference
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("sizeId")
    private Size size;

    private int quantity;

    public ProductSize() {
    }

    public ProductSize(Product product, Size size, int quantity) {
        this.product = product;
        this.size = size;
        this.quantity = quantity;
        this.id = new ProductSizeId(product.getId(), size.getId());
    }

    public ProductSizeId getId() {
        return id;
    }

    public void setId(ProductSizeId id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ProductSize that = (ProductSize) o;
        return Objects.equals(product, that.product) &&
                Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, size);
    }
}
