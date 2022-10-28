package com.evosoft.demo.model.entity;

import javax.persistence.*;
import lombok.*;
import com.evosoft.demo.model.dto.HistoryDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "reservation_history")
public class HistoryEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private ResourceEntity resource;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    public HistoryDto toDto() {
        return HistoryDto.builder()
            .userName(user.getUserName())
            .resource(resource.toDto())
            .build();
    }
}
