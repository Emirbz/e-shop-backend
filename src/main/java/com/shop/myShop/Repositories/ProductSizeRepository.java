package com.shop.myShop.Repositories;

import com.shop.myShop.Entities.Product;
import com.shop.myShop.Entities.ProductSize;
import com.shop.myShop.Entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {


    @Query("select ps from ProductSize ps where  ps.product.id = ?1")
    List<ProductSize> getProductSizesByProduct(Long id);

//    @Query("select p from Size p where p.product.id = ?1")
//    List<Size> getSizesByProduct(Long id);
}
