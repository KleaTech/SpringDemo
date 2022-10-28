package com.evosoft.demo.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.*;
import com.evosoft.demo.model.MachineType;
import com.evosoft.demo.model.dto.ResourceDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Entity
@Table(name = "resources")
public class ResourceEntity extends BaseEntity {
    @ToString.Exclude
    @JoinColumn(name = "locking_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity lockingUser;

    private LocalDateTime lockedSince;
    private LocalDateTime lockedTill;
    private String ip;    
    private String name;    
    private String os;
    private String details;
    private String comment;

    @Enumerated(EnumType.STRING)
    private MachineType mType;

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) return true;
        if (!(o instanceof ResourceEntity)) return false;
        var that = (ResourceEntity)o;
        return Objects.equals(this.getIp(), that.getIp())
            && Objects.equals(this.getName(), that.getName());
    }

    public ResourceDto toDto() {
        return ResourceDto.builder()
            .id(getId())
            .lockedBy(getLockingUser() == null ? null: getLockingUser().getUserName())
            .lockedSince(getLockedSince())
            .lockedTill(getLockedTill())
            .ip(getIp())
            .name(getName())
            .os(getOs())
            .details(getDetails())
            .comment(getComment())
            .mType(getMType())
            .build();
    }

    public static ResourceEntity fromDto(ResourceDto dto) {
        return ResourceEntity.builder()
        .id(dto.getId())
        .ip(dto.getIp())
        .name(dto.getName())
        .os(dto.getOs())
        .details(dto.getDetails())
        .comment(dto.getComment())
        .mType(dto.getMType())
        .build();
    }
}
