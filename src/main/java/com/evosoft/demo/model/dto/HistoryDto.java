package com.evosoft.demo.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoryDto {
    ResourceDto resource;
    String userName;
}
