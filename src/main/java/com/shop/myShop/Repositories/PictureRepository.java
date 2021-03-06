package com.shop.myShop.Repositories;

import com.shop.myShop.Entities.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    Optional<Picture> findByName(String name);

    @Query("select p from Picture p where p.product.id = ?1")
    List<Picture> getPicturesByProduct(Long id);

    @Query("select p from Picture p where p.category.id = ?1")
    Picture getPicturesByCategory(Long id);

    @Transactional
    @Modifying
    @Query("delete  from Picture p where p.product.id = ?1")
    void deletePicturesByProduct(Long id);
}
