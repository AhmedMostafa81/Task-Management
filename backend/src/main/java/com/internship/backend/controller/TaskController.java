package com.internship.backend.controller;

import com.internship.backend.dto.TaskRequestDTO;
import com.internship.backend.dto.TaskResponseDTO;
import com.internship.backend.entity.User;
import com.internship.backend.enums.Priority;
import com.internship.backend.enums.Status;
import com.internship.backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDTO createTask(
            @Valid @RequestBody TaskRequestDTO taskRequestDTO,
            @AuthenticationPrincipal User user) {
        log.info("Creating new task for user: {} - Title: {}", user.getUsername(), taskRequestDTO.getTitle());
        long startTime = System.currentTimeMillis();
        try {
            TaskResponseDTO response = taskService.createTask(taskRequestDTO, user);
            long duration = System.currentTimeMillis() - startTime;
            log.info("Task created successfully for user: {} - Task ID: {} - Duration: {}ms", 
                    user.getUsername(), response.getId(), duration);
            return response;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Error creating task for user: {} - Duration: {}ms", 
                    user.getUsername(), duration, e);
            throw e;
        }
    }

    @GetMapping
    public List<TaskResponseDTO> getTasks(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @AuthenticationPrincipal User user) {
        log.info("Fetching tasks for user: {} - Status filter: {}, Priority filter: {}", 
                user.getUsername(), status, priority);
        long startTime = System.currentTimeMillis();
        try {
            List<TaskResponseDTO> tasks = taskService.getTasks(status, priority, user);
            long duration = System.currentTimeMillis() - startTime;
            log.info("Tasks fetched successfully for user: {} - Count: {} - Duration: {}ms", 
                    user.getUsername(), tasks.size(), duration);
            return tasks;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Error fetching tasks for user: {} - Duration: {}ms", 
                    user.getUsername(), duration, e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(
            @PathVariable Long id, 
            @AuthenticationPrincipal User user) {
        log.info("Fetching task ID: {} for user: {}", id, user.getUsername());
        long startTime = System.currentTimeMillis();
        try {
            TaskResponseDTO task = taskService.getTaskById(id, user);
            long duration = System.currentTimeMillis() - startTime;
            log.info("Task ID: {} fetched successfully for user: {} - Duration: {}ms", 
                    id, user.getUsername(), duration);
            return task;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Error fetching task ID: {} for user: {} - Duration: {}ms", 
                    id, user.getUsername(), duration, e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO taskRequestDTO,
            @AuthenticationPrincipal User user) {
        log.info("Updating task ID: {} for user: {} - New title: {}", 
                id, user.getUsername(), taskRequestDTO.getTitle());
        long startTime = System.currentTimeMillis();
        try {
            TaskResponseDTO response = taskService.updateTask(id, taskRequestDTO, user);
            long duration = System.currentTimeMillis() - startTime;
            log.info("Task ID: {} updated successfully for user: {} - Duration: {}ms", 
                    id, user.getUsername(), duration);
            return response;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Error updating task ID: {} for user: {} - Duration: {}ms", 
                    id, user.getUsername(), duration, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
            @PathVariable Long id, 
            @AuthenticationPrincipal User user) {
        log.info("Deleting task ID: {} for user: {}", id, user.getUsername());
        long startTime = System.currentTimeMillis();
        try {
            taskService.deleteTask(id, user);
            long duration = System.currentTimeMillis() - startTime;
            log.info("Task ID: {} deleted successfully for user: {} - Duration: {}ms", 
                    id, user.getUsername(), duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Error deleting task ID: {} for user: {} - Duration: {}ms", 
                    id, user.getUsername(), duration, e);
            throw e;
        }
    }
}