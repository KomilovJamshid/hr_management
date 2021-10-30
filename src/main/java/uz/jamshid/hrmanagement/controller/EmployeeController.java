package uz.jamshid.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.jamshid.hrmanagement.payload.ApiResponse;
import uz.jamshid.hrmanagement.payload.EmployeeDto;
import uz.jamshid.hrmanagement.service.EmployeeService;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping
    public HttpEntity<?> getAllEmployees() {
        ApiResponse apiResponse = employeeService.getAllEmployees();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }

    @GetMapping("/{id}&{begin}&{end}")
    public HttpEntity<?> getAllDataAboutOneEmployee(@PathVariable UUID id, @PathVariable Date begin, @PathVariable Date end) {
        ApiResponse apiResponse = employeeService.getAllDataAboutOneEmployee(id, begin, end);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }
}
