package com.evosoft.demo.model.dto;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.evosoft.demo.model.MachineType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResourceDto {
    Long id;
    String lockedBy;
    LocalDateTime lockedSince;
    LocalDateTime lockedTill;

    @Valid @NotEmpty @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    String ip;

    @Valid @NotEmpty
    String name;

    @Valid @NotEmpty
    String os;
    
    String details;
    String comment;
    MachineType mType;
}
