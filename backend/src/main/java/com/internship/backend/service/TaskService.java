package com.internship.backend.service;
import com.internship.backend.dto.TaskRequestDTO;
import com.internship.backend.dto.TaskResponseDTO;
import com.internship.backend.entity.Task;
import com.internship.backend.entity.User;
import com.internship.backend.enums.Priority;
import com.internship.backend.enums.Status;
import com.internship.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskResponseDTO createTask(TaskRequestDTO requestDTO, User user) {
        log.debug("Creating new task for user: {} - Title: '{}', Priority: {}, Status: {}", 
                user.getUsername(), requestDTO.getTitle(), requestDTO.getPriority(), requestDTO.getStatus());
        
        try {
            Task task = Task.builder()
                    .title(requestDTO.getTitle())
                    .description(requestDTO.getDescription())
                    .status(requestDTO.getStatus())
                    .priority(requestDTO.getPriority())
                    .user(user)
                    .build();

            log.debug("Built task object - preparing to save for user: {}", user.getUsername());
            Task savedTask = taskRepository.save(task);
            log.info("Task created and saved successfully - ID: {}, User: {}", savedTask.getId(), user.getUsername());
            
            TaskResponseDTO response = mapToResponseDTO(savedTask);
            log.debug("Task response DTO created for task ID: {}", savedTask.getId());
            return response;
        } catch (Exception e) {
            log.error("Error creating task for user: {} - Title: '{}' - Error: {}", 
                    user.getUsername(), requestDTO.getTitle(), e.getMessage(), e);
            throw e;
        }
    }

    public List<TaskResponseDTO> getTasks(Status status, Priority priority, User user) {
        log.debug("Fetching tasks for user: {} - Status filter: {}, Priority filter: {}", 
                user.getUsername(), status, priority);
        
        try {
            List<Task> tasks;

            if (status != null && priority != null) {
                log.debug("Querying tasks with both status and priority filters for user: {}", user.getUsername());
                tasks = taskRepository.findAllByUserAndStatusAndPriority(user, status, priority);
            } else if (status != null) {
                log.debug("Querying tasks with status filter for user: {}", user.getUsername());
                tasks = taskRepository.findAllByUserAndStatus(user, status);
            } else if (priority != null) {
                log.debug("Querying tasks with priority filter for user: {}", user.getUsername());
                tasks = taskRepository.findAllByUserAndPriority(user, priority);
            } else {
                log.debug("Querying all tasks for user: {}", user.getUsername());
                tasks = taskRepository.findAllByUser(user);
            }

            log.info("Retrieved {} tasks for user: {}", tasks.size(), user.getUsername());
            List<TaskResponseDTO> response = tasks.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
            log.debug("Converted {} tasks to response DTOs for user: {}", response.size(), user.getUsername());
            return response;
        } catch (Exception e) {
            log.error("Error fetching tasks for user: {} - Filters (Status: {}, Priority: {}) - Error: {}", 
                    user.getUsername(), status, priority, e.getMessage(), e);
            throw e;
        }
    }

    public TaskResponseDTO getTaskById(Long id, User user) {
        log.debug("Fetching task ID: {} for user: {}", id, user.getUsername());
        
        try {
            Task task = getTaskForUser(id, user);
            TaskResponseDTO response = mapToResponseDTO(task);
            log.info("Task ID: {} retrieved successfully for user: {}", id, user.getUsername());
            return response;
        } catch (ResponseStatusException e) {
            log.warn("Task ID: {} not found or access denied for user: {} - Error: {}", 
                    id, user.getUsername(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error fetching task ID: {} for user: {} - Error: {}", 
                    id, user.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO requestDTO, User user) {
        log.debug("Updating task ID: {} for user: {} - New Title: '{}', New Priority: {}, New Status: {}", 
                id, user.getUsername(), requestDTO.getTitle(), requestDTO.getPriority(), requestDTO.getStatus());
        
        try {
            Task task = getTaskForUser(id, user);
            log.debug("Task retrieved for update - ID: {}, Current Title: '{}', Current Status: {}", 
                    id, task.getTitle(), task.getStatus());

            task.setTitle(requestDTO.getTitle());
            task.setDescription(requestDTO.getDescription());
            task.setStatus(requestDTO.getStatus());
            task.setPriority(requestDTO.getPriority());

            log.debug("Task properties updated - ID: {}, Saving to database", id);
            Task updatedTask = taskRepository.save(task);
            log.info("Task ID: {} updated successfully for user: {}", id, user.getUsername());
            
            TaskResponseDTO response = mapToResponseDTO(updatedTask);
            log.debug("Task response DTO created for updated task ID: {}", id);
            return response;
        } catch (ResponseStatusException e) {
            log.warn("Task ID: {} not found or access denied for user: {} during update - Error: {}", 
                    id, user.getUsername(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error updating task ID: {} for user: {} - Error: {}", 
                    id, user.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    public void deleteTask(Long id, User user) {
        log.debug("Deleting task ID: {} for user: {}", id, user.getUsername());
        
        try {
            Task task = getTaskForUser(id, user);
            log.debug("Task retrieved for deletion - ID: {}, Title: '{}'", id, task.getTitle());
            
            taskRepository.delete(task);
            log.info("Task ID: {} deleted successfully for user: {}", id, user.getUsername());
        } catch (ResponseStatusException e) {
            log.warn("Task ID: {} not found or access denied for user: {} during deletion - Error: {}", 
                    id, user.getUsername(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error deleting task ID: {} for user: {} - Error: {}", 
                    id, user.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    private TaskResponseDTO mapToResponseDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .createdAt(task.getCreatedAt())
                .build();
    }

    private Task getTaskForUser(Long id, User user) {
        log.debug("Validating task access - Task ID: {}, User: {}", id, user.getUsername());
        return taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> {
                    log.warn("Task ID: {} not found for user: {} or user does not have access", id, user.getUsername());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found or access denied");
                });
    }
}