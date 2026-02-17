package com.atomicmedia.taskmanagementsystem.service.impl;

import com.atomicmedia.taskmanagementsystem.dto.TaskRequest;
import com.atomicmedia.taskmanagementsystem.dto.TaskResponse;
import com.atomicmedia.taskmanagementsystem.model.Task;
import com.atomicmedia.taskmanagementsystem.repository.TaskRepository;
import com.atomicmedia.taskmanagementsystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
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
}
