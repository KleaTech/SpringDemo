package com.evosoft.demo.model.dto;

import com.evosoft.demo.model.HistoryAction;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoryDto {
    ResourceDto resource;
    String userName;
    HistoryAction action;
}
