package com.evosoft.demo.model.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;
import com.evosoft.demo.model.dto.UserDto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @ToString.Exclude
    @OneToMany(mappedBy = "lockingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResourceEntity> lockedResources = new HashSet<>();

    @Column(unique = true)
    @NonNull
    private String userName;

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) return true;
        if (!(o instanceof UserEntity)) return false;
        var that = (UserEntity)o;
        return Objects.equals(this.getUserName(), that.getUserName());
    }

    public UserDto toDto() {
        return UserDto.builder()
        .userName(getUserName())
        .lockedResources(lockedResources.stream()
            .map(ResourceEntity::toDto)
            .collect(Collectors.toSet()))
        .build();
    }

    public static UserEntity fromDto(UserDto dto) {
        return new UserEntity(dto.getUserName());
    }
}
