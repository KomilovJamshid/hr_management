package uz.jamshid.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.jamshid.hrmanagement.payload.ApiResponse;
import uz.jamshid.hrmanagement.payload.SalaryDto;
import uz.jamshid.hrmanagement.service.SalaryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {
    @Autowired
    SalaryService salaryService;

    @PostMapping("/paySalary")
    public HttpEntity<?> paySalary(@RequestBody SalaryDto salaryDto) {
        ApiResponse apiResponse = salaryService.paySalary(salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }

    @GetMapping("/{userId}")
    public HttpEntity<?> getSalary(@PathVariable UUID userId) {
        ApiResponse apiResponse = salaryService.getSalary(userId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }
}
