package com.atomicmedia.taskmanagementsystem.specification;

import com.atomicmedia.taskmanagementsystem.model.Task;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TaskSpecification {

    public static Specification<Task> hasCompletionStatus(Boolean completed) {
        return (root, query, cb) ->
                completed == null ? null : cb.equal(root.get("completed"), completed);
    }

    public static Specification<Task> dueDateAfter(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("dueDate"), from);
    }

    public static Specification<Task> dueDateBefore(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThanOrEqualTo(root.get("dueDate"), to);
    }

    public static Specification<Task> titleContains(String keyword) {
        return (root, query, cb) ->
                keyword == null || keyword.isBlank() ? null : cb.like(cb.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Task> hasAssignee(String assignedTo) {
        return (root, query, cb) ->
                assignedTo == null || assignedTo.isBlank() ? null : cb.equal(cb.lower(root.get("assignedTo")), assignedTo.toLowerCase());
    }
}
