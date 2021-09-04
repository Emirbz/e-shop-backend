package com.shop.myShop.Entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductSizeId implements Serializable {

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "size_id")
    private Long sizeId;

    public ProductSizeId() {
    }

    public ProductSizeId(Long productId, Long sizeId) {
        this.productId = productId;
        this.sizeId = sizeId;
    }

    //Getters omitted for brevity

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        ProductSizeId that = (ProductSizeId) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(sizeId, that.sizeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, sizeId);
    }
}
