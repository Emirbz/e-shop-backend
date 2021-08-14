package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.Sale;
import com.shop.myShop.Repositories.SaleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("sale")
public class SaleController {

    private SaleRepository saleRepository;

    public SaleController(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }


    @PostMapping
    ResponseEntity addSale(@RequestBody Sale sale) {
        return ResponseEntity.ok(saleRepository.save(sale));
    }


    @PutMapping("/{id}")
    ResponseEntity replaceSale(@RequestBody Sale newSale, @PathVariable Long id) {
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

        else
            return ResponseEntity.badRequest().body("Sale not found");
    }


}
