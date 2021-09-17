package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.Size;
import com.shop.myShop.Repositories.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("sizes")
public class SizeController {

    @Autowired
    SizeRepository sizeRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Size> addSize(@RequestBody Size size) {
        return ResponseEntity.ok(sizeRepository.save(size));
    }

    @GetMapping
    public ResponseEntity<List<Size>> getSizes() {
        return ResponseEntity.ok(sizeRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity replaceSize(@RequestBody Size newSize, @PathVariable Long id) {
        Map<String, String> error = new HashMap<>();
        Size size = sizeRepository.findById(id)
                .map(s -> {
                    s.setName(newSize.getName());
                    return sizeRepository.save(s);
                }).orElse(null);
        if (size != null) {
            return ResponseEntity.ok(size);
        } else {
            error.put("error", "Size not found");
            return ResponseEntity.badRequest().body(error);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteSize(@PathVariable Long id) {
        Map<String, String> error = new HashMap<>();
        Size p = sizeRepository.findById(id).orElse(null);
        if (p != null) {
            sizeRepository.deleteById(id);
            return ResponseEntity.ok(p);
        }  else {
            error.put("error", "Size not found");
            return ResponseEntity.badRequest().body(error);
        }
    }

}
