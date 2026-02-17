package com.atomicmedia.taskmanagementsystem.service;

import com.atomicmedia.taskmanagementsystem.dto.TaskFilterRequest;
import com.atomicmedia.taskmanagementsystem.dto.TaskRequest;
import com.atomicmedia.taskmanagementsystem.dto.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);
    Page<TaskResponse> getAllTasks(TaskFilterRequest filter, Pageable pageable);
    TaskResponse getTaskById(UUID id);
    TaskResponse updateTask(UUID id, TaskRequest request);
    void deleteTask(UUID id);
}
