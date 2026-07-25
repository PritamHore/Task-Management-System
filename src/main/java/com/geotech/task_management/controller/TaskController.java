package com.geotech.task_management.controller;

import com.geotech.task_management.dto.GlobalResponse;
import com.geotech.task_management.dto.TaskDto;
import com.geotech.task_management.dto.TaskResponseDto;
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

import java.util.List;
import java.util.UUID;

/*
@Author Pritam Hore
@Since v1
 */
@RestController
@RequestMapping("/api/v1/task")
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
                            description = "Task created successfully.",
                            content = @Content(schema = @Schema(implementation = GlobalResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input"
                    )
            }
    )
    @PostMapping()
    public ResponseEntity<GlobalResponse> create(@RequestBody TaskDto taskDto){
        log.info("Executing:TaskController, create()");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.create(taskDto));
    }

    @Operation(
            summary = "Get All Created Task",
            description = "Get All Created Task with Dependent Task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "JSON Response of TaskResponseDto.",
                            content = @Content(schema = @Schema(implementation = TaskResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input"
                    )
            }
    )
    @GetMapping("/get-all")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(){
        log.info("Executing:TaskController, getAllTasks()");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskService.getAllTasks());
    }

    @Operation(
            summary = "Get Created Task",
            description = "Get Created Task with Dependent Task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "JSON Response of TaskResponseDto.",
                            content = @Content(schema = @Schema(implementation = TaskResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input"
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<TaskResponseDto> getTask(@RequestParam UUID id){
        log.info("Executing:TaskController, getTask() with ID: {}", id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskService.getTask(id));
    }

    @Operation(
            summary = "Edit Task",
            description = "Edit Task and Handle Circular dependencies",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task Edited Successfully.",
                            content = @Content(schema = @Schema(implementation = GlobalResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input"
                    )
            }
    )
    @PutMapping()
    public ResponseEntity<GlobalResponse> editTask(@RequestBody TaskDto taskEditDto){
        log.info("Executing:TaskController, editTask() with ID: {}", taskEditDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskService.editTask(taskEditDto));
    }

}
