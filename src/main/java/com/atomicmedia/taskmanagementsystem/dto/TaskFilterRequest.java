package com.atomicmedia.taskmanagementsystem.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskFilterRequest(
        Boolean completed,
        LocalDateTime dueDateFrom,
        LocalDateTime dueDateTo,
        String search,
        String assignedTo
) {}
