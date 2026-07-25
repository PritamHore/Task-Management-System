package com.geotech.task_management.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class GlobalResponse {
    private UUID id;
    private String message;
}
