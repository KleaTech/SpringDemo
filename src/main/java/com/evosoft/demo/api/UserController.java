package com.evosoft.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.evosoft.demo.model.dto.UserDto;
import com.evosoft.demo.persistence.UserRepository;
import lombok.var;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired UserRepository userRepository;

    @GetMapping("/{userName}")
    public ResponseEntity<UserDto> getUserDetails(@PathVariable String userName) {
        var userFound = userRepository.findByUserName(userName);
        if (!userFound.isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(userFound.get().toDto());
    }
}
