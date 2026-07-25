package com.geotech.task_management.util;

import com.geotech.task_management.dto.TaskDto;
import com.geotech.task_management.entity.TaskEntity;
import com.geotech.task_management.exception.CircularDependencyException;
import com.geotech.task_management.exception.TaskNotFoundException;
import com.geotech.task_management.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskUtil {

    private final TaskRepository taskRepository;

    public void checkCircularDependency(TaskEntity currentTask, TaskDto newTask) {

        UUID parentId = newTask.getDependsOn();

        while (null != parentId) {

            if (parentId.equals(currentTask.getId())) {
                throw new CircularDependencyException("Circular dependency detected.");
            }

            TaskEntity parent = taskRepository.findById(parentId)
                    .orElseThrow(() -> new TaskNotFoundException("Task not found"));

            parentId = parent.getDependsOn() == null ? null : parent.getDependsOn().getId();
        }
    }

}
