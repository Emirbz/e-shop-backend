package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.*;
import com.shop.myShop.Repositories.*;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.JoinFetch;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SaleRepository saleRepository;
    private final SizeRepository sizeRepository;
    private final PictureRepository pictureRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository, SaleRepository saleRepository, SizeRepository sizeRepository, PictureRepository pictureRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.saleRepository = saleRepository;
        this.sizeRepository = sizeRepository;
        this.pictureRepository = pictureRepository;
    }


    @PostMapping
    public ResponseEntity addProduct(@RequestBody Product product) {
        Product dto = new Product();
        updateProduct(dto, product.getName(), product.getDescription(), product.getPrice(), product.getStatus(), product.getCollection(), product.getGender(), product.getCategories());
        product.getSizes().forEach(productSize -> {
            sizeRepository.findById(productSize.getSize().getId()).ifPresent(size -> dto.addSize(size, productSize.getQuantity()));
        });
        Product p = productRepository.save(dto);
        product.getPictures().forEach(picture ->
                {
                    Picture pic = new Picture();
                    pic.setName(picture.getName());
                    pic.setProduct(p);
                    pictureRepository.save(pic);
                }
        );
        return ResponseEntity.ok(p);
    }

    @GetMapping("/{id}")
    ResponseEntity getProduct(@PathVariable Long id) {
        Product p = productRepository.findById(id).orElse(null);
        if (p != null)
            return ResponseEntity.ok(p);
        else
            return ResponseEntity.badRequest().body("Product not found");
    }

    @PutMapping("/{id}")
    ResponseEntity replaceProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product p = productRepository.findById(id)
                .map(product -> {
                    updateProduct(product, newProduct.getName(), newProduct.getDescription(), newProduct.getPrice(),
                            newProduct.getStatus(), newProduct.getCollection(), newProduct.getGender(), newProduct.getCategories());
                    return productRepository.saveAndFlush(product);
                }).orElse(null);
        if (p != null) {
            p.getSizes().clear();
            newProduct.getSizes().forEach(productSize -> {
                sizeRepository.findById(productSize.getSize().getId()).ifPresent(size -> p.addSize(size, productSize.getQuantity()));
            });
            List<Picture> pictures = pictureRepository.getPicturesByProduct(p.getId());
            pictureRepository.deletePicturesByProduct(p.getId());
            pictures.forEach(picture -> {
                Picture pic = new Picture();
                pic.setName(picture.getName());
                pic.setProduct(p);
                pictureRepository.save(pic);
            });
            return ResponseEntity.ok(p);
        } else
            return ResponseEntity.badRequest().body("Product not found");
    }

    private void updateProduct(Product product, String name, String description, Double price, ProductStatus status, Collection collection, Gender gender, Set<Category> categories) {
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStatus(status);
        product.setCollection(collection);
        product.setGender(gender);
        product.getCategories().addAll(categories);
    }

    @DeleteMapping("/{id}")
    ResponseEntity deleteProduct(@PathVariable Long id) {
        Product p = productRepository.findById(id).orElse(null);
        if (p != null) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.badRequest().body("Product not found");
    }


    @GetMapping("/{id}/categories")
    ResponseEntity getProductsByCategory(@PathVariable Long id, @RequestBody List<Category> categories, Pageable pageable) {
        Category c = categoryRepository.findById(id).orElse(null);
        if (c != null) {
            Page<Product> p = productRepository.getProductsByCategory(categories, pageable);
            return ResponseEntity.ok(p);
        }
        return ResponseEntity.badRequest().body("Category not found");
    }

    @GetMapping
    ResponseEntity getAllProducts(
            @Join(path = "categories", alias = "cat")
            @JoinFetch(paths = "sizes", alias = "ps")
            @And({
                    @Spec(path = "name", spec = Like.class),
                    @Spec(path = "size", spec = Equal.class),
                    @Spec(path = "gender", spec = Equal.class),
                    @Spec(path = "collection", spec = Equal.class),
                    @Spec(path = "cat", spec = Equal.class),
                    @Spec(path = "ps", params = "size", spec = In.class),
                    @Spec(path = "price", params = {"minPrice", "maxPrice"}, spec = Between.class)
            }) Specification<Product> productSpecification, Pageable pageable) {
        Page<Product> p = productRepository.findAll(productSpecification, pageable);
      /*  for (Product product : p) {
            Integer percentage = saleRepository.isProductOnSale(new Date(), product.getId());
            if (percentage != null) {
                double newPrice = product.getPrice() - (product.getPrice() * percentage / 100);
                BigDecimal bd = new BigDecimal(newPrice).setScale(2, RoundingMode.HALF_UP);
                product.setNewPrice(bd.doubleValue());
            }
        }*/
        return ResponseEntity.ok(p);
    }
}
