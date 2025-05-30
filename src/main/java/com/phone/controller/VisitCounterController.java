package com.phone.controller;

import com.phone.service.VisitCounterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Счётчик посещений", description = "API для получения количества обращений к серверу")
@RestController
@RequestMapping("/visits")
public class VisitCounterController {

    private final VisitCounterService visitCounterService;

    public VisitCounterController(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @Operation(summary = "Получить количество посещений", description = "Возвращает текущее значение счётчика обращений")
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        Long count = visitCounterService.getCounter();
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Обнулить количество посещений", description = "Обнуляет количество посещений")
    @PostMapping("/reset")
    public void reset() {
        visitCounterService.reset();
    }
}
