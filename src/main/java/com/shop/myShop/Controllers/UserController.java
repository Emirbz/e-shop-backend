package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.User;
import com.shop.myShop.Repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RequestMapping("users")
public class UserController {

    private final UserRepository userRepository;
    public BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/{phone}")
    ResponseEntity getOrdersByPhoneNumber(@PathVariable String phone) {
        Map<String, String> error = new HashMap<>();

        User user = userRepository.getUserByPhone(phone);
        if (user != null){

            return ResponseEntity.ok(user);
        }

        error.put("error", "user not found");
        return ResponseEntity.badRequest().body(error);


    }
/*
    @PostMapping("login")
    ResponseEntity login(@RequestBody User user) {
        User u = userRepository.findUserByEmail(user.getEmail());
        if (u != null)
            if (encoder.matches(user.getPassword(), u.getPassword()))
                return ResponseEntity.ok(u);
            else
                return ResponseEntity.badRequest().body("Wrong credentials");

        return ResponseEntity.badRequest().body("User not found");
    }

    @PostMapping("signup")
    ResponseEntity signup(@RequestBody User user) {
        if (userRepository.findUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("User already exits");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return ResponseEntity.ok(userRepository.save(user));
    }

    @GetMapping("/user/{id}")
    ResponseEntity getUser(@PathVariable Long id) {
        User u = userRepository.findById(id).orElse(null);
        if (u != null)
            return ResponseEntity.ok(u);
        else
            return ResponseEntity.badRequest().body("User not found");
    }

    @PutMapping("/user/{id}")
    ResponseEntity replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        if (userRepository.findUserByEmail(newUser.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        User u = userRepository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setAddress(newUser.getAddress());
                    user.setEmail(newUser.getEmail());
                    user.setLastName(newUser.getLastName());
                    user.setName(newUser.getName());
                    user.setPhone(newUser.getPhone());
                    user.setPassword(newUser.getPassword());
                    user.setGender(newUser.getGender());
                    return userRepository.save(user);
                }).orElse(null);
        if (u != null)
            return ResponseEntity.ok(u);

        else
            return ResponseEntity.badRequest().body("User not found");
    }*/
}
