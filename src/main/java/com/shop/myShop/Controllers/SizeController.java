package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.Size;
import com.shop.myShop.Repositories.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("sizes")
public class SizeController {

    @Autowired
    SizeRepository sizeRepository;

    @PostMapping
    public ResponseEntity<Size> addSize(@RequestBody Size size) {
        return ResponseEntity.ok(sizeRepository.save(size));
    }

    @GetMapping
    public ResponseEntity<List<Size>> getSizes() {
        return ResponseEntity.ok(sizeRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity replaceSize(@RequestBody Size newSize, @PathVariable Long id) {
        Size size = sizeRepository.findById(id)
                .map(s -> {
                    s.setName(newSize.getName());
                    return sizeRepository.save(s);
                }).orElse(null);
        if (size != null) {
            return ResponseEntity.ok(size);
        } else
            return ResponseEntity.badRequest().body("Size not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSize(@PathVariable Long id) {
        Size p = sizeRepository.findById(id).orElse(null);
        if (p != null) {
            sizeRepository.deleteById(id);
            return ResponseEntity.ok("Size deleted");
        } else
            return ResponseEntity.badRequest().body("Size not found");
    }

}
