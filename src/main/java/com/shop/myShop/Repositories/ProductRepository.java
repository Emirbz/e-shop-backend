package com.shop.myShop.Repositories;

import com.shop.myShop.Entities.Category;
import com.shop.myShop.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("select p from Product p join p.categories c where c.id = ?1 order by p.dateAdded Desc")
    Page<Product> getProductsByCategory(Long id, Pageable pageable, Specification<Product> specification);

    @Query("select count(p) from Product p join p.categories c where c.id = ?1 order by p.dateAdded Desc")
    int getNumbersOfProductsByCategory(Long id);

    @Query("select count(p) from Product p")
    int getNumbersOfProducts();
}
