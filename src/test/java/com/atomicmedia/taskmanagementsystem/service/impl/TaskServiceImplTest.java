package com.atomicmedia.taskmanagementsystem.service.impl;

import com.atomicmedia.taskmanagementsystem.dto.TaskFilterRequest;
import com.atomicmedia.taskmanagementsystem.dto.TaskRequest;
import com.atomicmedia.taskmanagementsystem.dto.TaskResponse;
import com.atomicmedia.taskmanagementsystem.exception.TaskNotFoundException;
import com.atomicmedia.taskmanagementsystem.model.Task;
import com.atomicmedia.taskmanagementsystem.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void testGetTaskByIdShouldReturnTaskWhenExists() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(sampleTask));

        TaskResponse response = taskService.getTaskById(taskId);

        assertThat(response.id()).isEqualTo(taskId);
        assertThat(response.title()).isEqualTo("Test Task");
    }

    @Test
    void testShouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setId(UUID.randomUUID());
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(UUID.randomUUID());
        task2.setTitle("Task 2");

        Page<Task> page = new PageImpl<>(List.of(task1, task2));

        when(taskRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        TaskFilterRequest filter = new TaskFilterRequest(null, null, null, null, null);

        Pageable pageable = PageRequest.of(0, 10);

        Page<TaskResponse> result = taskService.getAllTasks(filter, pageable);

        assertEquals(2, result.getContent().size());
    }

    @Test
    void testShouldReturnEmptyListWhenNoTasksExist() {
        Page<Task> emptyPage = new PageImpl<>(Collections.emptyList());

        when(taskRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        TaskFilterRequest filter = new TaskFilterRequest(
                null, null, null, null, null
        );

        Pageable pageable = PageRequest.of(0, 10);

        Page<TaskResponse> result = taskService.getAllTasks(filter, pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    void testShouldThrowExceptionWhenTaskNotFound() {
        UUID taskId = UUID.randomUUID();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId));
    }

    @Test
    void testShouldUpdateTaskSuccessfully() {
        UUID id = UUID.randomUUID();

        Task existingTask = new Task();
        existingTask.setId(id);

        when(taskRepository.findById(id)).thenReturn(Optional.of(existingTask));

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskRequest request = new TaskRequest(
                "Updated Title",
                "Updated Description",
                true,
                LocalDateTime.now().plusDays(1),
                "John"
        );

        TaskResponse response = taskService.updateTask(id, request);
        assertEquals("Updated Title", response.title());
    }

    @Test
    void testShouldDeleteTaskSuccessfully() {
        UUID id = UUID.randomUUID();
        Task task = new Task();
        task.setId(id);

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        taskService.deleteTask(id);

        verify(taskRepository).delete(task);
    }
}