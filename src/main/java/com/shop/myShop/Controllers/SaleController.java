package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.Product;
import com.shop.myShop.Entities.Sale;
import com.shop.myShop.Repositories.ProductRepository;
import com.shop.myShop.Repositories.SaleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("sale")
public class SaleController {

    private SaleRepository saleRepository;
    private ProductRepository productRepository;

    public SaleController(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }


    @PostMapping("/{id}")
    ResponseEntity addSale(@RequestBody Sale sale, @PathVariable Long id) {
        Map<String, String> error = new HashMap<>();
        Product p = productRepository.findById(id).orElse(null);
        if (p != null) {
            sale.setProduct(p);
            return ResponseEntity.ok(saleRepository.save(sale));
        } else {
            error.put("error", "Product not found");
            return ResponseEntity.badRequest().body(error);
        }
    }


    @PutMapping("/{id}")
    ResponseEntity replaceSale(@RequestBody Sale newSale, @PathVariable Long id) {
        Map<String, String> error = new HashMap<>();
        Sale s = saleRepository.findById(id)
                .map(product -> {
                    product.setEndDate(newSale.getEndDate());
                    product.setStartDate(newSale.getStartDate());
                    product.setPercentage(newSale.getPercentage());
                    product.setProduct(newSale.getProduct());
                    return saleRepository.save(product);
                }).orElse(null);
        if (s != null)
            return ResponseEntity.ok(s);
        else {
            error.put("error", "Sale not found");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping
    ResponseEntity getSales() {
        return ResponseEntity.ok(saleRepository.findAll());
    }


}
