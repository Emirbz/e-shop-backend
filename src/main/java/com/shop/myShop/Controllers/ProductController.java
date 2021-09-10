package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.Collection;
import com.shop.myShop.Entities.*;
import com.shop.myShop.Repositories.*;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.JoinFetch;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("products")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SaleRepository saleRepository;
    private final SizeRepository sizeRepository;
    private final ProductSizeRepository productSizeRepository;
    private final PictureRepository pictureRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository, SaleRepository saleRepository, SizeRepository sizeRepository, ProductSizeRepository productSizeRepository, PictureRepository pictureRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.saleRepository = saleRepository;
        this.sizeRepository = sizeRepository;
        this.productSizeRepository = productSizeRepository;
        this.pictureRepository = pictureRepository;
    }


    @PostMapping
    public ResponseEntity addProduct(@RequestBody Product product) {
        Product dto = new Product();
        updateProduct(dto, product.getName(), product.getDescription(), product.getPrice(), product.getStatus(), product.getCollection(), product.getGender(), product.getCategories(), product.isDrop());
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
        Map<String, String> error = new HashMap<>();
        Product p = productRepository.findById(id).orElse(null);
        if (p != null)
            return ResponseEntity.ok(p);
        else {
            error.put("error", "Product not found");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    ResponseEntity replaceProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Map<String, String> error = new HashMap<>();
        Product p = productRepository.findById(id)
                .map(product -> {
                    product.getCategories().clear();
                    updateProduct(product, newProduct.getName(), newProduct.getDescription(), newProduct.getPrice(),
                            newProduct.getStatus(), newProduct.getCollection(), newProduct.getGender(), newProduct.getCategories(), newProduct.isDrop());
                    return productRepository.saveAndFlush(product);
                }).orElse(null);
        if (p != null) {
            List<ProductSize> productSizes = productSizeRepository.getProductSizesByProduct(id);
            productSizeRepository.deleteAll(productSizes);
            newProduct.getSizes().forEach(productSize -> {
                sizeRepository.findById(productSize.getSize().getId()).ifPresent(size -> p.addSize(size, productSize.getQuantity()));
            });
            pictureRepository.deletePicturesByProduct(p.getId());
            newProduct.getPictures().forEach(picture -> {
                Picture pic = new Picture();
                pic.setName(picture.getName());
                pic.setProduct(p);
                pictureRepository.save(pic);
            });
            return ResponseEntity.ok(p);
        } else {
            error.put("error", "Product not found");
            return ResponseEntity.badRequest().body(error);
        }
    }

    private void updateProduct(Product product, String name, String description, Double price, String status, Collection collection, Gender gender, Set<Category> categories, boolean isDrop) {
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStatus(status);
        product.setCollection(collection);
        product.setGender(gender);
        product.getCategories().addAll(categories);
        product.setDrop(isDrop);
    }

    @DeleteMapping("/{id}")
    ResponseEntity deleteProduct(@PathVariable Long id) {
        Map<String, String> error = new HashMap<>();
        Product p = productRepository.findById(id).orElse(null);
        if (p != null) {
            productRepository.deleteById(id);
            return ResponseEntity.ok(p);
        } else {
            error.put("error", "Product not found");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}/categories")
    ResponseEntity getProductsByCategory(@PathVariable Long id, @RequestBody List<Category> categories, Pageable pageable) {
        Map<String, String> error = new HashMap<>();
        Category c = categoryRepository.findById(id).orElse(null);
        if (c != null) {
            Page<Product> p = productRepository.getProductsByCategory(categories, pageable);
            return ResponseEntity.ok(p);
        }
        {
            error.put("error", "Category not found");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping(params = "cate")
    ResponseEntity getAllProducts(
            @Join(path = "categories", alias = "c")
            @Join(path = "sizes", alias = "s")
            @And({
                    @Spec(path = "name", spec = LikeIgnoreCase.class),
                    @Spec(path = "size", spec = Equal.class),
                    @Spec(path = "gender", spec = Equal.class),
                    @Spec(path = "collection", spec = Equal.class),
                    @Spec(path = "c.id", params = "cate", paramSeparator = ',', spec = In.class),
                    @Spec(path = "s", paramSeparator = ',', spec = In.class),
                    @Spec(path = "price", params = {"minPrice", "maxPrice"}, spec = Between.class)
            }) Specification<Product> productSpecification, Pageable pageable) {
        Page<Product> p = productRepository.findAll(productSpecification, pageable);
        for (Product product : p) {
            Sale sale = saleRepository.isProductOnSale(new Date(), product.getId());
            if (sale != null) {
                product.setSale(sale);
            }
        }
        return ResponseEntity.ok(p);
    }
}
