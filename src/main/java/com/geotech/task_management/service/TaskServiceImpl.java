package com.geotech.task_management.service;

import com.geotech.task_management.dto.GlobalResponse;
import com.geotech.task_management.dto.TaskDto;
import com.geotech.task_management.dto.TaskResponseDto;
import com.geotech.task_management.entity.TaskEntity;
import com.geotech.task_management.exception.TaskAlreadyCompletedException;
import com.geotech.task_management.exception.TaskNotFoundException;
import com.geotech.task_management.mapper.TaskMapper;
import com.geotech.task_management.repository.TaskRepository;
import com.geotech.task_management.util.TaskStatus;
import com.geotech.task_management.util.TaskUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
            if(TaskStatus.DONE == dependsOn.getStatus()){
                throw new TaskAlreadyCompletedException("Dependent task is already completed.");
            }
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
    public List<TaskResponseDto> getAllTasks() {
        log.info("TaskServiceImpl || getAllTasks()");
        List<TaskEntity> allTasks = taskRepository.findAll();
        log.info("TaskServiceImpl || getAllTasks() || Fetched {} records.", allTasks.size());
        return taskMapper.convertToDtoList(allTasks);
    }

    @Override
    public TaskResponseDto getTask(UUID id) {
        log.info("TaskServiceImpl || getTask() || ID: {}",id);

        TaskEntity task = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task Not Found.")
        );

        TaskResponseDto response = taskMapper.convertToDto(task);
        if(task.hasDependency()){
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
        if(TaskStatus.DONE == task.getStatus()){
            throw new TaskAlreadyCompletedException("Task is already completed.");
        }

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

    @Override
    public GlobalResponse updateTaskStatus(UUID taskId) {
        log.info("TaskServiceImpl || updateTaskStatus() || Updating Task, Id: {}", taskId);

        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
                () -> new TaskNotFoundException("Task Not Found.")
        );

        taskUtil.changeTaskStatus(task);
        log.info("TaskServiceImpl || updateTaskStatus() || Task has been updated, Id: {}", taskId);

        return GlobalResponse.builder()
                .id(task.getId())
                .message("Status has been updated.")
                .build();
    }
}
