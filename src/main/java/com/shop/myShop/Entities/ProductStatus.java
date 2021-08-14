package com.shop.myShop.Entities;

public enum ProductStatus {
    Available("Available"), Sold_Out("Sold out");

    private String status;

    ProductStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
