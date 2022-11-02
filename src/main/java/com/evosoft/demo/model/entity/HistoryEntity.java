package com.evosoft.demo.model.entity;

import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import com.evosoft.demo.model.HistoryAction;
import com.evosoft.demo.model.dto.HistoryDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "reservation_history")
public class HistoryEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private ResourceEntity resource;

    private HistoryAction action;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    public HistoryDto toDto() {
        return HistoryDto.builder()
            .userName(user.getUserName())
            .resource(resource.toDto())
            .action(action)
            .build();
    }
}
