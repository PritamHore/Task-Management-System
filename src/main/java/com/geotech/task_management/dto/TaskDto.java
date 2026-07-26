package com.geotech.task_management.dto;

import com.geotech.task_management.util.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class TaskDto {

    private UUID id;
    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;
    private String description;
    private TaskStatus status;
    private UUID dependsOn;

}
