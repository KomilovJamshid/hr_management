package uz.jamshid.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.jamshid.hrmanagement.payload.ApiResponse;
import uz.jamshid.hrmanagement.payload.TourniquetDto;
import uz.jamshid.hrmanagement.service.TourniquetService;

@RestController
@RequestMapping("/api/tourniquet")
public class TourniquetController {
    @Autowired
    TourniquetService tourniquetService;

    @PostMapping
    public HttpEntity<?> tourniquetCheck(@RequestBody TourniquetDto tourniquetDto) {
        ApiResponse apiResponse = tourniquetService.tourniquetCheck(tourniquetDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }
}
