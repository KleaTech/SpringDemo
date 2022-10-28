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
import com.evosoft.demo.model.dto.ResourceDto;
import com.evosoft.demo.model.entity.ResourceEntity;
import com.evosoft.demo.persistence.ResourceRepository;

import lombok.var;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    
    @Autowired private ResourceRepository resourceRepository;

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
}
