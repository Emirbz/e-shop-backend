package com.shop.myShop.Repositories;

import com.shop.myShop.Entities.Category;
import com.shop.myShop.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

//    @Query("select p from Product p join  pc on p.id = pc.products_id.id where pc.categories_id in ?1 order by p.dateAdded Desc")
//    Page<Product> getProductsByCategory(Category categories, Pageable pageable);
}
