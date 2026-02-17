package com.atomicmedia.taskmanagementsystem.service.impl;

import com.atomicmedia.taskmanagementsystem.dto.TaskRequest;
import com.atomicmedia.taskmanagementsystem.dto.TaskResponse;
import com.atomicmedia.taskmanagementsystem.model.Task;
import com.atomicmedia.taskmanagementsystem.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task sampleTask;
    private UUID taskId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        sampleTask = Task.builder()
                .id(taskId)
                .title("Test Task")
                .description("A test task")
                .completed(false)
                .dueDate(LocalDateTime.of(2026, 3, 1, 12, 0))
                .assignedTo("developer")
                .build();
        sampleTask.setCreatedAt(LocalDateTime.now());
        sampleTask.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateTaskShouldSaveAndReturnTask() {
        TaskRequest request = new TaskRequest("Test Task", "A test task", null,
                LocalDateTime.of(2026, 3, 1, 12, 0), "developer");

        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        TaskResponse response = taskService.createTask(request);

        assertThat(response.title()).isEqualTo("Test Task");
        assertThat(response.description()).isEqualTo("A test task");
        assertThat(response.completed()).isFalse();
        assertThat(response.assignedTo()).isEqualTo("developer");
        verify(taskRepository).save(any(Task.class));
    }
}