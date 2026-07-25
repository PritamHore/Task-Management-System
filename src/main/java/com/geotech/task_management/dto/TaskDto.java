package com.geotech.task_management.dto;

import com.geotech.task_management.util.TaskStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class TaskDto {

    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private UUID dependsOn;

}
