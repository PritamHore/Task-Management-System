package com.geotech.task_management.service;

import com.geotech.task_management.dto.GlobalResponse;
import com.geotech.task_management.dto.TaskCreationDto;
import com.geotech.task_management.dto.TaskDto;
import com.geotech.task_management.entity.TaskEntity;
import com.geotech.task_management.exception.TaskNotFoundException;
import com.geotech.task_management.mapper.TaskMapper;
import com.geotech.task_management.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public GlobalResponse create(TaskCreationDto taskCreationDto) {
        log.info("TaskServiceImpl || create()");

        TaskEntity entity = taskMapper.convertToEntity(taskCreationDto);
        if(null != taskCreationDto.getDependsOn()){
            TaskEntity dependsOn = taskRepository.findById(taskCreationDto.getDependsOn()).orElseThrow(
                    () -> new TaskNotFoundException("Parent Task Not Found.")
            );
            entity.setDependsOn(dependsOn);
        }
        entity = taskRepository.save(entity);
        log.info("Task Created Successfully!");

        return GlobalResponse.builder()
                .id(entity.getId())
                .message("Task Created Successfully!")
                .build();
    }

    @Override
    public TaskDto getTask(UUID id) {
        log.info("TaskServiceImpl || getTask() || ID: {}",id);

        TaskEntity task = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task Not Found.")
        );

        TaskDto response = taskMapper.convertToDto(task);
        if(null != task.getDependsOn()){
            TaskDto dependsOn = taskMapper.convertToDto(task.getDependsOn());
            response.setDependsOn(dependsOn);
        }

        log.info("TaskServiceImpl || getTask() || Successfully Fetched: {} ", response);
        
        return response;
    }
}
