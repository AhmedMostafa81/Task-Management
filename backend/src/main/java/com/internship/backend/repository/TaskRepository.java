package com.internship.backend.repository;

import com.internship.backend.entity.Task;
import com.internship.backend.entity.User;
import com.internship.backend.enums.Priority;
import com.internship.backend.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find a specific task ensuring it belongs to the logged-in user
    Optional<Task> findByIdAndUser(Long id, User user);

    // Find all tasks belonging to the user
    List<Task> findAllByUser(User user);

    // Find tasks belonging to the user filtered by status
    List<Task> findAllByUserAndStatus(User user, Status status);

    // Find tasks belonging to the user filtered by priority
    List<Task> findAllByUserAndPriority(User user, Priority priority);

    // Find tasks belonging to the user filtered by both status and priority
    List<Task> findAllByUserAndStatusAndPriority(User user, Status status, Priority priority);
}