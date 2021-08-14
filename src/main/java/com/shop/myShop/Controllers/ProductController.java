package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.Category;
import com.shop.myShop.Entities.Product;
import com.shop.myShop.Entities.Size;
import com.shop.myShop.Repositories.CategoryRepository;
import com.shop.myShop.Repositories.ProductRepository;
import com.shop.myShop.Repositories.SaleRepository;
import com.shop.myShop.Repositories.SizeRepository;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@RestController
@CrossOrigin(origins = "http://localhost:4200",maxAge = 3600)
@RequestMapping("products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SaleRepository saleRepository;
    private final SizeRepository sizeRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository, SaleRepository saleRepository, SizeRepository sizeRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.saleRepository = saleRepository;
        this.sizeRepository = sizeRepository;
    }

    @PostMapping
    ResponseEntity addProduct(@RequestBody Product product) {
        Product p = productRepository.save(product);
        for (Size size : product.getSizes()) {
            size.setProduct(p);
            sizeRepository.save(size);
        }
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
                    product.setName(newProduct.getName());
                    product.setCategory(newProduct.getCategory());
                    product.setCollection(newProduct.getCollection());
                    product.setColor(newProduct.getColor());
                    product.setDescription(newProduct.getDescription());
                    product.setGender(newProduct.getGender());
                    product.setPrice(newProduct.getPrice());
                    product.setSizes(newProduct.getSizes());
                    product.setStatus(newProduct.getStatus());
                    return productRepository.save(product);
                }).orElse(null);
        if (p != null)
            return ResponseEntity.ok(p);

        else
            return ResponseEntity.badRequest().body("Product not found");
    }

    @DeleteMapping("/{id}")
    ResponseEntity deleteProduct(@PathVariable Long id) {
        Product p = productRepository.findById(id).orElse(null);
        if (p != null) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else
            return ResponseEntity.badRequest().body("User not found");
    }


    @GetMapping("/category/{id}")
    ResponseEntity getProductsByCategory(@PathVariable Long id, Pageable pageable) {
        Category c = categoryRepository.findById(id).orElse(null);
        if (c != null) {
            Page<Product> p = productRepository.getProductsByCategory(id, pageable);
            return ResponseEntity.ok(p);
        }
        return ResponseEntity.badRequest().body("Category not found");

    }

    @GetMapping
    ResponseEntity getAllProducts(@And({
            @Spec(path = "name", spec = Like.class),
            @Spec(path = "color", spec = Equal.class),
            @Spec(path = "size", spec = Equal.class),
            @Spec(path = "gender", spec = Equal.class),
            @Spec(path = "collection", spec = Equal.class),
            @Spec(path = "category.id", spec = Equal.class),
            @Spec(path = "price", params = {"minPrice", "maxPrice"}, spec = Between.class),
    }) Specification<Product> productSpecification, Pageable pageable) {
        Page<Product> p = productRepository.findAll(productSpecification, pageable);
        for (Product product : p) {
            Integer percentage = saleRepository.isProductOnSale(new Date(), product.getId());
            if (percentage != null) {
                double newPrice = product.getPrice() - (product.getPrice() * percentage / 100);
                BigDecimal bd = new BigDecimal(newPrice).setScale(2, RoundingMode.HALF_UP);
                product.setNewPrice(bd.doubleValue());
            }
        }
        return ResponseEntity.ok(p);
    }


}
