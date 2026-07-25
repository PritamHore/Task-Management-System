package com.geotech.task_management.dto;

import com.geotech.task_management.util.TaskStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class TaskResponseDto {

    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskResponseDto dependsOn;

}
