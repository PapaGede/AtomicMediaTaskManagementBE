package com.atomicmedia.taskmanagementsystem.controller;

import com.atomicmedia.taskmanagementsystem.dto.TaskRequest;
import com.atomicmedia.taskmanagementsystem.dto.TaskResponse;
import com.atomicmedia.taskmanagementsystem.exception.TaskNotFoundException;
import com.atomicmedia.taskmanagementsystem.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    private ObjectMapper objectMapper;
    private TaskResponse sampleResponse;
    private UUID taskId;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        taskId = UUID.randomUUID();
        sampleResponse = TaskResponse.builder()
                .id(taskId)
                .title("Test Task")
                .description("Description")
                .completed(false)
                .dueDate(LocalDateTime.of(2026, 3, 1, 12, 0))
                .assignedTo("developer")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateTaskShouldReturn201WhenValid() throws Exception {
        TaskRequest request = new TaskRequest("Test Task", "Description", null,
                LocalDateTime.of(2026, 3, 1, 12, 0), "developer");

        when(taskService.createTask(any(TaskRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testCreateTaskShouldReturn400WhenTitleBlank() throws Exception {
        TaskRequest request = new TaskRequest("", "Description", null, null, null);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSetTaskByIdShouldReturn404WhenNotFound() throws Exception {
        UUID missingId = UUID.randomUUID();
        when(taskService.getTaskById(missingId)).thenThrow(new TaskNotFoundException(missingId));

        mockMvc.perform(get("/api/tasks/" + missingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found with id: " + missingId));
    }

}