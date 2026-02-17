package com.atomicmedia.taskmanagementsystem.service;

import com.atomicmedia.taskmanagementsystem.dto.TaskRequest;
import com.atomicmedia.taskmanagementsystem.dto.TaskResponse;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);
    List<TaskResponse> getAllTasks();
    TaskResponse getTaskById(UUID id);
}
