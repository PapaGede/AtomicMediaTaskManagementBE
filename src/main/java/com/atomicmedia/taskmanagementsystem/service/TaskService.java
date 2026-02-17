package com.atomicmedia.taskmanagementsystem.service;

import com.atomicmedia.taskmanagementsystem.dto.TaskRequest;
import com.atomicmedia.taskmanagementsystem.dto.TaskResponse;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);
}
