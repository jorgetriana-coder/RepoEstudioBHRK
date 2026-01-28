package com.bhrk.taskmanajemet.controller;

import com.bhrk.taskmanajemet.dto.TaskRequestDTO;
import com.bhrk.taskmanajemet.dto.TaskResponseDTO;
import com.bhrk.taskmanajemet.entity.Task;
import com.bhrk.taskmanajemet.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/users/{userId}/task")
    public ResponseEntity<TaskResponseDTO> createdTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO, @PathVariable Integer userId) {
        TaskResponseDTO taskSaved = taskService.create(taskRequestDTO, userId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(taskSaved.getId())
                .toUri();
        return ResponseEntity.created(location).body(taskSaved);
    }

    @GetMapping("/users/{userId}/task")
    public ResponseEntity<List<TaskResponseDTO>> findAllTask(@PathVariable Integer userId) {
        List<TaskResponseDTO> tasks = taskService.findAll(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/users/{userId}/task/{taskId}")
    public ResponseEntity<EntityModel<TaskResponseDTO>> findById(
            @PathVariable(name = "userId") Integer userId,
            @PathVariable(name = "taskId") Integer id) {
        TaskResponseDTO task = taskService.findById(userId, id);
        EntityModel<TaskResponseDTO> model = EntityModel.of(task);
        model.add(
                linkTo(methodOn(this.getClass())
                        .findAllTask(userId))
                        .withRel("all-tasks")
        );
        return ResponseEntity.ok(model);


    }
    @PutMapping("/users/{userId}/task/{taskId}")
    public TaskResponseDTO upDateTask(
            @PathVariable Integer userId,
            @RequestBody TaskRequestDTO task,
            @PathVariable Integer taskId) {
        return taskService.upDateTask(userId, task,taskId);
    }

    @DeleteMapping("/users/{userId}/task/{taskId}")
    public ResponseEntity<Void> deleteUsers(@PathVariable Integer taskId,@PathVariable Integer userId) {
        taskService.deleteByID(userId,taskId);
        return ResponseEntity.noContent().build();
    }

}
