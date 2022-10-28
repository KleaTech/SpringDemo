package com.evosoft.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evosoft.demo.model.entity.ResourceEntity;

public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {
    
}
