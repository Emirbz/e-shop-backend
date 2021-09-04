package com.shop.myShop.Repositories;

import com.shop.myShop.Entities.Category;
import com.shop.myShop.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("select p from Product p where p.categories in ?1 order by p.dateAdded Desc")
    Page<Product> getProductsByCategory(List<Category> categories, Pageable pageable);
}
