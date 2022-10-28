package com.evosoft.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evosoft.demo.model.entity.HistoryEntity;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {
    
}
