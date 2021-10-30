package uz.jamshid.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.jamshid.hrmanagement.payload.ApiResponse;
import uz.jamshid.hrmanagement.payload.TaskDto;
import uz.jamshid.hrmanagement.service.TaskService;

import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping("/addTask")
    public HttpEntity<?> addTask(@RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.addTask(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 401).body(apiResponse);
    }

    @GetMapping("/getTask")
    public HttpEntity<?> getTask(@RequestParam String email, @RequestParam UUID taskId) {
        ApiResponse apiResponse = taskService.getTask(email, taskId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/completeTask/{id}")
    public HttpEntity<?> completeTask(@PathVariable UUID id) {
        ApiResponse apiResponse = taskService.completeTask(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 401).body(apiResponse);
    }
}
