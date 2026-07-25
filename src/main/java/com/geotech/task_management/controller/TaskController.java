package com.geotech.task_management.controller;

import com.geotech.task_management.dto.GlobalResponse;
import com.geotech.task_management.dto.TaskCreationDto;
import com.geotech.task_management.dto.TaskDto;
import com.geotech.task_management.entity.TaskEntity;
import com.geotech.task_management.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/*
@Author Pritam Hore
@Since v1
 */
@RestController
@RequestMapping("/api/v1/task-controller")
@Slf4j
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task which may or may not be dependent on another task.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Task created successfully",
                            content = @Content(schema = @Schema(implementation = GlobalResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input"
                    )
            }
    )
    @PostMapping()
    public ResponseEntity<GlobalResponse> create(@RequestBody TaskCreationDto taskCreationDto){
        log.info("Executing:TaskController, create()");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.create(taskCreationDto));
    }

    @Operation(
            summary = "Get Created",
            description = "Creates a new task which may or may not be dependent on another task.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Task created successfully",
                            content = @Content(schema = @Schema(implementation = GlobalResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input"
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<TaskDto> getTask(@RequestParam UUID id){
        log.info("Executing:TaskController, getTask() with ID: {}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskService.getTask(id));
    }

}
