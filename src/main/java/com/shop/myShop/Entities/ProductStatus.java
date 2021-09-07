package com.shop.myShop.Entities;

public enum ProductStatus {
    Disponible("Disponible"), En_repture_de_stock("En repture de stock");

    private String status;

    ProductStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
