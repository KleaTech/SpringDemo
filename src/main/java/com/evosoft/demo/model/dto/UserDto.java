package com.evosoft.demo.model.dto;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    @Valid @NotEmpty
    String userName;
    Set<ResourceDto> lockedResources;
}
