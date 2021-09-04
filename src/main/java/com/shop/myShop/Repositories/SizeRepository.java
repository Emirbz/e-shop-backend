package com.shop.myShop.Repositories;

import com.shop.myShop.Entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

//    @Query("select p from Size p where p.product.id = ?1")
//    List<Size> getSizesByProduct(Long id);
}
