package com.internship.backend.controller;

import com.internship.backend.dto.TaskRequestDTO;
import com.internship.backend.dto.TaskResponseDTO;
import com.internship.backend.entity.User;
import com.internship.backend.enums.Priority;
import com.internship.backend.enums.Status;
import com.internship.backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDTO createTask(
            @Valid @RequestBody TaskRequestDTO taskRequestDTO,
            @AuthenticationPrincipal User user) { // MAGIC: Gets the full User object!
        return taskService.createTask(taskRequestDTO, user);
    }

    @GetMapping
    public List<TaskResponseDTO> getTasks(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @AuthenticationPrincipal User user) {
        return taskService.getTasks(status, priority, user);
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return taskService.getTaskById(id, user);
    }

    @PutMapping("/{id}")
    public TaskResponseDTO updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO taskRequestDTO,
            @AuthenticationPrincipal User user) {
        return taskService.updateTask(id, taskRequestDTO, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id, @AuthenticationPrincipal User user) {
        taskService.deleteTask(id, user);
    }
}