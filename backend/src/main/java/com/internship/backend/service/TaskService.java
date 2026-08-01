package com.internship.backend.service;
import com.internship.backend.dto.TaskRequestDTO;
import com.internship.backend.dto.TaskResponseDTO;
import com.internship.backend.entity.Task;
import com.internship.backend.entity.User;
import com.internship.backend.enums.Priority;
import com.internship.backend.enums.Status;
import com.internship.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskResponseDTO createTask(TaskRequestDTO requestDTO, User user) {
        Task task = Task.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .status(requestDTO.getStatus())
                .priority(requestDTO.getPriority())
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToResponseDTO(savedTask);
    }

    public List<TaskResponseDTO> getTasks(Status status, Priority priority, User user) {
        List<Task> tasks;

        if (status != null && priority != null) {
            tasks = taskRepository.findAllByUserAndStatusAndPriority(user, status, priority);
        } else if (status != null) {
            tasks = taskRepository.findAllByUserAndStatus(user, status);
        } else if (priority != null) {
            tasks = taskRepository.findAllByUserAndPriority(user, priority);
        } else {
            tasks = taskRepository.findAllByUser(user);
        }

        return tasks.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    public TaskResponseDTO getTaskById(Long id, User user) {
        Task task = getTaskForUser(id, user);
        return mapToResponseDTO(task);
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO requestDTO, User user) {
        Task task = getTaskForUser(id, user);

        task.setTitle(requestDTO.getTitle());
        task.setDescription(requestDTO.getDescription());
        task.setStatus(requestDTO.getStatus());
        task.setPriority(requestDTO.getPriority());

        Task updatedTask = taskRepository.save(task);
        return mapToResponseDTO(updatedTask);
    }

    public void deleteTask(Long id, User user) {
        Task task = getTaskForUser(id, user);
        taskRepository.delete(task);
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
        return taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found or access denied"));
    }
}