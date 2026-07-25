package com.geotech.task_management.service;

import com.geotech.task_management.dto.GlobalResponse;
import com.geotech.task_management.dto.TaskDto;
import com.geotech.task_management.dto.TaskResponseDto;
import com.geotech.task_management.entity.TaskEntity;
import com.geotech.task_management.exception.TaskNotFoundException;
import com.geotech.task_management.mapper.TaskMapper;
import com.geotech.task_management.repository.TaskRepository;
import com.geotech.task_management.util.TaskUtil;
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
    private final TaskUtil taskUtil;

    @Override
    public GlobalResponse create(TaskDto taskCreationDto) {
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
    public TaskResponseDto getTask(UUID id) {
        log.info("TaskServiceImpl || getTask() || ID: {}",id);

        TaskEntity task = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task Not Found.")
        );

        TaskResponseDto response = taskMapper.convertToDto(task);
        if(null != task.getDependsOn()){
            TaskResponseDto dependsOn = taskMapper.convertToDto(task.getDependsOn());
            response.setDependsOn(dependsOn);
        }

        log.info("TaskServiceImpl || getTask() || Successfully Fetched: {} ", response);

        return response;
    }

    @Override
    public GlobalResponse editTask(TaskDto taskEditDto) {
        log.info("TaskServiceImpl || editTask() || Updatable: {}",taskEditDto);

        TaskEntity task = taskRepository.findById(taskEditDto.getId()).orElseThrow(
                () -> new TaskNotFoundException("Task Not Found.")
        );

        taskUtil.checkCircularDependency(task,taskEditDto);

        taskMapper.updateEntity(taskEditDto, task);
        if(null != taskEditDto.getDependsOn()){
            TaskEntity dependsOn = taskRepository.findById(taskEditDto.getDependsOn()).orElseThrow(
                    () -> new TaskNotFoundException("Parent Not Found.")
            );
            task.setDependsOn(dependsOn);
        }else{
            task.setDependsOn(null);
        }
        task = taskRepository.save(task);

        log.info("Task Edited Successfully!");
        return GlobalResponse.builder()
                .id(task.getId())
                .message("Task Edited Successfully.")
                .build();
    }
}
