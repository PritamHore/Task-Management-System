package com.geotech.task_management.mapper;

import com.geotech.task_management.dto.TaskDto;
import com.geotech.task_management.dto.TaskResponseDto;
import com.geotech.task_management.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "dependsOn", ignore = true)
    TaskEntity convertToEntity(TaskDto taskCreationDto);

    @Mapping(target = "dependsOn", ignore = true)
    TaskResponseDto convertToDto(TaskEntity taskEntity);

    @Mapping(target = "dependsOn", ignore = true)
    void updateEntity(TaskDto taskDto, @MappingTarget TaskEntity taskEntity);

    @Mapping(target = "dependsOn", ignore = true)
    List<TaskResponseDto> convertToEntityList(List<TaskEntity> allTasks);
}
