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

    Optional<Task> findByIdAndUser(Long id, User user);

    List<Task> findAllByUser(User user);

    List<Task> findAllByUserAndStatus(User user, Status status);

    List<Task> findAllByUserAndPriority(User user, Priority priority);

    List<Task> findAllByUserAndStatusAndPriority(User user, Status status, Priority priority);
}