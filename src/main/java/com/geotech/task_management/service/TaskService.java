package com.geotech.task_management.service;

import com.geotech.task_management.dto.GlobalResponse;
import com.geotech.task_management.dto.TaskDto;
import com.geotech.task_management.dto.TaskResponseDto;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    GlobalResponse create(TaskDto taskDto);

    List<TaskResponseDto> getAllTasks();

    TaskResponseDto getTask(UUID id);

    GlobalResponse editTask(TaskDto taskEditDto);

}
