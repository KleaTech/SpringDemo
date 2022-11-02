package com.evosoft.demo.api;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.evosoft.demo.model.HistoryAction;
import com.evosoft.demo.model.dto.ResourceDto;
import com.evosoft.demo.model.dto.UserDto;
import com.evosoft.demo.model.entity.HistoryEntity;
import com.evosoft.demo.model.entity.ResourceEntity;
import com.evosoft.demo.model.entity.UserEntity;
import com.evosoft.demo.persistence.HistoryRepository;
import com.evosoft.demo.persistence.ResourceRepository;
import com.evosoft.demo.persistence.UserRepository;

import lombok.var;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    
    @Autowired ResourceRepository resourceRepository;
    @Autowired UserRepository userRepository;
    @Autowired HistoryRepository historyRepository;

    @GetMapping
    public ResponseEntity<List<ResourceDto>> getResources(@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        var resources = resourceRepository.findAll(pageable).map(ResourceEntity::toDto);
        return ResponseEntity.status(resources.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
            .body(resources.getContent());
    }

    @PutMapping
    public ResponseEntity<Void> putResource(@Valid @RequestBody ResourceDto resource) {
        var similarResourceExists = resourceRepository.findOne(Example.of(
            ResourceEntity.builder()
                .ip(resource.getIp())
                .name(resource.getName())
                .build()
        )).isPresent();
        if (similarResourceExists) return ResponseEntity.badRequest().build();
        resourceRepository.save(ResourceEntity.fromDto(resource));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateResource(@Valid @RequestBody ResourceDto resource) {
        var resourceToUpdate = resourceRepository.findById(resource.getId());
        if (!resourceToUpdate.isPresent()) return ResponseEntity.notFound().build();
        resourceRepository.save(ResourceEntity.fromDto(resource));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{resourceId}/lock")
    public ResponseEntity<String> lockResource(@PathVariable Long resourceId, @RequestParam String userName) {
        var resourceToUpdate = resourceRepository.findById(resourceId);
        if (!resourceToUpdate.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found.");
        var resource = resourceToUpdate.get();
        if (resource.getLockingUser() != null) return ResponseEntity.badRequest().body("Resource is already locked.");
        var userToLock = userRepository.findByUserName(userName);
        UserEntity user;
        if (userToLock.isPresent()) {
            user = userToLock.get();
        }
        else {
            var newUser = UserDto.builder().userName(userName).build();
            user = userRepository.save(UserEntity.fromDto(newUser));
        }
        resource.setLockingUser(user);
        resourceRepository.save(resource);

        HistoryEntity historyEntry = HistoryEntity.builder()
            .action(HistoryAction.LOCK)
            .resource(resource)
            .user(user)
            .build();
        historyRepository.save(historyEntry);
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{resourceId}/release")
    public ResponseEntity<String> releaseResource(@PathVariable Long resourceId) {
        var resourceToUpdate = resourceRepository.findById(resourceId);
        if (!resourceToUpdate.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found.");
        var resource = resourceToUpdate.get();
        var user = resource.getLockingUser();
        if (user == null) return ResponseEntity.badRequest().body("Resource is not locked.");
        resource.setLockingUser(null);
        resourceRepository.save(resource);

        HistoryEntity historyEntry = HistoryEntity.builder()
            .action(HistoryAction.RELEASE)
            .resource(resource)
            .user(user)
            .build();
        historyRepository.save(historyEntry);

        return ResponseEntity.ok().build();
    }
}
