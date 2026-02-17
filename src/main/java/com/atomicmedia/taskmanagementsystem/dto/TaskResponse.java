package com.atomicmedia.taskmanagementsystem.dto;

import com.atomicmedia.taskmanagementsystem.model.Task;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record TaskResponse(
        UUID id,
        String title,
        String description,
        boolean completed,
        LocalDateTime dueDate,
        String assignedTo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TaskResponse from(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .dueDate(task.getDueDate())
                .assignedTo(task.getAssignedTo())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
