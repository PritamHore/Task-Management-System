package com.geotech.task_management.service;

import com.geotech.task_management.dto.GlobalResponse;
import com.geotech.task_management.dto.TaskCreationDto;
import com.geotech.task_management.dto.TaskDto;

import java.util.UUID;

public interface TaskService {
    GlobalResponse create(TaskCreationDto taskCreationDto);

    TaskDto getTask(UUID id);
}
