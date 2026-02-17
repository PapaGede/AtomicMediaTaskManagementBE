package com.atomicmedia.taskmanagementsystem.service.impl;

import com.atomicmedia.taskmanagementsystem.dto.TaskRequest;
import com.atomicmedia.taskmanagementsystem.dto.TaskResponse;
import com.atomicmedia.taskmanagementsystem.exception.TaskNotFoundException;
import com.atomicmedia.taskmanagementsystem.model.Task;
import com.atomicmedia.taskmanagementsystem.repository.TaskRepository;
import com.atomicmedia.taskmanagementsystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .completed(request.completed() != null ? request.completed() : false)
                .dueDate(request.dueDate())
                .assignedTo(request.assignedTo())
                .build();

        Task saved = taskRepository.save(task);
        return TaskResponse.from(saved);
    }

    @Override
    @Transactional
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public TaskResponse getTaskById(UUID id) {
        return TaskResponse.from(taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id)));
    }
}
