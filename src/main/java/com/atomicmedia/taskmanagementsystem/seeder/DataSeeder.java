package com.atomicmedia.taskmanagementsystem.seeder;

import com.atomicmedia.taskmanagementsystem.model.Task;
import com.atomicmedia.taskmanagementsystem.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder {

    private final TaskRepository taskRepository;

    @PostConstruct
    public void seed() {
        if (taskRepository.count() > 0) return;

        List<Task> tasks = List.of(
                Task.builder()
                        .title("Set up project repository")
                        .description("Initialise the Git repo and configure CI/CD pipeline")
                        .completed(true)
                        .dueDate(LocalDateTime.of(2026, 2, 10, 9, 0))
                        .assignedTo("Alice")
                        .build(),
                Task.builder()
                        .title("Design database schema")
                        .description("Create the ERD and define table relationships for the task management system")
                        .completed(true)
                        .dueDate(LocalDateTime.of(2026, 2, 12, 17, 0))
                        .assignedTo("Bob")
                        .build(),
                Task.builder()
                        .title("Implement REST API endpoints")
                        .description("Build CRUD endpoints for tasks with validation and error handling")
                        .completed(false)
                        .dueDate(LocalDateTime.of(2026, 2, 20, 17, 0))
                        .assignedTo("Alice")
                        .build(),
                Task.builder()
                        .title("Build frontend task list view")
                        .description("Create the main task list page with filtering, sorting, and pagination")
                        .completed(false)
                        .dueDate(LocalDateTime.of(2026, 2, 25, 17, 0))
                        .assignedTo("Charlie")
                        .build(),
                Task.builder()
                        .title("Write unit tests")
                        .description("Add JUnit tests for the service layer and controller")
                        .completed(false)
                        .dueDate(LocalDateTime.of(2026, 3, 1, 12, 0))
                        .assignedTo("Bob")
                        .build(),
                Task.builder()
                        .title("Code review and refactoring")
                        .description("Review all pull requests and refactor where necessary")
                        .completed(false)
                        .dueDate(LocalDateTime.of(2026, 3, 5, 17, 0))
                        .assignedTo("Alice")
                        .build(),
                Task.builder()
                        .title("Update README documentation")
                        .description("Document setup instructions, API endpoints, and architectural decisions")
                        .completed(false)
                        .assignedTo("Charlie")
                        .build(),
                Task.builder()
                        .title("Performance testing")
                        .description("Run load tests and identify bottlenecks in the API")
                        .completed(false)
                        .dueDate(LocalDateTime.of(2026, 3, 10, 17, 0))
                        .build()
        );

        taskRepository.saveAll(tasks);
    }
}
