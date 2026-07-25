package com.geotech.task_management.mapper;

import com.geotech.task_management.dto.TaskCreationDto;
import com.geotech.task_management.dto.TaskDto;
import com.geotech.task_management.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "dependsOn", ignore = true)
    TaskEntity convertToEntity(TaskCreationDto taskCreationDto);

    @Mapping(target = "dependsOn", ignore = true)
    TaskDto convertToDto(TaskEntity taskEntity);


}
